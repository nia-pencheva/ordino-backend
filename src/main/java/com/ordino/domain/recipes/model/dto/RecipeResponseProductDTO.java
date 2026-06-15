package com.ordino.domain.recipes.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeResponseProductDTO {
    private Long id;
    private String name;
    private Integer position;
    private BigDecimal quantity;
    private RecipeResponseProductUnitDTO unit;
}
