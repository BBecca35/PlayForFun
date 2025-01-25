package hu.nye.home.service.Classes;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.GameDescriptionIsExistException;
import hu.nye.home.exception.GameDescriptionNotFoundException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.GameDescriptionRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//https://github.com/teddysmithdev/pokemon-review-springboot/blob/master/src/main/java/com
//pokemonreview/api/service/impl/ReviewServiceImpl.java

@Service
public class GameDescriptionService implements GameDescriptionServiceInterface {
    
    private static final String IMAGES_FOLDER = "C:/apache-tomcat-9.0.39/webapps/ROOT/images/";
    private final GameDescriptionRepository gameDescriptionRepository;
    private final UserRepository userRepository;
    private final FileService imageService;
    
    @Autowired
    public GameDescriptionService(GameDescriptionRepository gameDescriptionRepository,
                                  UserRepository userRepository, FileService imageService) {
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
    public List<GameDescriptionDto> getAllGameDescription() {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findAll();
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionDto saveGameDescription(Long userId, GameDescriptionDto dto, MultipartFile imageFile) {
        GameDescriptionModel gameDescription = mapToEntity(dto);
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        gameDescription.setUser(user);
        
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageService.saveFileToFileSystem(imageFile);
            gameDescription.setImageName(imageName);
            gameDescription.setImagePath(IMAGES_FOLDER + imageName);
            gameDescription.setImageType(imageFile.getContentType());
        }
        
        if(gameDescriptionRepository.findByName(dto.getName()) != null){
            throw new GameDescriptionIsExistException();
        }
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
            if (gameDescription.getImagePath() != null) {
                imageService.deleteFileFromFileSystem(gameDescription.getImagePath());
            }
            
            String imageName = imageService.saveFileToFileSystem(imageFile);
            gameDescription.setImageName(imageName);
            gameDescription.setImagePath(IMAGES_FOLDER + imageName);
            gameDescription.setImageType(imageFile.getContentType());
        }
        
        gameDescription.setName(dto.getName());
        gameDescription.setGenre(dto.getGenre());
        gameDescription.setPlatform(dto.getPlatform());
        gameDescription.setAgeLimit(dto.getAgeLimit());
        gameDescription.setPublisher(dto.getPublisher());
        gameDescription.setPublishedAt(dto.getPublishedAt());
        gameDescription.setDescription(dto.getDescription());
        
        GameDescriptionModel updatedGameDescription = gameDescriptionRepository.save(gameDescription);
        return mapToDto(updatedGameDescription);
        
    }
    
    @Override
    @SneakyThrows
    public void deleteGameDescription(Long gameDescriptionId, Long userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId)
                                                 .orElseThrow(GameDescriptionNotFoundException::new);
        
        if (gameDescription.getImagePath() != null) {
            imageService.deleteFileFromFileSystem(gameDescription.getImagePath());
        }
        
        gameDescriptionRepository.delete(gameDescription);
    }
    
    @Override
    public List<GameDescriptionDto> getAllByGenre(String genre) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByGenre(genre);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllByPlatform(String platform) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByPlatform(platform);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllByAgeLimit(int ageLimit) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByAgeLimit(ageLimit);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllByPublisher(String publisher) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByPublisher(publisher);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllByPublishedAtBetween(int min, int max) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByPublishedAtBetween(min, max);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public GameDescriptionDto getByName(String name) {
        GameDescriptionModel gameDescription = gameDescriptionRepository.findByName(name);
        return mapToDto(gameDescription);
    }
    
    
    private GameDescriptionDto mapToDto(GameDescriptionModel gameDescription) {
        GameDescriptionDto dto = new GameDescriptionDto();
        dto.setId(gameDescription.getId());
        dto.setUserId(gameDescription.getUser().getId());
        dto.setName(gameDescription.getName());
        dto.setGenre(gameDescription.getGenre());
        dto.setPlatform(gameDescription.getPlatform());
        dto.setAgeLimit(gameDescription.getAgeLimit());
        dto.setPublisher(gameDescription.getPublisher());
        dto.setPublishedAt(gameDescription.getPublishedAt());
        dto.setDescription(gameDescription.getDescription());
        dto.setImageName(gameDescription.getImageName());
        dto.setImagePath(gameDescription.getImagePath());
        dto.setImageType(gameDescription.getImageType());
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
        gameDescription.setImageName(dto.getImageName());
        gameDescription.setImagePath(dto.getImagePath());
        gameDescription.setImageType(dto.getImageType());
        return gameDescription;
    }
    
}
