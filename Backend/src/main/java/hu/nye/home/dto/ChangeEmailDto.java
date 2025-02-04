package hu.nye.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeEmailDto {
    
    private Long id;
    private String currentEmail;
    private String newEmail;
}
