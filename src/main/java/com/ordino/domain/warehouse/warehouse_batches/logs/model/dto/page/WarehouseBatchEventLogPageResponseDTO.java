package com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.page;

import java.util.List;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseBatchEventLogPageResponseDTO {
    private List<WarehouseBatchEventLogEntryResponseDTO> entries;
    private Long totalElements;
    private Integer totalPages;
}
