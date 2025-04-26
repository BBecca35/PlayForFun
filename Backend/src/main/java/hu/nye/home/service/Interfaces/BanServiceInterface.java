package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.BanDetails;
import hu.nye.home.dto.BanDto;

import java.time.LocalDateTime;

public interface BanServiceInterface {
    
    BanDto banUser(BanDetails details);
    BanDto getValidBan(Long userId);
    BanDto updateBanDetails(BanDetails details);
    void invalidatingBanFromUser(Long userId);
    boolean validateBan(LocalDateTime date, int expirationTime);
    void invalidatingAllBan();
}
