package hu.nye.home.controller;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.dto.GameDescriptionFilterRequest;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    //Lekér egy adott leírást id alapján, ami majd a játék leírás betöltésnél lesz hasznos
    @GetMapping("/gameDescriptions/{id}")
    public ResponseEntity<GameDescriptionDto> getGameDescriptionById(
      @PathVariable(value = "id") Long gameDescriptionId) {
        GameDescriptionDto dto = gameDescriptionService.getGameDescriptionById(gameDescriptionId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @GetMapping("/gameDescriptions")
    public List<GameDescriptionDto> getAllGameDescription(){
        return gameDescriptionService.getAllGameDescription();
    }
    
    @GetMapping("/name/{name}/gameDescription")
    public ResponseEntity<List<GameDescriptionDto>> getGameDescriptionByName(
      @PathVariable(value = "name") String name){
        List<GameDescriptionDto> dto = gameDescriptionService.searchByName(name);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PostMapping("/gameDescriptions/filter")
    public List<GameDescriptionDto> getGameDescriptionsByFilters(
      @RequestBody GameDescriptionFilterRequest filters) {
        return gameDescriptionService.getAllByFilters(filters);
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
    
    @DeleteMapping("/gameDescriptions/{id}")
    public ResponseEntity<String> deleteGameDescription(@PathVariable(value = "id") Long gameDescriptionId) {
        gameDescriptionService.deleteGameDescription(gameDescriptionId);
        return new ResponseEntity<>("Game description deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/gameDescriptions/sorted/ByNameAsc")
    public ResponseEntity<List<GameDescriptionDto>> getGameDescriptionsSortedByNameAsc() {
        List<GameDescriptionDto> gameDescriptions = gameDescriptionService.getAllGameDescriptionsSortedByNameAsc();
        return ResponseEntity.ok(gameDescriptions);
    }
    
    @GetMapping("/gameDescriptions/sorted/ByNameDesc")
    public ResponseEntity<List<GameDescriptionDto>> getGameDescriptionsSortedByNameDesc() {
        List<GameDescriptionDto> gameDescriptions = gameDescriptionService.getAllGameDescriptionsSortedByNameDesc();
        return ResponseEntity.ok(gameDescriptions);
    }
    
    @GetMapping("/gameDescriptions/sorted/ByUserNameAsc")
    public ResponseEntity<List<GameDescriptionDto>> getGameDescriptionsSortedByUserNameAsc() {
        List<GameDescriptionDto> gameDescriptions = gameDescriptionService.getAllGameDescriptionsSortedByUserNameAsc();
        return ResponseEntity.ok(gameDescriptions);
    }
    
    @GetMapping("/gameDescriptions/sorted/ByUserNameDesc")
    public ResponseEntity<List<GameDescriptionDto>> getGameDescriptionsSortedByUserNameDesc() {
        List<GameDescriptionDto> gameDescriptions = gameDescriptionService.getAllGameDescriptionsSortedByUserNameDesc();
        return ResponseEntity.ok(gameDescriptions);
    }
}
