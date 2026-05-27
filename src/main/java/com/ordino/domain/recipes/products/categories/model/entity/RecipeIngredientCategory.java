package com.ordino.domain.recipes.products.categories.model.entity;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.units.model.entity.Unit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "recipe_ingredient_categories")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"parentCategory", "childCategories", "allowedUnits", "products"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeIngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, length = 100)
    private String category;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "parent_category_id", nullable = true)
    private RecipeIngredientCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<RecipeIngredientCategory> childCategories;

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredient_categories_allowed_units",
        joinColumns = @JoinColumn(name = "recipes_ingredient_category_id"),
        inverseJoinColumns = @JoinColumn(name = "unit_id")
    )
    private List<Unit> allowedUnits;

    @ManyToMany
    @JoinTable(
        name = "products_recipe_ingredient_categories",
        joinColumns = @JoinColumn(name = "recipe_ingredient_category_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}
