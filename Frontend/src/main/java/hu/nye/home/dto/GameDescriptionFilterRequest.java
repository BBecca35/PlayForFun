package hu.nye.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDescriptionFilterRequest {
    private String genre;
    private String platform;
    private Integer ageLimit;
    private Integer avgRating;
    private Integer minPublishedAt;
    private Integer maxPublishedAt;
}
