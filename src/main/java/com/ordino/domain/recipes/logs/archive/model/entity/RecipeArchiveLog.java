package com.ordino.domain.recipes.logs.archive.model.entity;

import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "recipe_archive_logs")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "recipe", "recipeArchiveEvent"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeArchiveLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "recipe_archive_event_id", nullable = false)
    private RecipeArchiveEvent recipeArchiveEvent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;
}
