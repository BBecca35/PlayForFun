package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentModel {
    
    //https://www.quora.com/What-is-the-difference-between-identity-and-auto_increment-in-MySQL
    //https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //https://www.baeldung.com/jpa-one-to-one
    //https://stackoverflow.com/questions/70621164/saving-in-one-shot-two-parents-and-one-child-spring-jpa
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_description_id", nullable = false)
    @JsonIgnore
    private GameDescriptionModel gameDescription;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserModel user;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "rating")
    private double rating;
    
}
