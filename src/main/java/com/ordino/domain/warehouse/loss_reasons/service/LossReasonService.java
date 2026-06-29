package com.ordino.domain.warehouse.loss_reasons.service;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonRequestDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonResponseDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonsPageResponseDTO;

import jakarta.persistence.EntityNotFoundException;

public interface LossReasonService {
    LossReasonsPageResponseDTO getLossReasons(Integer page, Integer pageSize);

    LossReasonResponseDTO getLossReason(Long id) throws EntityNotFoundException;

    void addLossReason(LossReasonRequestDTO dto);

    void saveLossReason(Long id, LossReasonRequestDTO dto) throws EntityNotFoundException;

    void deleteLossReason(Long id) throws EntityNotFoundException, ForbiddenOperationException;
}
