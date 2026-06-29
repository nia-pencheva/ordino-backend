package com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchOfProductResponseDTO {
    private Long id;
    private LocalDate expiryDate;
}
