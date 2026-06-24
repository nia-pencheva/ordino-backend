package com.ordino.domain.warehouse.warehouse_batches.model.dto.batch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchResponseDTO {
    private Long id;
    private WarehouseBatchResponseProductDTO product;
    private Long orderId;
    private BigDecimal quantity;
    private String unitAbbreviation;
    private LocalDate expiryDate;
    private List<WarehouseBatchEventLogEntryResponseDTO> events;
}
