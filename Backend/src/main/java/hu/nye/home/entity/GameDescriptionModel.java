package hu.nye.home.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameDescriptionModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageModel image;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "genre", nullable = false)
    private String genre;
    
    @Column(name = "publisher", nullable = false)
    private String publisher;
    
    @Column(name = "platform", nullable = false)
    private String platform;
    
    @Column(name = "published_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("published_at")
    private LocalDate publishedAt;
    
    @Column(name = "agelimit", nullable = false)
    private int ageLimit;
    
    @Column(name = "description", nullable = false)
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDescriptionModel that = (GameDescriptionModel) o;
        return ageLimit == that.ageLimit && Objects.equals(id, that.id) &&
                 Objects.equals(image, that.image) && Objects.equals(name, that.name) &&
                 Objects.equals(genre, that.genre) && Objects.equals(publisher, that.publisher) &&
                 Objects.equals(platform, that.platform) && Objects.equals(publishedAt, that.publishedAt) &&
                 Objects.equals(description, that.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, image, name, genre, publisher, platform, publishedAt, ageLimit, description);
    }
}
