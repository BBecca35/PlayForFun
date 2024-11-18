package hu.nye.home.repository;

import hu.nye.home.entity.GameDescriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDescriptionRepository extends JpaRepository<GameDescriptionModel, Long> {
    
    GameDescriptionModel findGameDescriptionByName(String name);
}
