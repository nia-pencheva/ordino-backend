package com.ordino.domain.orders.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.orders.model.entity.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    Optional<OrderStatus> findByStatus(String status);
}
