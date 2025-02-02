package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.dto.GameDescriptionFilterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//szűrő lekérdezések: kiadás éve(between), korhatár, név, kiadó,
// műfaj, Platform, leírás készítője

//2024.01.09: A játék leírás készítése során a korhatár, a plartform, a kiadás éve és műfaj
// egy legördülő menüpontok lesznek, ahol előre megadott választási lehetőségek alapján
// lehet kiválasztani a számunkra megfelelő opciót. Így a szűrés is pontosabb tud majd lenni.

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
    void updateGameDescriptionRating(Long gameDescriptionId);
    
}
