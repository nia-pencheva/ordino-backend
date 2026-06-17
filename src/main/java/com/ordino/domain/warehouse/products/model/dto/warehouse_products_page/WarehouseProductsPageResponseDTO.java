package com.ordino.domain.warehouse.products.model.dto.warehouse_products_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseProductsPageResponseDTO {
    private List<WarehouseProductForPageResponseDTO> warehouseProducts;
    private Long totalElements;
    private Integer totalPages;
}
