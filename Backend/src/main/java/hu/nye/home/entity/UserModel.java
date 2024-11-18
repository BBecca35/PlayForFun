package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

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
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("birthdate")
    private LocalDate birthDate;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(email, userModel.email) && Objects.equals(username, userModel.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }
}
