package hu.nye.home.entity;

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
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_descriptions_id")
    private GameDescriptionModel gameDescription;
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;
    
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "rating")
    private double rating;
    
}
