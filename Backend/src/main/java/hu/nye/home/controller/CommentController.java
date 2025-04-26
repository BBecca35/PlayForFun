package hu.nye.home.controller;

import hu.nye.home.dto.CommentDto;
import hu.nye.home.service.Interfaces.CommentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/comment-api")
@RestController
public class CommentController {
    
    private final CommentServiceInterface commentService;
    
    @Autowired
    public CommentController(CommentServiceInterface commentService) {
        this.commentService = commentService;
    }
    
    @PostMapping("/user/{userId}/gameDescription/{gameDescriptionId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "userId") Long userId,
                                                    @PathVariable(value = "gameDescriptionId")
                                                    Long gameDescriptionId,
                                                    @RequestBody CommentDto dto) {
        return new ResponseEntity<>(commentService.saveComment(dto, userId, gameDescriptionId),
          HttpStatus.CREATED);
    }
    
    @GetMapping("/user/{userId}/comments")
    public List<CommentDto> getCommentsByUserId(@PathVariable(value = "userId") Long userId) {
        return commentService.getCommentsByByUserId(userId);
    }
    
    @GetMapping("/gameDescription/{gameDescriptionId}/comments")
    public List<CommentDto> getCommentsByGameDescriptionId(@PathVariable(value = "gameDescriptionId")
                                                               Long gameDescriptionId) {
        return commentService.getCommentsByGameDescriptionId(gameDescriptionId);
    }
    
    @GetMapping("/user/{userId}/gameDescription/{gameDescriptionId}/comments/{id}")
    public ResponseEntity<CommentDto> getReviewById(@PathVariable(value = "userId")
                                                        Long userId,
                                                    @PathVariable(value = "gameDescriptionId")
                                                    Long gameDescriptionId,
                                                    @PathVariable(value = "id")
                                                        Long id
                                                    ) {
        CommentDto dto = commentService.getCommentById(userId, gameDescriptionId, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PutMapping("/user/{userId}/gameDescription/{gameDescriptionId}/comments/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'moderator:update')")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "userId") Long userId,
                                                  @PathVariable(value = "gameDescriptionId")
                                                  Long gameDescriptionId,
                                                  @PathVariable(value = "id")
                                                      Long id,
                                                  @RequestBody CommentDto dto) {
        CommentDto updatedComment = commentService.updateComment(id, dto, gameDescriptionId, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
    
    @DeleteMapping("/user/{userId}/gameDescription/{gameDescriptionId}/comments/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'moderator:delete')")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "userId")
                                                   Long userId,
                                               @PathVariable(value = "gameDescriptionId")
                                                   Long gameDescriptionId,
                                               @PathVariable(value = "id")
                                                   Long id) {
        commentService.deleteComment(gameDescriptionId, userId, id);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }
    
    @DeleteMapping("/gameDescription/{gameDescriptionId}/comments")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'moderator:delete')")
    public ResponseEntity<String> deleteAllComment(@PathVariable(value = "gameDescriptionId")
                                                    Long gameDescriptionId ) {
        commentService.deleteAllCommentUnderADesc(gameDescriptionId);
        return new ResponseEntity<>("All Review deleted successfully", HttpStatus.OK);
    }
}
