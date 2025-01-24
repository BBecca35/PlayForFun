package hu.nye.home.service.Classes;

import hu.nye.home.entity.RefreshToken;
import hu.nye.home.entity.UserModel;
import hu.nye.home.repository.RefreshTokenRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.RefreshTokenServiceInterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenServiceInterFace {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;
    
    @Override
    public String generateRefreshToken(UserModel user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
        refreshTokenRepository.save(refreshToken);
        return token;
    }
    
    @Override
    public boolean validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                                      .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }
        return true;
    }
    
    @Override
    public void deleteByUser(UserModel user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
