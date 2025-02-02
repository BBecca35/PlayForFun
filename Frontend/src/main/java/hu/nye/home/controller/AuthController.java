package hu.nye.home.controller;

import hu.nye.home.dto.AuthResponseDto;
import hu.nye.home.dto.UserDto;
import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.security.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;
    //private final RefreshTokenService refreshTokenService;
    
    public AuthController(AuthenticationManager authenticationManager, JWTGenerator jwtGenerator,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserDto userDto){
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        Optional<UserModel> user = userRepository.findByUsername(userDto.getUsername());
        Long userId = user.map(UserModel::getId).orElse(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtGenerator.generateAccessToken(authentication.getName());
        String refreshToken = jwtGenerator.generateRefreshToken(authentication.getName());
        return new ResponseEntity<>(new AuthResponseDto(accessToken, refreshToken, userId), HttpStatus.OK);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (jwtGenerator.validateToken(refreshToken)) {
            String username = jwtGenerator.getUsernameFromJWT(refreshToken);
            UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(username, null);
            String newAccessToken = jwtGenerator.generateAccessToken(authenticationToken.getName());
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
    }
}
