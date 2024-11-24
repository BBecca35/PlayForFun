package hu.nye.home.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    
    private Long id;
    
    @NotNull
    @NotEmpty
    private String message;
    
    @NotNull
    @NotEmpty
    @Positive
    private double rating;
    
}
