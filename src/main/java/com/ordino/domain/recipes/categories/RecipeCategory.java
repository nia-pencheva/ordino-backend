package com.ordino.domain.recipes.categories;

import com.ordino.domain.recipes.Recipe;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "recipe_categories")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "recipes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String category;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "recipeCategories")
    private List<Recipe> recipes;
}
