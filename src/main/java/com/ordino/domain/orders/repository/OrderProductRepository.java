package com.ordino.domain.orders.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.orders.model.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op.warehouseProduct.id FROM OrderProduct op WHERE op.order.id = :orderId")
    Set<Long> findWarehouseProductIdsByOrderId(@Param("orderId") Long orderId);
}
