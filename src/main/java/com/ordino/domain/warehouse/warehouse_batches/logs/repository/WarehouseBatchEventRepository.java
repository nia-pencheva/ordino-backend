package com.ordino.domain.warehouse.warehouse_batches.logs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;

public interface WarehouseBatchEventRepository extends JpaRepository<WarehouseBatchEvent, Long> {

    Optional<WarehouseBatchEvent> findByType(String type);
}
