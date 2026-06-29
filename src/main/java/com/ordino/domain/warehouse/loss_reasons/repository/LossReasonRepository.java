package com.ordino.domain.warehouse.loss_reasons.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;

public interface LossReasonRepository extends JpaRepository<LossReason, Long> {
    boolean existsByReason(String reason);
    boolean existsByReasonAndIdNot(String reason, Long id);
}
