package hu.nye.home.repository;

import hu.nye.home.entity.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByGameDescriptionId(Long gameDescriptionId);
    List<CommentModel> findByUserId(Long userId);
    List<CommentModel> findAllByGameDescription_Id(Long gameDescriptionId);
    
    @Query("SELECT comment.rating FROM CommentModel comment WHERE comment.gameDescription.id = :gameDescriptionId")
    List<Integer> findRatingsByGameDescriptionId(@Param("gameDescriptionId") Long gameDescriptionId);
}
