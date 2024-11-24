package hu.nye.home.repository;

import hu.nye.home.entity.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByGameDescriptionId(Long gameDescriptionId);
    List<CommentModel> findByUserId(Long userId);
}
