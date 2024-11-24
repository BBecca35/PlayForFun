package hu.nye.home.controller;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class GameDescriptionController {

    private final GameDescriptionServiceInterface gameDescriptionService;
    
    @Autowired
    public GameDescriptionController(GameDescriptionServiceInterface gameDescriptionService) {
        this.gameDescriptionService = gameDescriptionService;
    }
    
    @PostMapping("/user/{userId}/gameDescriptions")
    public ResponseEntity<GameDescriptionDto> creategameDescription(@PathVariable(value = "userId")
                                                                        Long userId,
                                                                    @RequestBody GameDescriptionDto dto) {
        return new ResponseEntity<>(gameDescriptionService.saveGameDescription(userId, dto),
          HttpStatus.CREATED);
    }
    
    @GetMapping("/user/{userId}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByUserId(@PathVariable(value = "userId")
                                                                    Long userId) {
        return gameDescriptionService.getGameDescriptionsByUserId(userId);
    }
    
    @GetMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<GameDescriptionDto> getGameDescriptionsById(@PathVariable(value = "userId")
                                                       Long userId, @PathVariable(value = "id")
    Long gameDescriptionId) {
        GameDescriptionDto dto = gameDescriptionService.getGameDescriptionById(userId, gameDescriptionId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PutMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<GameDescriptionDto> updateGameDescription(@PathVariable(value = "userId")
                                                                        Long userId,
                                                                    @PathVariable(value = "id")
                                                                    Long gameDescriptionId,
                                                                    @RequestBody GameDescriptionDto dto,
                                                                    @RequestBody MultipartFile file) {
        GameDescriptionDto updatedGameDescription = gameDescriptionService.
                                                      updateGameDescription(userId,
                                                        gameDescriptionId, dto, file);
        return new ResponseEntity<>(updatedGameDescription, HttpStatus.OK);
    }
    
    @DeleteMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<String> deleteGameDescription(@PathVariable(value = "userId") Long userId,
                                                        @PathVariable(value = "id") Long gameDescriptionId) {
        gameDescriptionService.deleteGameDescription(userId, gameDescriptionId);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }
    
}
