package hu.nye.home.specifications;

import hu.nye.home.dto.GameDescriptionFilterRequest;
import hu.nye.home.entity.GameDescriptionModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GameDescriptionSpecifications {
    public static Specification<GameDescriptionModel> withFilters(GameDescriptionFilterRequest filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filters.getGenre() != null) {
                String genre = filters.getGenre().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(
                  criteriaBuilder.lower(root.get("genre")),
                  genre
                ));
            }
            if (filters.getPlatform() != null) {
                String platform = filters.getPlatform().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(
                  criteriaBuilder.lower(root.get("platform")),
                  platform
                ));
            }
            if (filters.getAgeLimit() != null) {
                predicates.add(criteriaBuilder.equal(root.get("ageLimit"), filters.getAgeLimit()));
            }
            if (filters.getAvgRating() != null) {
                predicates.add(criteriaBuilder.equal(root.get("avgRating"), filters.getAvgRating()));
            }
            if (filters.getMinPublishedAt() != null && filters.getMaxPublishedAt() != null) {
                predicates.add(criteriaBuilder.between(root.get("publishedAt"), filters.getMinPublishedAt(), filters.getMaxPublishedAt()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
