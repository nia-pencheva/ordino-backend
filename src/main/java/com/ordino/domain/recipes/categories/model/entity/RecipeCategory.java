package com.ordino.domain.recipes.categories.model.entity;

import com.ordino.domain.recipes.model.entity.Recipe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "recipeCategories")
    private List<Recipe> recipes;
}
