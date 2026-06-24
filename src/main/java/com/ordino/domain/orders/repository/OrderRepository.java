package com.ordino.domain.orders.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.orders.model.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.supplier.id = :supplierId AND (:from IS NULL OR o.createdAt >= :from) AND (:to IS NULL OR o.createdAt <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findBySupplierIdAndCreatedAtBetweenAndOrderStatus(@Param("supplierId") Long supplierId, @Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.supplier.id = :supplierId AND (:from IS NULL OR o.actualDelivery >= :from) AND (:to IS NULL OR o.actualDelivery <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findBySupplierIdAndReceivedAtBetweenAndOrderStatus(@Param("supplierId") Long supplierId, @Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderProducts op WHERE o.supplier.id = :supplierId AND op.warehouseProduct.id = :warehouseProductId AND (:from IS NULL OR o.createdAt >= :from) AND (:to IS NULL OR o.createdAt <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findBySupplierIdAndWarehouseProductIdAndCreatedAtBetweenAndOrderStatus(@Param("supplierId") Long supplierId, @Param("warehouseProductId") Long warehouseProductId, @Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderProducts op WHERE o.supplier.id = :supplierId AND op.warehouseProduct.id = :warehouseProductId AND (:from IS NULL OR o.actualDelivery >= :from) AND (:to IS NULL OR o.actualDelivery <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findBySupplierIdAndWarehouseProductIdAndReceivedAtBetweenAndOrderStatus(@Param("supplierId") Long supplierId, @Param("warehouseProductId") Long warehouseProductId, @Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE (:from IS NULL OR o.createdAt >= :from) AND (:to IS NULL OR o.createdAt <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findAllByCreatedAtBetweenAndOrderStatus(@Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE (:from IS NULL OR o.actualDelivery >= :from) AND (:to IS NULL OR o.actualDelivery <= :to) AND (:orderStatus IS NULL OR o.orderStatus.status = :orderStatus) ORDER BY o.createdAt DESC")
    Page<Order> findAllByReceivedAtBetweenAndOrderStatus(@Param("from") Instant from, @Param("to") Instant to, @Param("orderStatus") String orderStatus, Pageable pageable);
}
