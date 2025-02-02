package hu.nye.home.service.Interfaces;

import hu.nye.home.entity.UserModel;

public interface RefreshTokenServiceInterFace {
    
    String generateRefreshToken(UserModel user);
    boolean validateRefreshToken(String token);
    void deleteByUser(UserModel user);
}
