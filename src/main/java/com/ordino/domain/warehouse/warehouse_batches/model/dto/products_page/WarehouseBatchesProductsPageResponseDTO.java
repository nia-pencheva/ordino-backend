package com.ordino.domain.warehouse.warehouse_batches.model.dto.products_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchesProductsPageResponseDTO {
    private List<WarehouseBatchesProductsPageResponseProductDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
