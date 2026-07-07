package com.ordino.domain.warehouse.loss_reasons.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LossReasonServiceImplTest {

    private final LossReasonRepository repository = mock(LossReasonRepository.class);
    private final ModelMapper mapper = new ModelMapper();

    private final LossReasonServiceImpl service = new LossReasonServiceImpl(repository, mapper);

    @Test
    void deleteLossReason_usedInWarehouseBatchEventLogs_throwsForbiddenOperationException() {
        LossReason lossReason = new LossReason();
        lossReason.setId(1L);
        lossReason.setEventLogs(List.of(new WarehouseBatchEventLog()));

        when(repository.findById(1L)).thenReturn(Optional.of(lossReason));

        assertThatThrownBy(() -> service.deleteLossReason(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deleteLossReason_notUsedInAnyEventLogs_deletesSuccessfully() {
        LossReason lossReason = new LossReason();
        lossReason.setId(2L);
        lossReason.setEventLogs(List.of());

        when(repository.findById(2L)).thenReturn(Optional.of(lossReason));

        service.deleteLossReason(2L);

        verify(repository).delete(lossReason);
    }
}
