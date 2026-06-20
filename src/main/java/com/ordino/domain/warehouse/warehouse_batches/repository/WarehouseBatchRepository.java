package com.ordino.domain.warehouse.warehouse_batches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;

public interface WarehouseBatchRepository extends JpaRepository<WarehouseBatch, Long> {
}
