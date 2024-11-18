package hu.nye.home.service.Classes;

import hu.nye.home.dto.CommentDto;
import hu.nye.home.entity.CommentModel;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.CommentNotFoundException;
import hu.nye.home.repository.CommentRepository;
import hu.nye.home.service.Interfaces.CommentServiceInterface;
import jakarta.validation.constraints.Null;
import lombok.SneakyThrows;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentService implements CommentServiceInterface {
    
    private final CommentRepository commentRepository;
    
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    @SneakyThrows
    public CommentModel getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }
    
    @Override
    public CommentModel saveComment(CommentDto dto) {
        CommentModel comment = new CommentModel();
        if(dto == null){
            throw new NullPointerException("CommentDto cannot be null");
        }else {
            if(dto.getUser() != null){
                UserModel user = new UserModel();
                user.setId(dto.getUser().getId());
                user.setUsername(dto.getUser().getUsername());
                user.setEmail(dto.getUser().getEmail());
                user.setPassword(dto.getUser().getPassword());
                user.setBirthDate(dto.getUser().getBirthDate());
                user.setCreatedAt(dto.getUser().getCreatedAt());
                comment.setUser(user);
            }
            if(dto.getGameDescription() != null){
                GameDescriptionModel gameDescription = new GameDescriptionModel();
                gameDescription.setId(dto.getGameDescription().getId());
                gameDescription.setUser(dto.getGameDescription().getUser());
                gameDescription.setImage(dto.getGameDescription().getImage());
                gameDescription.setName(dto.getGameDescription().getName());
                gameDescription.setGenre(dto.getGameDescription().getGenre());
                gameDescription.setPlatform(dto.getGameDescription().getPlatform());
                gameDescription.setPublisher(dto.getGameDescription().getPublisher());
                gameDescription.setPublishedAt(dto.getGameDescription().getPublishedAt());
                gameDescription.setAgeLimit(dto.getGameDescription().getAgeLimit());
                gameDescription.setDescription(dto.getGameDescription().getDescription());
                comment.setGameDescription(gameDescription);
            }
            comment.setComment(dto.getComment());
            comment.setRating(dto.getRating());
            commentRepository.save(comment);
            return comment;
        }
    }
    
    @Override
    @SneakyThrows
    public CommentModel updateComment(Long id, CommentDto dto) {
        CommentModel comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.setUser(dto.getUser());
        comment.setGameDescription(dto.getGameDescription());
        comment.setComment(dto.getComment());
        comment.setRating(dto.getRating());
        return commentRepository.save(comment);
    }
    
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
