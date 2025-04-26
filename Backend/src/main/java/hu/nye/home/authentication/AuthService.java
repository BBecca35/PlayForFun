package hu.nye.home.authentication;

import hu.nye.home.entity.BanModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.BannedUserException;
import hu.nye.home.exception.TokenIsExpiredException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.BanRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.security.CustomUserDetailsService;
import hu.nye.home.security.JwtTokenService;
import hu.nye.home.security.SecurityConstants;
import hu.nye.home.service.Classes.BanService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService implements AuthServiceInterface {
    
    private final UserRepository userRepository;
    private final BanRepository banRepository;
    private final BanService banService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    public AuthService(UserRepository userRepository, BanRepository banRepository, BanService banService,
                       JwtTokenService jwtTokenService,
                       AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
        this.banService = banService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @Override
    @SneakyThrows
    public void authenticate(AuthRequestDto authRequestDto,
                                          HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequestDto.getUsername());
        if(!userRepository.existsByUsername(authRequestDto.getUsername())){
            throw new UserNotFoundException();
        }
        
        UserModel user = userRepository.findByUsername(authRequestDto.getUsername());
        BanModel ban = banRepository.findValidBanByUserId(user.getId());
        if(ban != null){
            if(ban.getBanExpiration() != -1){
                boolean isExpiredBan = banService.validateBan(ban.getBannedAt(), ban.getBanExpiration());
                if (!isExpiredBan) {
                    throw new BannedUserException(ban);
                }else{
                    ban.setExpired(true);
                    ban.setBanExpiration(0);
                    banRepository.save(ban);
                }
            }
            else{
                throw new BannedUserException(ban);
            }
        }
        
        boolean rememberMe = authRequestDto.isCheckedRememberMe();
        long expirationTime;
        
        if (rememberMe) {
            expirationTime = SecurityConstants.JWT_REFRESH_EXPIRATION;
        } else {
            expirationTime = SecurityConstants.JWT_EXPIRATION;
        }
        
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails, expirationTime);
        //System.out.println(accessToken);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                                         .httpOnly(true)
                                         .path("/api/auth")
                                         .maxAge(604800)
                                         .sameSite("Lax")
                                         .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        
    }
    
    @Override
    @SneakyThrows
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        String oldAccessToken = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            oldAccessToken = authHeader.substring(7);
        }
        
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        
        if (refreshToken == null || oldAccessToken == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Access or refresh token not found\"}");
            return;
        }
        
        String username = jwtTokenService.getUsernameFromJWT(refreshToken);
        
        if(username != null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(jwtTokenService.isTokenValid(refreshToken, userDetails)){
                String newAccessToken = jwtTokenService.generateAccessToken(userDetails);
                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
                response.setStatus(HttpServletResponse.SC_OK);
            }else {
                throw new TokenIsExpiredException();
            }
        }else{
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Username not found\"}");
        }
    }
}
