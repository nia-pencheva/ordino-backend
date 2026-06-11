package com.ordino.domain.warehouse.products.categories.model.dto.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseProductCategoryAddableProductResponseDTO {
    private Long id;
    private String name;
    private Boolean active;
}
