package com.ordino.domain.recipes.logs.review.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recipe_review_events")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "logs")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeReviewEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String event;

    @OneToMany(mappedBy = "recipeReviewEvent")
    private List<RecipeReviewLog> logs;
}
