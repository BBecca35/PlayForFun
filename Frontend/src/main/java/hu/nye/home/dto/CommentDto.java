package hu.nye.home.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    
    private Long id;
    
    private Long userId;
    
    private Long gameDescriptionId;
    
    @NotNull
    @NotEmpty
    private String message;
    
    private int rating;
    
    private LocalDateTime createdAt;
    
}
