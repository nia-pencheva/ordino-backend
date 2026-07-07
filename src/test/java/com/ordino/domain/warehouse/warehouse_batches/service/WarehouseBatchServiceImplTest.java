package com.ordino.domain.warehouse.warehouse_batches.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.notifications.warehouse.WarehouseNotificationService;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.service.WarehouseBatchEventLogService;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.MarkQuantityUsedRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.WriteOffQuantityRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import jakarta.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WarehouseBatchServiceImplTest {

    private final WarehouseBatchRepository warehouseBatchRepository = mock(WarehouseBatchRepository.class);
    private final WarehouseBatchEventRepository warehouseBatchEventRepository = mock(WarehouseBatchEventRepository.class);
    private final LossReasonRepository lossReasonRepository = mock(LossReasonRepository.class);
    private final WarehouseBatchEventLogService logService = mock(WarehouseBatchEventLogService.class);
    private final WarehouseNotificationService warehouseNotificationService = mock(WarehouseNotificationService.class);

    private final WarehouseBatchServiceImpl service = new WarehouseBatchServiceImpl(
            warehouseBatchRepository, warehouseBatchEventRepository, lossReasonRepository, logService, warehouseNotificationService
    );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new DatabaseUserDetails(user), null, List.of())
        );
    }

    private WarehouseProduct warehouseProductWithMinQuantity(BigDecimal minQuantity) {
        WarehouseProduct product = new WarehouseProduct();
        product.setId(1L);
        product.setMinQuantity(minQuantity);
        Unit unit = new Unit();
        unit.setAbbreviation("g");
        product.setUnit(unit);
        return product;
    }

    private WarehouseBatch batchWithQuantity(BigDecimal quantity, WarehouseProduct product) {
        WarehouseBatch batch = new WarehouseBatch();
        batch.setId(100L);
        batch.setQuantity(quantity);
        batch.setWarehouseProduct(product);
        product.setWarehouseBatches(List.of(batch));
        return batch;
    }

    @Test
    void markQuantityUsed_validQuantity_reducesBatchQuantityAndLogsUsedEventWithNegativeDelta() {
        User warehouseManager = new User();
        warehouseManager.setId(1L);
        authenticateAs(warehouseManager);

        WarehouseProduct product = warehouseProductWithMinQuantity(BigDecimal.ZERO);
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("10.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));

        WarehouseBatchEvent usedEvent = new WarehouseBatchEvent();
        usedEvent.setType("USED");
        when(warehouseBatchEventRepository.findByType("USED")).thenReturn(Optional.of(usedEvent));

        MarkQuantityUsedRequestDTO dto = new MarkQuantityUsedRequestDTO();
        dto.setQuantity(new BigDecimal("3.000"));
        dto.setNotes("used in prep");

        service.markQuantityUsed(100L, dto);

        assertThat(batch.getQuantity()).isEqualByComparingTo("7.000");
        verify(logService).createLog(eq(warehouseManager), eq(batch), eq(usedEvent), eq(new BigDecimal("-3.000")), eq("used in prep"));
    }

    @Test
    void writeOffQuantity_withLossReason_reducesQuantityAndLogsLostEventWithReason() {
        User warehouseManager = new User();
        warehouseManager.setId(1L);
        authenticateAs(warehouseManager);

        WarehouseProduct product = warehouseProductWithMinQuantity(BigDecimal.ZERO);
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("10.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));

        WarehouseBatchEvent lostEvent = new WarehouseBatchEvent();
        lostEvent.setType("LOST");
        when(warehouseBatchEventRepository.findByType("LOST")).thenReturn(Optional.of(lostEvent));

        LossReason spoiled = new LossReason();
        spoiled.setId(5L);
        spoiled.setReason("Spoiled");
        when(lossReasonRepository.findById(5L)).thenReturn(Optional.of(spoiled));

        WriteOffQuantityRequestDTO dto = new WriteOffQuantityRequestDTO();
        dto.setQuantity(new BigDecimal("4.000"));
        dto.setLossReasonId(5L);
        dto.setNotes("gone bad");

        service.writeOffQuantity(100L, dto);

        assertThat(batch.getQuantity()).isEqualByComparingTo("6.000");
        verify(logService).createLog(eq(warehouseManager), eq(batch), eq(lostEvent), eq(new BigDecimal("-4.000")), eq(spoiled), eq("gone bad"));
    }

    @Test
    void writeOffQuantity_lossReasonIdNotFound_throwsEntityNotFound() {
        authenticateAs(new User());

        WarehouseProduct product = warehouseProductWithMinQuantity(BigDecimal.ZERO);
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("10.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));
        when(warehouseBatchEventRepository.findByType("LOST")).thenReturn(Optional.of(new WarehouseBatchEvent()));
        when(lossReasonRepository.findById(999L)).thenReturn(Optional.empty());

        WriteOffQuantityRequestDTO dto = new WriteOffQuantityRequestDTO();
        dto.setQuantity(new BigDecimal("1.000"));
        dto.setLossReasonId(999L);

        assertThatThrownBy(() -> service.writeOffQuantity(100L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void markQuantityUsed_resultingProductTotalBelowMinQuantity_triggersLowQuantityNotification() {
        authenticateAs(new User());

        WarehouseProduct product = warehouseProductWithMinQuantity(new BigDecimal("5.000"));
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("10.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));
        when(warehouseBatchEventRepository.findByType("USED")).thenReturn(Optional.of(new WarehouseBatchEvent()));

        MarkQuantityUsedRequestDTO dto = new MarkQuantityUsedRequestDTO();
        dto.setQuantity(new BigDecimal("7.000"));

        service.markQuantityUsed(100L, dto);

        assertThat(batch.getQuantity()).isEqualByComparingTo("3.000");
        verify(warehouseNotificationService).sendLowQuantityNotification(product);
    }

    @Test
    void markQuantityUsed_batchDepletedButProductHasOtherStockedBatches_returnsRedirectProduct() {
        authenticateAs(new User());

        WarehouseProduct product = warehouseProductWithMinQuantity(BigDecimal.ZERO);
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("5.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));
        when(warehouseBatchEventRepository.findByType("USED")).thenReturn(Optional.of(new WarehouseBatchEvent()));
        when(warehouseBatchRepository.existsByWarehouseProductIdAndQuantityGreaterThan(1L, BigDecimal.ZERO)).thenReturn(true);

        MarkQuantityUsedRequestDTO dto = new MarkQuantityUsedRequestDTO();
        dto.setQuantity(new BigDecimal("5.000"));

        String redirect = service.markQuantityUsed(100L, dto);

        assertThat(batch.getQuantity()).isEqualByComparingTo("0.000");
        assertThat(redirect).isEqualTo("product");
    }

    @Test
    void markQuantityUsed_batchDepletedAndNoOtherStockRemains_returnsRedirectStock() {
        authenticateAs(new User());

        WarehouseProduct product = warehouseProductWithMinQuantity(BigDecimal.ZERO);
        WarehouseBatch batch = batchWithQuantity(new BigDecimal("5.000"), product);
        when(warehouseBatchRepository.findByIdAndQuantityGreaterThan(100L, BigDecimal.ZERO)).thenReturn(Optional.of(batch));
        when(warehouseBatchEventRepository.findByType("USED")).thenReturn(Optional.of(new WarehouseBatchEvent()));
        when(warehouseBatchRepository.existsByWarehouseProductIdAndQuantityGreaterThan(1L, BigDecimal.ZERO)).thenReturn(false);

        MarkQuantityUsedRequestDTO dto = new MarkQuantityUsedRequestDTO();
        dto.setQuantity(new BigDecimal("5.000"));

        String redirect = service.markQuantityUsed(100L, dto);

        assertThat(redirect).isEqualTo("stock");
    }

    @Test
    void getBatchesOfProduct_noBatchesWithPositiveQuantity_throwsEntityNotFound() {
        org.springframework.data.domain.Page<WarehouseBatch> emptyPage = org.springframework.data.domain.Page.empty();
        when(warehouseBatchRepository.findByWarehouseProductIdAndQuantityGreaterThan(
                eq(1L), eq(BigDecimal.ZERO), any())).thenReturn(emptyPage);

        assertThatThrownBy(() -> service.getBatchesOfProduct(1L, 1, 10))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
