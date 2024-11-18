package hu.nye.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
    
    public UserDto(String email, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }
}
