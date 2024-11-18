package hu.nye.home.service.Classes;

import hu.nye.home.dto.GameDescriptionDto;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.ImageModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.GameDescriptionIsExistException;
import hu.nye.home.exception.GameDescriptionNotFoundException;
import hu.nye.home.repository.GameDescriptionRepository;
import hu.nye.home.service.Interfaces.GameDescriptionServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameDescriptionService implements GameDescriptionServiceInterface {
    
    private final GameDescriptionRepository gameDescriptionRepository;
    
    @Autowired
    public GameDescriptionService(GameDescriptionRepository gameDescriptionRepository) {
        this.gameDescriptionRepository = gameDescriptionRepository;
    }
    
    
    @Override
    @SneakyThrows
    public GameDescriptionModel getGameDescriptionById(Long id) {
        return gameDescriptionRepository.findById(id).orElseThrow(GameDescriptionNotFoundException::new);
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionModel saveGameDescription(GameDescriptionDto dto) {
        GameDescriptionModel gameDescription = new GameDescriptionModel();
        if(dto == null){
            throw new NullPointerException("GameDescriptionDto cannot be null");
        }else {
            GameDescriptionModel findGameDescriptionModel =
              gameDescriptionRepository.findGameDescriptionByName(dto.getName());
            if(findGameDescriptionModel != null){
                throw new GameDescriptionIsExistException();
            }else{
                if(dto.getUser() != null){
                    UserModel user = new UserModel();
                    user.setId(dto.getUser().getId());
                    user.setEmail(dto.getUser().getEmail());
                    user.setUsername(dto.getUser().getUsername());
                    user.setPassword(dto.getUser().getPassword());
                    user.setCreatedAt(dto.getUser().getCreatedAt());
                    user.setBirthDate(dto.getUser().getBirthDate());
                    gameDescription.setUser(user);
                }
                if (dto.getImage() != null){
                    ImageModel image = new ImageModel();
                    image.setId(dto.getImage().getId());
                    image.setName(dto.getImage().getName());
                    image.setType(dto.getImage().getType());
                    image.setPath(dto.getImage().getPath());
                    gameDescription.setImage(image);
                }
                gameDescription.setName(dto.getName());
                gameDescription.setGenre(dto.getGenre());
                gameDescription.setPlatform(dto.getPlatform());
                gameDescription.setPublisher(dto.getPublisher());
                gameDescription.setPublishedAt(dto.getPublishedAt());
                gameDescription.setAgeLimit(dto.getAgeLimit());
                gameDescription.setDescription(dto.getDescription());
                gameDescriptionRepository.save(gameDescription);
                return gameDescription;
            }
            
        }
    }
    
    @Override
    @SneakyThrows
    public GameDescriptionModel updateGameDescription(Long id, GameDescriptionDto dto) {
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(id).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        gameDescription.setUser(dto.getUser());
        gameDescription.setImage(dto.getImage());
        gameDescription.setName(dto.getName());
        gameDescription.setGenre(dto.getGenre());
        gameDescription.setPublisher(dto.getPublisher());
        gameDescription.setPlatform(dto.getPlatform());
        gameDescription.setPublishedAt(dto.getPublishedAt());
        gameDescription.setAgeLimit(dto.getAgeLimit());
        gameDescription.setDescription(dto.getDescription());
        return gameDescriptionRepository.save(gameDescription);
        
    }
    
    @Override
    public void deleteGameDescription(Long id) {
        gameDescriptionRepository.deleteById(id);
    }
}
