package com.ordino.domain.orders.repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.orders.model.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op.warehouseProduct.id FROM OrderProduct op WHERE op.order.id = :orderId")
    Set<Long> findWarehouseProductIdsByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT op FROM OrderProduct op WHERE (:from IS NULL OR op.createdAt >= :from) AND (:to IS NULL OR op.createdAt <= :to) ORDER BY op.createdAt ASC")
    List<OrderProduct> findByCreatedAtBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query(
        value = """
            SELECT op.warehouseProduct.product.name, COUNT(op)
            FROM OrderProduct op
            WHERE (:from IS NULL OR op.createdAt >= :from)
            AND (:to IS NULL OR op.createdAt <= :to)
            GROUP BY op.warehouseProduct.product.id, op.warehouseProduct.product.name
            ORDER BY COUNT(op) DESC
        """,
        countQuery = """
            SELECT COUNT(DISTINCT op.warehouseProduct.product.id)
            FROM OrderProduct op
            WHERE (:from IS NULL OR op.createdAt >= :from)
            AND (:to IS NULL OR op.createdAt <= :to)
        """
    )
    Page<Object[]> findProductOrderFrequencyByNameAndCreatedAtBetween(
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );
}
