package hu.nye.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.nye.home.entity.GameDescriptionModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    
    private Long id;
    
    @NotEmpty
    @NotNull
    private String email;
    
    @NotEmpty
    @NotNull
    private String username;
    
    @NotEmpty
    @NotNull
    private String password;
    
    @NotEmpty
    @NotNull
    private LocalDate birthDate;
    
    private LocalDateTime createdAt;
    private List<GameDescriptionModel> gameDescriptions;
    
}
