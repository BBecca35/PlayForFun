package hu.nye.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.nye.home.entity.ImageModel;
import hu.nye.home.entity.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDescriptionDto {

    
    private Long id;
    
    @NotEmpty
    @NotNull
    private ImageModel image;
    
    @NotEmpty
    @NotNull
    private UserModel user;
    
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
    
    @NotEmpty
    @NotNull
    private LocalDate publishedAt;
    
    @NotEmpty
    @NotNull
    @Positive
    private int ageLimit;
    
    @NotEmpty
    @NotNull
    private String description;
    
    public GameDescriptionDto(ImageModel image, UserModel user, String name, String genre,
                              String publisher, String platform, LocalDate publishedAt, int ageLimit,
                              String description) {
        this.image = image;
        this.user = user;
        this.name = name;
        this.genre = genre;
        this.publisher = publisher;
        this.platform = platform;
        this.publishedAt = publishedAt;
        this.ageLimit = ageLimit;
        this.description = description;
    }
}
