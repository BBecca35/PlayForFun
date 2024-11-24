package hu.nye.home.service.Classes;

import hu.nye.home.dto.CommentDto;
import hu.nye.home.entity.CommentModel;
import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.UserModel;
import hu.nye.home.exception.CommentNotFoundException;
import hu.nye.home.exception.GameDescriptionNotFoundException;
import hu.nye.home.exception.UserNotFoundException;
import hu.nye.home.repository.CommentRepository;
import hu.nye.home.repository.GameDescriptionRepository;
import hu.nye.home.repository.UserRepository;
import hu.nye.home.service.Interfaces.CommentServiceInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements CommentServiceInterface {
    
    private final CommentRepository commentRepository;
    private final GameDescriptionRepository gameDescriptionRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          GameDescriptionRepository gameDescriptionRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.gameDescriptionRepository = gameDescriptionRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public List<CommentDto> getCommentsByByUserId(Long id) {
        List<CommentModel> comments = commentRepository.findByUserId(id);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<CommentDto> getCommentsByGameDescriptionId(Long id) {
        List<CommentModel> comments = commentRepository.findByGameDescriptionId(id);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    @Override
    @SneakyThrows
    public CommentDto getCommentById(Long userId, Long gameDescriptionId, Long commentId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        CommentModel comment = commentRepository.findById(commentId).
                                 orElseThrow(CommentNotFoundException::new);
        
        if((comment.getUser().getId() != user.getId()) ||
            (comment.getGameDescription().getId() != gameDescription.getId())
        ){
            throw new CommentNotFoundException();
        }
        return mapToDto(comment);
    }
    
    @Override
    @SneakyThrows
    public CommentDto saveComment(CommentDto dto, Long userId, Long gameDescriptionId) {
        CommentModel comment = mapToEntity(dto);
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        comment.setUser(user);
        comment.setGameDescription(gameDescription);
        CommentModel newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }
    
    @Override
    @SneakyThrows
    public CommentDto updateComment(Long commentId, CommentDto dto, Long gameDescriptionId, Long userId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        CommentModel comment = commentRepository.findById(commentId).
                                 orElseThrow(CommentNotFoundException::new);
        
        if((comment.getUser().getId() != user.getId()) ||
             (comment.getGameDescription().getId() != gameDescription.getId())
        ){
            throw new CommentNotFoundException();
        }
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
        CommentModel updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }
    
    @Override
    @SneakyThrows
    public void deleteComment(Long gameDescriptionId, Long userId, Long commentId) {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GameDescriptionModel gameDescription = gameDescriptionRepository.findById(gameDescriptionId).
                                                 orElseThrow(GameDescriptionNotFoundException::new);
        CommentModel comment = commentRepository.findById(commentId).
                                 orElseThrow(CommentNotFoundException::new);
        
        if((comment.getUser().getId() != user.getId()) ||
             (comment.getGameDescription().getId() != gameDescription.getId())
        ){
            throw new CommentNotFoundException();
        }
        commentRepository.delete(comment);
    }
    
    private CommentDto mapToDto(CommentModel comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setRating(comment.getRating());
        return dto;
    }
    
    private CommentModel mapToEntity(CommentDto dto) {
        CommentModel comment = new CommentModel();
        comment.setId(dto.getId());
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
        return comment;
    }
    
}
