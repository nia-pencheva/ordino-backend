package com.ordino.domain.recipes;

import com.ordino.domain.menus.MenuSectionRecipe;
import com.ordino.domain.recipes.categories.RecipeCategory;
import com.ordino.domain.recipes.images.RecipeImage;
import com.ordino.domain.recipes.logs.archive.RecipeArchiveLog;
import com.ordino.domain.recipes.logs.edits.RecipeEditLog;
import com.ordino.domain.recipes.logs.review.RecipeReviewLog;
import com.ordino.domain.recipes.products.RecipeProduct;
import com.ordino.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"recipeStatus", "createdBy", "recipeCategories", "images", "reviewLogs",
        "editLogs", "archiveLogs", "recipeProducts", "menuSectionRecipes"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = true, length = 250)
    private String title;

    @Column(name = "preparation_time", nullable = true, columnDefinition = "SMALLINT UNSIGNED")
    private Integer preparationTime;

    @Column(nullable = true, columnDefinition = "TINYINT UNSIGNED")
    private Integer servings;

    @Column(nullable = true, columnDefinition = "JSON")
    private String instructions;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(nullable = true)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "recipe_status_id", nullable = false)
    private RecipeStatus recipeStatus;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToMany
    @JoinTable(
        name = "recipes_recipe_categories",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_category_id")
    )
    private List<RecipeCategory> recipeCategories;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeImage> images;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeReviewLog> reviewLogs;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeEditLog> editLogs;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeArchiveLog> archiveLogs;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeProduct> recipeProducts;

    @OneToMany(mappedBy = "recipe")
    private List<MenuSectionRecipe> menuSectionRecipes;
}
