package com.ordino.domain.warehouse.warehouse_batches.logs.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;


public interface WarehouseBatchEventLogRepository extends JpaRepository<WarehouseBatchEventLog, Long> {
    @Query("""
        SELECT log FROM WarehouseBatchEventLog log
        JOIN log.warehouseBatch wb
        WHERE (:warehouseProductId IS NULL OR wb.warehouseProduct.id = :warehouseProductId)
        AND (:from IS NULL OR log.createdAt >= :from)
        AND (:to IS NULL OR log.createdAt <= :to)
        ORDER BY log.createdAt DESC
    """)
    Page<WarehouseBatchEventLog> findAllByWarehouseProductIdAndCreatedAtBetween(
        @Param("warehouseProductId") Long warehouseProductId,
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );

    @Query("""
        SELECT log FROM WarehouseBatchEventLog log
        JOIN log.warehouseBatch wb
        WHERE wb.warehouseProduct.product IN (
            SELECT p FROM WarehouseProductCategory wpc JOIN wpc.products p
            WHERE wpc.id IN :categoryIds
        )
        AND (:from IS NULL OR log.createdAt >= :from)
        AND (:to IS NULL OR log.createdAt <= :to)
        ORDER BY log.createdAt DESC
    """)
    Page<WarehouseBatchEventLog> findByWarehouseProductCategoryIdsAndCreatedAtBetween(
        @Param("categoryIds") List<Long> categoryIds,
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );
}
