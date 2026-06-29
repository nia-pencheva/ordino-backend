package com.ordino.domain.warehouse.warehouse_batches.logs.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.exception.ValidationException;
import com.ordino.domain.warehouse.products.categories.model.entity.WarehouseProductCategory;
import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseProductDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.page.WarehouseBatchEventLogPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventLogRepository;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseBatchEventLogServiceImpl implements WarehouseBatchEventLogService {
    private final Integer pageSize = 10;
    private final CustomMapper mapper;
    private final WarehouseBatchEventLogRepository warehouseBatchEventLogRepository;
    private final WarehouseProductCategoryRepository warehouseProductCategoryRepository;

    @Transactional(readOnly = true)
    public WarehouseBatchEventLogPageResponseDTO getWarehouseBatchLog(Integer page, Integer pageSize, Long warehouseProductId, Long warehouseProductCategoryId, Instant from, Instant to) {
        if (from != null && to != null && !from.isBefore(to)) {
            throw new ValidationException("from", "'From' must be before 'To'");
        }

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<WarehouseBatchEventLog> logsPage;
        if (warehouseProductCategoryId != null) {
            List<Long> categoryIds = collectDescendantCategoryIds(warehouseProductCategoryId);
            logsPage = warehouseBatchEventLogRepository
                    .findByWarehouseProductCategoryIdsAndCreatedAtBetween(categoryIds, from, to, pageRequest);
        } else {
            logsPage = warehouseBatchEventLogRepository
                    .findAllByWarehouseProductIdAndCreatedAtBetween(warehouseProductId, from, to, pageRequest);
        }

        List<WarehouseBatchEventLogEntryResponseDTO> entries = logsPage.stream()
                .map(this::mapLogEntry)
                .toList();

        WarehouseBatchEventLogPageResponseDTO responseDTO = new WarehouseBatchEventLogPageResponseDTO();
        responseDTO.setEntries(entries);
        responseDTO.setTotalElements(logsPage.getTotalElements());
        responseDTO.setTotalPages(logsPage.getTotalPages());
        return responseDTO;
    }

    @Transactional(readOnly = true)
    public WarehouseBatchEventLogEntryResponseDTO getWarehouseBatchLogEntry(Long id) {
        WarehouseBatchEventLog log = warehouseBatchEventLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch event log entry not found"));
        return mapLogEntry(log);
    }

    private WarehouseBatchEventLogEntryResponseDTO mapLogEntry(WarehouseBatchEventLog log) {
        WarehouseBatchEventLogEntryResponseDTO dto = mapper.map(log, WarehouseBatchEventLogEntryResponseDTO.class);

        WarehouseBatchEventLogEntryResponseProductDTO productDTO = new WarehouseBatchEventLogEntryResponseProductDTO();
        productDTO.setId(log.getWarehouseBatch().getWarehouseProduct().getId());
        productDTO.setName(log.getWarehouseBatch().getWarehouseProduct().getProduct().getName());
        dto.setProduct(productDTO);

        dto.setEventType(log.getWarehouseBatchEvent().getType());
        dto.setUnitAbbreviation(log.getWarehouseBatch().getWarehouseProduct().getUnit().getAbbreviation());
        dto.setLossReason(log.getLossReason() != null ? log.getLossReason().getReason() : null);

        return dto;
    }

    private List<Long> collectDescendantCategoryIds(Long categoryId) {
        WarehouseProductCategory category = warehouseProductCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));
        List<Long> ids = new ArrayList<>();
        collectCategoryIds(category, ids);
        return ids;
    }

    private void collectCategoryIds(WarehouseProductCategory category, List<Long> ids) {
        ids.add(category.getId());
        for (WarehouseProductCategory sub : category.getSubCategories()) {
            collectCategoryIds(sub, ids);
        }
    }

    public void createLog(User user, WarehouseBatch batch, WarehouseBatchEvent event, BigDecimal quantity, String notes) {
        createLog(user, batch, event, quantity, null, notes);
    }

    public void createLog(User user, WarehouseBatch batch, WarehouseBatchEvent event, BigDecimal quantity, LossReason lossReason, String notes) {
        WarehouseBatchEventLog eventLog = new WarehouseBatchEventLog();

        eventLog.setUser(user);
        eventLog.setWarehouseBatch(batch);
        eventLog.setWarehouseBatchEvent(event);
        eventLog.setQuantityDelta(quantity);
        eventLog.setLossReason(lossReason);
        eventLog.setNotes(notes);

        warehouseBatchEventLogRepository.save(eventLog);
    }
}
