package com.ordino.domain.warehouse.products.model.dto.addable_products_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseProductsAddableProductsPageResponseDTO {
    private List<WarehouseProductsAddableProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
