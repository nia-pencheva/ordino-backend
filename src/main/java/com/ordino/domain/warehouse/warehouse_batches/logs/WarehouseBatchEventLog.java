package com.ordino.domain.warehouse.warehouse_batches.logs;

import com.ordino.domain.users.User;
import com.ordino.domain.warehouse.warehouse_batches.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.WarehouseBatch;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "warehouse_batch_event_logs")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "warehouseBatch", "warehouseBatchEvent", "lossReason"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WarehouseBatchEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "warehouse_batch_id", nullable = false)
    private WarehouseBatch warehouseBatch;

    @ManyToOne
    @JoinColumn(name = "warehouse_batch_event_id", nullable = false)
    private WarehouseBatchEvent warehouseBatchEvent;

    @Column(name = "quantity_delta", nullable = false, columnDefinition = "DECIMAL(10, 3)")
    private BigDecimal quantityDelta;

    @ManyToOne
    @JoinColumn(name = "loss_reason_id", nullable = true)
    private LossReason lossReason;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;
}
