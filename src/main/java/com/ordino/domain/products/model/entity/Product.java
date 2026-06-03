package com.ordino.domain.products.model.entity;

import com.ordino.domain.recipes.products.model.entity.RecipeProduct;
import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.categories.model.entity.WarehouseProductCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
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
