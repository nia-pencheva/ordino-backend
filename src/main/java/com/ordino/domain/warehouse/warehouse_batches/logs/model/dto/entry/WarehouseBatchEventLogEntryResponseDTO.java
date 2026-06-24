package com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchEventLogEntryResponseDTO {
    private Long id;
    private WarehouseBatchEventLogEntryResponseProductDTO product;
    private WarehouseBatchEventLogEntryResponseUserDTO user;
    private String eventType;
    private BigDecimal quantityDelta;
    private String unitAbbreviation;
    private Instant createdAt;
    private String lossReason;
    private String notes;
}
