package com.ordino.domain.warehouse.loss_reasons.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonRequestDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonResponseDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonsPageResponseDTO;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LossReasonServiceImpl implements LossReasonService {
    private final LossReasonRepository repository;
    private final ModelMapper mapper;

    public LossReasonsPageResponseDTO getLossReasons(Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("reason").ascending());

        Page<LossReason> lossReasonsPage = repository.findAll(pageRequest);

        LossReasonsPageResponseDTO responseDTO = new LossReasonsPageResponseDTO();

        responseDTO.setLossReasons(
            lossReasonsPage
                .stream()
                .map(lossReason -> {
                    LossReasonResponseDTO dto = mapper.map(lossReason, LossReasonResponseDTO.class);

                    dto.setDeleteForbiddenReasons(canBeDeleted(lossReason));

                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(lossReasonsPage.getTotalElements());
        responseDTO.setTotalPages(lossReasonsPage.getTotalPages());

        return responseDTO;
    }

    public LossReasonResponseDTO getLossReason(Long id) throws EntityNotFoundException {
        LossReason lossReason = repository.findById(id)
                                          .orElseThrow(() -> new EntityNotFoundException("Loss reason not found"));

        LossReasonResponseDTO responseDTO = mapper.map(lossReason, LossReasonResponseDTO.class);
        responseDTO.setDeleteForbiddenReasons(canBeDeleted(lossReason));

        return responseDTO;
    }

    public void addLossReason(LossReasonRequestDTO dto) {
        repository.save(mapper.map(dto, LossReason.class));
    }

    public void saveLossReason(Long id, LossReasonRequestDTO dto) throws EntityNotFoundException {
        LossReason lossReason = repository.findById(id)
                                          .orElseThrow(() -> new EntityNotFoundException("Loss reason not found"));

        mapper.map(dto, lossReason);
        repository.save(lossReason);
    }

    @Transactional
    public void deleteLossReason(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        LossReason lossReason = repository.findById(id)
                                          .orElseThrow(() -> new EntityNotFoundException("Loss reason not found"));

        List<String> deleteForbiddenReasons = canBeDeleted(lossReason);
        if (!deleteForbiddenReasons.isEmpty())
            throw new ForbiddenOperationException(deleteForbiddenReasons);

        repository.delete(lossReason);
    }

    private List<String> canBeDeleted(LossReason lossReason) {
        List<String> reasons = new ArrayList<>();

        if (!lossReason.getEventLogs().isEmpty()) {
            reasons.add("Cannot delete a loss reason that is used in warehouse batch event logs");
        }

        return reasons;
    }
}
