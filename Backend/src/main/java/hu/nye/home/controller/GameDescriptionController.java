package hu.nye.home.controller;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/gd-api")
@RestController
public class GameDescriptionController {
    
    private final GameDescriptionServiceInterface gameDescriptionService;
    
    @Autowired
    public GameDescriptionController(GameDescriptionServiceInterface gameDescriptionService) {
        this.gameDescriptionService = gameDescriptionService;
    }
    
    @PostMapping("/user/{userId}/gameDescriptions")
    public ResponseEntity<GameDescriptionDto> createGameDescription(
      @PathVariable(value = "userId") Long userId,
      @RequestPart(value = "dto") GameDescriptionDto dto,
      @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(gameDescriptionService.saveGameDescription(userId, dto, image),
          HttpStatus.CREATED);
    }
    
    //Lekéri az összes leírást, ami az adott felhasználóhoz tartozik
    // (nem a szűréshez tartozik,hanem a játékleírásaim oldalhoz)
    @GetMapping("/user/{userId}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByUserId(@PathVariable(value = "userId") Long userId) {
        return gameDescriptionService.getGameDescriptionsByUserId(userId);
    }
    
    @GetMapping("/genre/{genre}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByGenre(@PathVariable(value = "genre") String genre) {
        return gameDescriptionService.getAllByGenre(genre);
    }
    
    @GetMapping("/platform/{platform}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByPlatform(@PathVariable(value = "platform")
                                                                      String platform) {
        return gameDescriptionService.getAllByPlatform(platform);
    }
    
    @GetMapping("/agelimit/{agelimit}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByAgeLimit(@PathVariable(value = "agelimit")
                                                                      int ageLimit) {
        return gameDescriptionService.getAllByAgeLimit(ageLimit);
    }
    
    @GetMapping("/publisher/{publisher}/gameDescriptions")
    public List<GameDescriptionDto> getGameDescriptionsByPublisher(@PathVariable(value = "publisher")
                                                                       String publisher) {
        return gameDescriptionService.getAllByPublisher(publisher);
    }
    
    //https://www.youtube.com/watch?v=UHEDH3lnLb8&t=471s
    @GetMapping("/gameDescriptions/publishedAt")
    public List<GameDescriptionDto> getGameDescriptionsByPublishedAtBetween(@RequestParam int min,
                                                                            @RequestParam int max) {
        return gameDescriptionService.getAllByPublishedAtBetween(min, max);
    }
    
    //Lekér egy adott leírást id alapján, ami majd a játék leírás betöltésnél lesz hasznos
    @GetMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<GameDescriptionDto> getGameDescriptionById(
      @PathVariable(value = "userId") Long userId,
      @PathVariable(value = "id") Long gameDescriptionId) {
        GameDescriptionDto dto = gameDescriptionService.getGameDescriptionById(userId, gameDescriptionId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @GetMapping("/gameDescriptions")
    public List<GameDescriptionDto> getAllGameDescription(){
        return gameDescriptionService.getAllGameDescription();
    }
    
    @GetMapping("/name/{name}/gameDescription")
    public ResponseEntity<GameDescriptionDto> getGameDescriptionByName(@PathVariable(value = "name")
                                                                           String name){
        GameDescriptionDto dto = gameDescriptionService.getByName(name);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PutMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<GameDescriptionDto> updateGameDescription(
      @PathVariable(value = "userId") Long userId,
      @PathVariable(value = "id") Long gameDescriptionId,
      @RequestPart(value = "dto") GameDescriptionDto dto,
      @RequestPart(value = "image", required = false) MultipartFile file) {
        GameDescriptionDto updatedGameDescription = gameDescriptionService
                                                      .updateGameDescription(gameDescriptionId,
                                                        userId, dto, file);
        return new ResponseEntity<>(updatedGameDescription, HttpStatus.OK);
    }
    
    @DeleteMapping("/user/{userId}/gameDescriptions/{id}")
    public ResponseEntity<String> deleteGameDescription(@PathVariable(value = "userId") Long userId,
                                                        @PathVariable(value = "id") Long gameDescriptionId) {
        gameDescriptionService.deleteGameDescription(userId, gameDescriptionId);
        return new ResponseEntity<>("Game description deleted successfully", HttpStatus.OK);
    }
    
}
