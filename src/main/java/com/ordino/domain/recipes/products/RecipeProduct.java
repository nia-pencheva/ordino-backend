package com.ordino.domain.recipes.products;

import com.ordino.domain.products.Product;
import com.ordino.domain.recipes.Recipe;
import com.ordino.domain.units.Unit;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "recipes_products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"recipe", "product", "unit"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 3) UNSIGNED")
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
}
