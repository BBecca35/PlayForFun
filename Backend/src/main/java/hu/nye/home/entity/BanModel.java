package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "bans")
public class BanModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserModel user;
    
    private String reason;
    
    @Column(name="expired")
    private boolean isExpired;
    
    private int banExpiration;
    
    @Column(updatable = false)
    private LocalDateTime bannedAt;
    
    @PrePersist
    protected void onCreate() {
        bannedAt = LocalDateTime.now();
    }
}
