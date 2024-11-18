package hu.nye.home.dto;

import hu.nye.home.entity.GameDescriptionModel;
import hu.nye.home.entity.UserModel;
import jakarta.persistence.*;
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
    
    private GameDescriptionModel gameDescription;
    
    private UserModel user;
    
    @NotNull
    @NotEmpty
    private String comment;
    
    @NotNull
    @NotEmpty
    @Positive
    private double rating;
    
    public CommentDto(GameDescriptionModel gameDescription, UserModel user, String comment, double rating) {
        this.gameDescription = gameDescription;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }
}
