package com.ordino.domain.warehouse.warehouse_batches.logs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;

public interface WarehouseBatchEventLogRepository extends JpaRepository<WarehouseBatchEventLog, Long> {
}
