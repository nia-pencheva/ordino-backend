package com.ordino.domain.units.model.entity;

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
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"recipeProducts", "warehouseProducts", "allowedInIngredientCategories", "allowedInWarehouseCategories"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String unit;

    @Column(nullable = false, unique = true, length = 30)
    private String abbreviation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant deletedAt;

    @OneToMany(mappedBy = "unit")
    private List<RecipeProduct> recipeProducts;

    @OneToMany(mappedBy = "unit")
    private List<WarehouseProduct> warehouseProducts;

    @ManyToMany(mappedBy = "allowedUnits")
    private List<RecipeIngredientCategory> allowedInIngredientCategories;

    @ManyToMany(mappedBy = "allowedUnits")
    private List<WarehouseProductCategory> allowedInWarehouseCategories;
}
