package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.dto.GameDescriptionFilterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameDescriptionServiceInterface {
    
    List<GameDescriptionDto> getGameDescriptionsByUserId(Long id);
    GameDescriptionDto getGameDescriptionById(Long gameDescriptionId);
    List<GameDescriptionDto> getAllGameDescription();
    
    GameDescriptionDto saveGameDescription(Long userId, GameDescriptionDto dto,
                                           MultipartFile imageFile);
    GameDescriptionDto updateGameDescription(Long gameDescriptionId, Long userId,
                                             GameDescriptionDto dto, MultipartFile imageFile);
    GameDescriptionDto updateAvgRating(Long gameDescriptionId, int avgRating);
    void deleteGameDescription(Long gameDescriptionId);
    
    List<GameDescriptionDto> getAllByFilters(GameDescriptionFilterRequest filters);
    List<GameDescriptionDto> getAllGameDescriptionsSortedByNameAsc();
    List<GameDescriptionDto> getAllGameDescriptionsSortedByNameDesc();
    List<GameDescriptionDto> getAllGameDescriptionsSortedByUserNameAsc();
    List<GameDescriptionDto> getAllGameDescriptionsSortedByUserNameDesc();
    List<GameDescriptionDto> searchByName(String name);
    void calculateGameDescriptionRating(Long gameDescriptionId);
    boolean isAdmin();
    boolean isModerator();
}
