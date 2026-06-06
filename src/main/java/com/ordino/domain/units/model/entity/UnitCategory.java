package com.ordino.domain.units.model.entity;

import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;
import com.ordino.domain.warehouse.products.categories.model.entity.WarehouseProductCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "unit_categories")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"units", "allowedInIngredientCategories", "allowedInWarehouseCategories"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UnitCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String category;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "unitCategory", cascade = CascadeType.ALL)
    private List<Unit> units;

    @ManyToMany(mappedBy = "allowedUnitCategories")
    private List<RecipeIngredientCategory> allowedInIngredientCategories;

    @ManyToMany(mappedBy = "allowedUnitCategories")
    private List<WarehouseProductCategory> allowedInWarehouseCategories;
}
