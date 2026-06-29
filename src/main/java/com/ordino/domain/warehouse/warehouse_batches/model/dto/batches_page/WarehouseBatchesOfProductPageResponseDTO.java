package com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchesOfProductPageResponseDTO {
    private List<WarehouseBatchOfProductResponseDTO> batches;
    private Long totalElements;
    private Integer totalPages;
}
