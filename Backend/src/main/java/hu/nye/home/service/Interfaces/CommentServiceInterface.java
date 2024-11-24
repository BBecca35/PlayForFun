package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.CommentDto;

import java.util.List;

public interface CommentServiceInterface {
    
    List<CommentDto> getCommentsByByUserId(Long id);
    List<CommentDto> getCommentsByGameDescriptionId(Long id);
    CommentDto getCommentById(Long userId, Long gameDescriptionId, Long commentId);
    CommentDto saveComment(CommentDto dto, Long userId, Long gameDescriptionId);
    CommentDto updateComment(Long commentId, CommentDto dto, Long gameDescriptionId, Long userId);
    void deleteComment(Long gameDescriptionId, Long userId, Long commentId);
    
    
}
