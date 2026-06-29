package com.ordino.domain.warehouse.warehouse_batches.logs.service;

import java.math.BigDecimal;
import java.time.Instant;

import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.page.WarehouseBatchEventLogPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;

public interface WarehouseBatchEventLogService {
    WarehouseBatchEventLogPageResponseDTO getWarehouseBatchLog(Integer page, Integer pageSize, Long warehouseProductId, Long warehouseProductCategoryId, Instant from, Instant to);

    WarehouseBatchEventLogEntryResponseDTO getWarehouseBatchLogEntry(Long id);

    void createLog(User user, WarehouseBatch batch, WarehouseBatchEvent event, BigDecimal quantity, String notes);

    void createLog(User user, WarehouseBatch batch, WarehouseBatchEvent event, BigDecimal quantity, LossReason lossReason, String notes);
}
