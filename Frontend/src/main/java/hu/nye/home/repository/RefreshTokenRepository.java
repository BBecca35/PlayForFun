package hu.nye.home.repository;

import hu.nye.home.entity.RefreshToken;
import hu.nye.home.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository <RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(UserModel user);
    
}
