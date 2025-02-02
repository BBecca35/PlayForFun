package hu.nye.home.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {
    
    public String generateAccessToken(String username) {
        return generateToken(username, SecurityConstants.JWT_EXPIRATION);
    }
    
    public String generateRefreshToken(String username) {
        return generateToken(username, SecurityConstants.JWT_REFRESH_EXPIRATION);
    }
    
    public String generateToken(String username, Long expirationTime) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expirationTime);
        
        Key key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes());
        
        return Jwts.builder()
                 .setSubject(username)
                 .setIssuedAt(new Date())
                 .setExpiration(expireDate)
                 .signWith(key, SignatureAlgorithm.HS256)
                 .compact();
    }
    
    public String getUsernameFromJWT(String token) {
        Key key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes());
        
        Claims claims = Jwts.parser()
                          .setSigningKey(key)
                          .build()
                          .parseClaimsJws(token)
                          .getBody();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes());
            Jwts.parser()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect", ex.fillInStackTrace());
        }
    }
}
