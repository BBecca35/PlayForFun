package hu.nye.home.repository;

import hu.nye.home.entity.GameDescriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GameDescriptionRepository extends JpaRepository<GameDescriptionModel, Long>,
                                                     JpaSpecificationExecutor<GameDescriptionModel> {
    
    List<GameDescriptionModel> findByUserId(Long userId);
    
    List<GameDescriptionModel> findByGenre(String genre);
    List<GameDescriptionModel> findByPlatform(String platform);
    List<GameDescriptionModel> findByAgeLimit(int ageLimit);
    List<GameDescriptionModel> findByPublisher(String publisher);
    
    List<GameDescriptionModel> findByPublishedAtBetween(int min, int max);
    
    @Query("SELECT g FROM GameDescriptionModel g WHERE g.name ILIKE %:name%")
    List<GameDescriptionModel> searchByName(@Param("name") String name);
    
    GameDescriptionModel findByName(String name);
    
    List<GameDescriptionModel> findAllByOrderByNameAsc();
    List<GameDescriptionModel> findAllByOrderByNameDesc();
    List<GameDescriptionModel> findAllByOrderByUserUsernameAsc();
    List<GameDescriptionModel> findAllByOrderByUserUsernameDesc();
    
}
