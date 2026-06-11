package com.ordino.domain.warehouse.products.categories.model.dto.products;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseProductCategoryAddableProductsPageResponseDTO {
    private List<WarehouseProductCategoryAddableProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
