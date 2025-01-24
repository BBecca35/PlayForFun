package hu.nye.home.repository;

import hu.nye.home.entity.GameDescriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface GameDescriptionRepository extends JpaRepository<GameDescriptionModel, Long> {
    
    List<GameDescriptionModel> findByUserId(Long userId);
    
    List<GameDescriptionModel> findByGenre(String genre);
    List<GameDescriptionModel> findByPlatform(String platform);
    List<GameDescriptionModel> findByAgeLimit(int ageLimit);
    List<GameDescriptionModel> findByPublisher(String publisher);
    
    List<GameDescriptionModel> findByPublishedAtBetween(int min, int max);
    GameDescriptionModel findByName(String name);
    
}
