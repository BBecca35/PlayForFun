package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("birthdate")
    private LocalDate birthDate;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    //https://github.com/teddysmithdev/pokemon-review-springboot
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameDescriptionModel> gameDescriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> comments = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public UserModel(String email, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }
    
    @Override
    public String toString() {
        return "User{" +
                 "id=" + id +
                 ", email='" + email + '\'' +
                 ", username='" + username + '\'' +
                 '}';
    }
}
