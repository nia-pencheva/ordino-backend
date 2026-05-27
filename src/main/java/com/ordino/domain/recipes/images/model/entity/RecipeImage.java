package com.ordino.domain.recipes.images.model.entity;

import com.ordino.domain.recipes.model.entity.Recipe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "recipes_images")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "recipe")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Column(nullable = false)
    private Integer position;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "deleted_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant deletedAt;
}
