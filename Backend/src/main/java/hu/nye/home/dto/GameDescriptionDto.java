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
    
}
