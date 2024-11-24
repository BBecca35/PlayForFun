package hu.nye.home.service.Classes;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.ImageModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.GameDescriptionIsExistException;
import hu.nye.home.exception.GameDescriptionNotFoundException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.GameDescriptionRepository;
import hu.nye.home.repository.ImageRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

//https://github.com/teddysmithdev/pokemon-review-springboot/blob/master/src/main/java/com
//pokemonreview/api/service/impl/ReviewServiceImpl.java
@Service
public class GameDescriptionService implements GameDescriptionServiceInterface {
    
    private final GameDescriptionRepository gameDescriptionRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    
    @Autowired
    public GameDescriptionService(GameDescriptionRepository gameDescriptionRepository,
                                  UserRepository userRepository, ImageService imageService) {
        this.gameDescriptionRepository = gameDescriptionRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }
    
    
    @Override
    public List<GameDescriptionDto> getGameDescriptionsByUserId(Long id) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByUserId(id);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionDto getGameDescriptionById(Long userId, Long gameDescriptionId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.
                                                 findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        if(gameDescription.getUser().getId() != user.getId()){
            throw new GameDescriptionNotFoundException();
        }
        return mapToDto(gameDescription);
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionDto saveGameDescription(Long userId, GameDescriptionDto dto) {
        GameDescriptionModel gameDescription = mapToEntity(dto);
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        gameDescription.setUser(user);
        GameDescriptionModel newGameDescription = gameDescriptionRepository.save(gameDescription);
        return mapToDto(newGameDescription);
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionDto updateGameDescription(Long gameDescriptionId, Long userId,
                                                    GameDescriptionDto dto, MultipartFile imageFile) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        if(gameDescription.getUser().getId() != user.getId()){
            throw new GameDescriptionIsExistException();
        }
        
        if (imageFile != null && !imageFile.isEmpty()) {
            if (gameDescription.getImage() != null) {
                ImageModel updatedImage = imageService.replaceImage(gameDescription.getImage().
                                                                      getId(), imageFile);
                gameDescription.setImage(updatedImage);
            } else {
                ImageModel newImage = imageService.uploadImageAndReturnEntity(imageFile);
                gameDescription.setImage(newImage);
            }
        }
        gameDescription.setName(dto.getName());
        gameDescription.setGenre(dto.getGenre());
        gameDescription.setPlatform(dto.getPlatform());
        gameDescription.setAgeLimit(dto.getAgeLimit());
        gameDescription.setPublisher(dto.getPublisher());
        gameDescription.setPublishedAt(dto.getPublishedAt());
        gameDescription.setDescription(dto.getDescription());
        
        GameDescriptionModel updateGameDescription = gameDescriptionRepository.save(gameDescription);
        return mapToDto(updateGameDescription);
        
    }
    
    @Override
    @SneakyThrows
    public void deleteGameDescription(Long gameDescriptionId, Long userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.
                                                 findById(gameDescriptionId).orElseThrow(
                                                   GameDescriptionNotFoundException::new
          );
        if(gameDescription.getUser().getId() != user.getId()){
            throw new GameDescriptionIsExistException();
        }
        if (gameDescription.getImage() != null) {
            imageService.deleteImage(gameDescription.getImage().getId());
        }
        gameDescriptionRepository.delete(gameDescription);
    }
    
    
    private GameDescriptionDto mapToDto(GameDescriptionModel gameDescription) {
        GameDescriptionDto dto = new GameDescriptionDto();
        dto.setId(gameDescription.getId());
        dto.setName(gameDescription.getName());
        dto.setGenre(gameDescription.getGenre());
        dto.setPlatform(gameDescription.getPlatform());
        dto.setAgeLimit(gameDescription.getAgeLimit());
        dto.setPublisher(gameDescription.getPublisher());
        dto.setPublishedAt(gameDescription.getPublishedAt());
        dto.setDescription(gameDescription.getDescription());
        return dto;
    }
    
    private GameDescriptionModel mapToEntity(GameDescriptionDto dto) {
        GameDescriptionModel gameDescription = new GameDescriptionModel();
        gameDescription.setId(dto.getId());
        gameDescription.setName(dto.getName());
        gameDescription.setGenre(dto.getGenre());
        gameDescription.setPlatform(dto.getPlatform());
        gameDescription.setAgeLimit(dto.getAgeLimit());
        gameDescription.setPublisher(dto.getPublisher());
        gameDescription.setPublishedAt(dto.getPublishedAt());
        gameDescription.setDescription(dto.getDescription());
        return gameDescription;
    }
    
}
