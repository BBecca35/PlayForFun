package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private UserModel user;
    
    @OneToMany(mappedBy = "gameDescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> comments = new ArrayList<>();
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String genre;
    
    @Column(nullable = false)
    private String publisher;
    
    @Column(nullable = false)
    private String platform;
    
    @Column(nullable = false)
    @JsonProperty("published_at")
    private int publishedAt;
    
    @Column(nullable = false)
    private int ageLimit;
    
    @Column(nullable = false, length = 3000)
    private String description;
    
    private String imagePath;
    private String imageName;
    private String imageType;
    
}
