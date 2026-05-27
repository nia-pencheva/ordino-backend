package com.ordino.domain.warehouse.warehouse_batches.model.entity;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "loss_reasons")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "eventLogs")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LossReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "lossReason")
    private List<WarehouseBatchEventLog> eventLogs;
}
