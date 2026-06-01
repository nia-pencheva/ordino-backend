package com.ordino.domain.recipes.logs.edits.model.entity;

import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "recipe_edit_logs")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "recipe"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeEditLog {

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

    @Column(name = "old_data", nullable = true, columnDefinition = "JSON")
    private String oldData;

    @Column(name = "new_data", nullable = true, columnDefinition = "JSON")
    private String newData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;
}
