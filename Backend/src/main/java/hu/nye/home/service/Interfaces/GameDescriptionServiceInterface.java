package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.entity.GameDescriptionModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameDescriptionServiceInterface {
    
    List<GameDescriptionDto> getGameDescriptionsByUserId(Long id);
    GameDescriptionDto getGameDescriptionById(Long userId, Long gameDescriptionId);
    GameDescriptionDto saveGameDescription(Long userId, GameDescriptionDto dto);
    GameDescriptionDto updateGameDescription(Long gameDescriptionId, Long userId,
                                             GameDescriptionDto dto, MultipartFile imageFile);
    void deleteGameDescription(Long gameDescriptionId, Long userId);
}
