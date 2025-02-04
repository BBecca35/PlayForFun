package hu.nye.home.service.Classes;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.dto.GameDescriptionFilterRequest;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.GameDescriptionIsExistException;
import hu.nye.home.exception.GameDescriptionNotFoundException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.CommentRepository;
import hu.nye.home.repository.GameDescriptionRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import hu.nye.home.specifications.GameDescriptionSpecifications;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

//https://github.com/teddysmithdev/pokemon-review-springboot/blob/master/src/main/java/com
//pokemonreview/api/service/impl/ReviewServiceImpl.java

@Service
public class GameDescriptionService implements GameDescriptionServiceInterface {
    
    private static final String IMAGES_FOLDER = "C:/apache-tomcat-9.0.39/webapps/ROOT/images/";
    private final GameDescriptionRepository gameDescriptionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FileService imageService;
    
    @Autowired
    public GameDescriptionService(GameDescriptionRepository gameDescriptionRepository,
                                  UserRepository userRepository, CommentRepository commentRepository, FileService imageService) {
        this.gameDescriptionRepository = gameDescriptionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.imageService = imageService;
    }
    
    
    @Override
    public List<GameDescriptionDto> getGameDescriptionsByUserId(Long id) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findByUserId(id);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionDto getGameDescriptionById(Long gameDescriptionId) {
        GameDescriptionModel gameDescription = gameDescriptionRepository.
                                                 findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
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
    public GameDescriptionDto updateAvgRating(Long gameDescriptionId, int avgRating) {
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        gameDescription.setAvgRating(avgRating);
        GameDescriptionModel updatedGameDescription = gameDescriptionRepository.save(gameDescription);
        return mapToDto(updatedGameDescription);
    }
    
    @Override
    @SneakyThrows
    public void deleteGameDescription(Long gameDescriptionId) {
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId)
                                                 .orElseThrow(GameDescriptionNotFoundException::new);
        
        if (gameDescription.getImagePath() != null) {
            imageService.deleteFileFromFileSystem(gameDescription.getImagePath());
        }
        
        gameDescriptionRepository.delete(gameDescription);
    }
    
    @Override
    public List<GameDescriptionDto> getAllByFilters(GameDescriptionFilterRequest filters) {
        Specification<GameDescriptionModel> spec = GameDescriptionSpecifications.withFilters(filters);
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.findAll(spec);
        return gameDescriptions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllGameDescriptionsSortedByNameAsc() {
        return gameDescriptionRepository.findAllByOrderByNameAsc()
                 .stream()
                 .map(this::mapToDto)
                 .collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllGameDescriptionsSortedByNameDesc() {
        return gameDescriptionRepository.findAllByOrderByNameDesc()
                 .stream()
                 .map(this::mapToDto)
                 .collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllGameDescriptionsSortedByUserNameAsc() {
        return gameDescriptionRepository.findAllByOrderByUserUsernameAsc()
                 .stream()
                 .map(this::mapToDto)
                 .collect(Collectors.toList());
    }
    
    @Override
    public List<GameDescriptionDto> getAllGameDescriptionsSortedByUserNameDesc() {
        return gameDescriptionRepository.findAllByOrderByUserUsernameDesc()
                 .stream()
                 .map(this::mapToDto)
                 .collect(Collectors.toList());
    }
    
    @Override
    @SneakyThrows
    public List<GameDescriptionDto> searchByName(String name) {
        List<GameDescriptionModel> gameDescriptions = gameDescriptionRepository.searchByName(name);
        
        if (gameDescriptions.isEmpty()) {
            throw new GameDescriptionNotFoundException();
        }
        
        return gameDescriptions.stream()
                 .map(this::mapToDto)
                 .collect(Collectors.toList());
    }
    
    @Override
    public void updateGameDescriptionRating(Long gameDescriptionId) {
        List<Integer> ratings = commentRepository.findRatingsByGameDescriptionId(gameDescriptionId);
        List<Integer> validRatings = ratings.stream()
                                       .filter(rating -> rating != null && rating > 0)
                                       .collect(Collectors.toList());
        double averageRating = validRatings.isEmpty() ? 0 :
                                 validRatings.stream().mapToInt(Integer::intValue).average().orElse(0);
        int roundedRating = (int) Math.round(averageRating);
        updateAvgRating(gameDescriptionId, roundedRating);
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
        dto.setAvgRating(gameDescription.getAvgRating());
        dto.setCreatedAt(gameDescription.getCreatedAt());
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
