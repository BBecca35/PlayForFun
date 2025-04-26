package hu.nye.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanDetails {
    
    private Long userId;
    private String reason;
    private int expirationTime;
}
