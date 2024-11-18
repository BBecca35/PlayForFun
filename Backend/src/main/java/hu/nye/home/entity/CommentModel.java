package hu.nye.home.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    
    //https://www.quora.com/What-is-the-difference-between-identity-and-auto_increment-in-MySQL
    //https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //https://www.baeldung.com/jpa-one-to-one
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_description_id", referencedColumnName = "id")
    private GameDescriptionModel gameDescription;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;
    
    @Column(name = "comment")
    private String comment;
    
    @Column(name = "rating")
    private double rating;
    
    @Override
    public String toString() {
        return "CommentModel{" +
                 "user=" + user.getUsername() +
                 ", comment='" + comment + '\'' +
                 ", rating=" + rating +
                 '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentModel that = (CommentModel) o;
        return Objects.equals(gameDescription.getId(), that.gameDescription.getId()) && Objects.equals(user.getId(), that.user.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(gameDescription, user);
    }
}
