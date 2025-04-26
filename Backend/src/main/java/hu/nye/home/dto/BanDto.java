package hu.nye.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BanDto {
    
    private Long id;
    private Long userId;
    private String reason;
    private boolean isExpired;
    private int banExpiration;
    private LocalDateTime bannedAt;
    
}
