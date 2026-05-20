package com.ordino.domain.products;

import com.ordino.domain.recipes.products.RecipeProduct;
import com.ordino.domain.recipes.products.categories.RecipeIngredientCategory;
import com.ordino.domain.warehouse.products.WarehouseProduct;
import com.ordino.domain.warehouse.products.categories.WarehouseProductCategory;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"recipeProducts", "warehouseProduct", "recipeIngredientCategories", "warehouseProductCategories"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "product")
    private List<RecipeProduct> recipeProducts;

    @OneToOne(mappedBy = "product")
    private WarehouseProduct warehouseProduct;

    @ManyToMany(mappedBy = "products")
    private List<RecipeIngredientCategory> recipeIngredientCategories;

    @ManyToMany(mappedBy = "products")
    private List<WarehouseProductCategory> warehouseProductCategories;
}
