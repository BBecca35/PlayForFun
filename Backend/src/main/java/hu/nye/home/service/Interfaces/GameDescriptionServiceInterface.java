package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.entity.GameDescriptionModel;

public interface GameDescriptionServiceInterface {
    
    GameDescriptionModel getGameDescriptionById(Long id);
    GameDescriptionModel saveGameDescription(GameDescriptionDto dto);
    GameDescriptionModel updateGameDescription(Long id, GameDescriptionDto dto);
    void deleteGameDescription(Long id);
}
