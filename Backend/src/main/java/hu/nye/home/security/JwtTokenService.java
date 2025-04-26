package hu.nye.home.security;

import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    
    private final UserRepository userRepository;
    
    public String generateAccessToken(UserDetails userDetails) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        UserModel user = userRepository.findByUsername(userDetails.getUsername());
        
        return Jwts.builder()
                 .setSubject(userDetails.getUsername())
                 .claim("role", user.getRole())
                 .claim("userId", user.getId())
                 .setIssuedAt(new Date())
                 .setExpiration(expireDate)
                 .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                 .compact();
    }
    
    public String generateRefreshToken(UserDetails userDetails, long expirationTime) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expirationTime);
        
        return Jwts.builder()
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date())
                 .setExpiration(expireDate)
                 .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                 .compact();
    }
    
    public String getUsernameFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                 .setSigningKey(getSignInKey())
                 .build()
                 .parseClaimsJws(token)
                 .getBody();
    }
    
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWT(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
