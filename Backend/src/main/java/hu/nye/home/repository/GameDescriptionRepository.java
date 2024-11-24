package hu.nye.home.repository;

import hu.nye.home.entity.GameDescriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameDescriptionRepository extends JpaRepository<GameDescriptionModel, Long> {
    
    List<GameDescriptionModel> findByUserId(Long userId);
    
}
