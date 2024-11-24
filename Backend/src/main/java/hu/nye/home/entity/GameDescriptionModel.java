package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_descriptions")
public class GameDescriptionModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;
    
    @OneToMany(mappedBy = "game_description", cascade = CascadeType.REMOVE)
    private List<CommentModel> comments = new ArrayList<>();
    
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageModel image;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String genre;
    
    @Column(nullable = false)
    private String publisher;
    
    @Column(nullable = false)
    private String platform;
    
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("published_at")
    private LocalDate publishedAt;
    
    @Column(nullable = false)
    private int ageLimit;
    
    @Column(nullable = false)
    private String description;
    
    @Override
    public String toString() {
        return "GameDescriptionModel{" +
                 "name='" + name + '\'' +
                 ", genre='" + genre + '\'' +
                 ", publisher='" + publisher + '\'' +
                 ", platform='" + platform + '\'' +
                 ", publishedAt=" + publishedAt +
                 ", ageLimit=" + ageLimit +
                 ", description='" + description + '\'' +
                 '}';
    }
    
    /*
    public GameDescriptionModel(ImageModel image, UserModel user,
                                String name, String genre, String publisher,
                                String platform, LocalDate publishedAt, int ageLimit,
                                String description) {
        this.image = image;
        this.user = user;
        this.name = name;
        this.genre = genre;
        this.publisher = publisher;
        this.platform = platform;
        this.publishedAt = publishedAt;
        this.ageLimit = ageLimit;
        this.description = description;
    }
     */
}
