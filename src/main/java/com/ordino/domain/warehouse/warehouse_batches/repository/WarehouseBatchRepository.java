package com.ordino.domain.warehouse.warehouse_batches.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;

public interface WarehouseBatchRepository extends JpaRepository<WarehouseBatch, Long> {
    @Query("SELECT DISTINCT b.warehouseProduct FROM WarehouseBatch b WHERE b.quantity > 0")
    Page<WarehouseProduct> findDistinctWarehouseProducts(Pageable pageable);

    Page<WarehouseBatch> findByWarehouseProductIdAndQuantityGreaterThan(Long warehouseProductId, BigDecimal quantity, Pageable pageable);

    Optional<WarehouseBatch> findByIdAndQuantityGreaterThan(Long id, BigDecimal quantity);

    boolean existsByWarehouseProductIdAndQuantityGreaterThan(Long warehouseProductId, BigDecimal quantity);

    List<WarehouseBatch> findByQuantityGreaterThanAndExpiryDateBetween(BigDecimal quantity, LocalDate from, LocalDate to);
}
