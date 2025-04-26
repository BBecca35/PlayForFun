package hu.nye.home.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDescriptionDto {
    
    private Long id;
    
    private Long userId;
    
    @NotEmpty
    @NotNull
    private String name;
    
    @NotEmpty
    @NotNull
    private String genre;
    
    @NotEmpty
    @NotNull
    private String publisher;
    
    @NotEmpty
    @NotNull
    private String platform;
    
    @NotNull
    @Positive
    private int publishedAt;
    
    @NotNull
    @Positive
    private int ageLimit;
    
    @NotEmpty
    @NotNull
    private String description;
    
    @NotEmpty
    @NotNull
    private String imageName;
    
    @NotEmpty
    @NotNull
    private String imagePath;
    
    @NotEmpty
    @NotNull
    private String imageType;
    
    private int avgRating;
    
    private LocalDateTime createdAt;
    
}
