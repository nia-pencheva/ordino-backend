package com.ordino.domain.recipes.logs.archive.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recipe_archive_events")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "logs")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeArchiveEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String event;

    @OneToMany(mappedBy = "recipeArchiveEvent")
    private List<RecipeArchiveLog> logs;
}
