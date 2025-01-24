package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

//Roles: user, admin, moderator
//moderator: commentek törlése, felhasználók figyelmeztetése
//admin: felhasználók ideiglenes vagy örökös kitíltása a weboldalról,
//amit majd a szabályszegés súlyossága fog eldönteni

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameDescriptionModel> gameDescriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> comments = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleModel> roles = new ArrayList<>();
    
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
    
    public UserModel(String username, String password, List<RoleModel> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
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
