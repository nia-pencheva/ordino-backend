package com.ordino.domain.recipes.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recipe_statuses")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "recipes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String status;

    @OneToMany(mappedBy = "recipeStatus")
    private List<Recipe> recipes;
}
