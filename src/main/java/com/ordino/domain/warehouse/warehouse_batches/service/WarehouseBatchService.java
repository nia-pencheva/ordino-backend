package com.ordino.domain.warehouse.warehouse_batches.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.notifications.warehouse.WarehouseNotificationService;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseUserDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.service.WarehouseBatchEventLogService;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.MarkQuantityUsedRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.WriteOffQuantityRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batch.WarehouseBatchResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batch.WarehouseBatchResponseProductDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page.WarehouseBatchOfProductResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page.WarehouseBatchesOfProductPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.products_page.WarehouseBatchesProductsPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.products_page.WarehouseBatchesProductsPageResponseProductDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseBatchService {
    private final Integer pageSize = 10;
    private final WarehouseBatchRepository warehouseBatchRepository;
    private final WarehouseBatchEventRepository warehouseBatchEventRepository;
    private final LossReasonRepository lossReasonRepository;
    private final WarehouseBatchEventLogService logService;
    private final WarehouseNotificationService warehouseNotificationService;

    public WarehouseBatchesProductsPageResponseDTO getProducts(Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<WarehouseProduct> productsPage = warehouseBatchRepository.findDistinctWarehouseProducts(pageRequest);

        WarehouseBatchesProductsPageResponseDTO responseDTO = new WarehouseBatchesProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage.stream()
                .map(wp -> {
                    WarehouseBatchesProductsPageResponseProductDTO dto = new WarehouseBatchesProductsPageResponseProductDTO();
                    dto.setWarehouseProductId(wp.getId());
                    dto.setProductName(wp.getProduct().getName());
                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public WarehouseBatchesOfProductPageResponseDTO getBatchesOfProduct(Long warehouseProductId, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<WarehouseBatch> batchesPage = warehouseBatchRepository.findByWarehouseProductIdAndQuantityGreaterThan(warehouseProductId, BigDecimal.ZERO, pageRequest);

        if (batchesPage.getTotalElements() == 0) {
            throw new EntityNotFoundException("No batches found for warehouse product with id: " + warehouseProductId);
        }

        WarehouseBatchesOfProductPageResponseDTO responseDTO = new WarehouseBatchesOfProductPageResponseDTO();

        responseDTO.setBatches(
            batchesPage.stream()
                .map(batch -> {
                    WarehouseBatchOfProductResponseDTO dto = new WarehouseBatchOfProductResponseDTO();
                    dto.setId(batch.getId());
                    dto.setExpiryDate(batch.getExpiryDate());
                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(batchesPage.getTotalElements());
        responseDTO.setTotalPages(batchesPage.getTotalPages());

        return responseDTO;
    }

    public WarehouseBatchResponseDTO getBatch(Long warehouseProductId, Long warehouseBatchId) {
        WarehouseBatch batch = warehouseBatchRepository.findByIdAndQuantityGreaterThan(warehouseBatchId, BigDecimal.ZERO)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch not found"));

        if (!batch.getWarehouseProduct().getId().equals(warehouseProductId)) {
            throw new EntityNotFoundException("Warehouse batch does not belong to warehouse product with id: " + warehouseProductId);
        }

        WarehouseBatchResponseProductDTO productDTO = new WarehouseBatchResponseProductDTO();
        productDTO.setId(batch.getWarehouseProduct().getId());
        productDTO.setName(batch.getWarehouseProduct().getProduct().getName());

        WarehouseBatchResponseDTO responseDTO = new WarehouseBatchResponseDTO();
        responseDTO.setId(batch.getId());
        responseDTO.setProduct(productDTO);
        responseDTO.setOrderId(batch.getOrder() != null ? batch.getOrder().getId() : null);
        responseDTO.setQuantity(batch.getQuantity());
        responseDTO.setUnitAbbreviation(batch.getWarehouseProduct().getUnit().getAbbreviation());
        responseDTO.setExpiryDate(batch.getExpiryDate());

        responseDTO.setEvents(
            batch.getEventLogs().stream()
                .sorted(Comparator.comparing(WarehouseBatchEventLog::getCreatedAt).reversed())
                .map(log -> {
                    WarehouseBatchEventLogEntryResponseUserDTO userDTO = new WarehouseBatchEventLogEntryResponseUserDTO();
                    userDTO.setId(log.getUser().getId());
                    userDTO.setFullName(log.getUser().getFullName());

                    WarehouseBatchEventLogEntryResponseDTO eventDTO = new WarehouseBatchEventLogEntryResponseDTO();
                    eventDTO.setId(log.getId());
                    eventDTO.setUser(userDTO);
                    eventDTO.setEventType(log.getWarehouseBatchEvent().getType());
                    eventDTO.setQuantityDelta(log.getQuantityDelta());
                    eventDTO.setCreatedAt(log.getCreatedAt());
                    eventDTO.setLossReason(log.getLossReason() != null ? log.getLossReason().getReason() : null);
                    eventDTO.setNotes(log.getNotes());
                    return eventDTO;
                })
                .toList()
        );

        return responseDTO;
    }

    @Transactional
    public String markQuantityUsed(Long id, MarkQuantityUsedRequestDTO dto) {
        User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        WarehouseBatch batch = warehouseBatchRepository.findByIdAndQuantityGreaterThan(id, BigDecimal.ZERO)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch not found"));

        WarehouseBatchEvent event = warehouseBatchEventRepository.findByType("USED")
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch event not found"));

        String redirect = applyQuantityChange(batch, dto.getQuantity());
        logService.createLog(currentUser, batch, event, dto.getQuantity().negate(), dto.getNotes());
        return redirect;
    }

    @Transactional
    public String writeOffQuantity(Long id, WriteOffQuantityRequestDTO dto) {
        User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        WarehouseBatch batch = warehouseBatchRepository.findByIdAndQuantityGreaterThan(id, BigDecimal.ZERO)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch not found"));

        WarehouseBatchEvent event = warehouseBatchEventRepository.findByType("LOST")
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch event not found"));

        LossReason lossReason = dto.getLossReasonId() != null
                ? lossReasonRepository.findById(dto.getLossReasonId())
                        .orElseThrow(() -> new EntityNotFoundException("Loss reason not found"))
                : null;

        String redirect = applyQuantityChange(batch, dto.getQuantity());
        logService.createLog(currentUser, batch, event, dto.getQuantity().negate(), lossReason, dto.getNotes());
        return redirect;
    }

    private String applyQuantityChange(WarehouseBatch batch, BigDecimal quantity) {
        BigDecimal newQuantity = batch.getQuantity().subtract(quantity);
        batch.setQuantity(newQuantity);
        warehouseBatchRepository.save(batch);
        checkNewQuantityUnderMinimum(batch);
        if (newQuantity.compareTo(BigDecimal.ZERO) > 0) return "batch";
        return warehouseBatchRepository.existsByWarehouseProductIdAndQuantityGreaterThan(batch.getWarehouseProduct().getId(), BigDecimal.ZERO) ? "product" : "stock";
    }

    private void checkNewQuantityUnderMinimum(WarehouseBatch batch) {
        BigDecimal totalProductQuantity = batch.getWarehouseProduct().getWarehouseBatches().stream()
                .map(WarehouseBatch::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalProductQuantity.compareTo(batch.getWarehouseProduct().getMinQuantity()) < 0) {
            warehouseNotificationService.sendLowQuantityNotification(batch.getWarehouseProduct());
        }
    }

    public List<WarehouseBatch> getExpiringProductBatches(Integer daysBeforeExpiry) {
        LocalDate today = LocalDate.now();
        LocalDate expiryThreshold = today.plusDays(daysBeforeExpiry);
        return warehouseBatchRepository.findByQuantityGreaterThanAndExpiryDateBetween(BigDecimal.ZERO, today, expiryThreshold);
    }
}
