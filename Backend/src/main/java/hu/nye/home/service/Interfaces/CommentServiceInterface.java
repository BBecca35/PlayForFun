package hu.nye.home.service.Interfaces;

import hu.nye.home.dto.CommentDto;
import hu.nye.home.entity.CommentModel;

public interface CommentServiceInterface {
    
    CommentModel getCommentById(Long id);
    CommentModel saveComment(CommentDto dto);
    CommentModel updateComment(Long id, CommentDto dto);
    void deleteComment(Long id);
}
