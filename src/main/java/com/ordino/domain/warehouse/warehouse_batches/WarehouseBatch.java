package com.ordino.domain.warehouse.warehouse_batches;

import com.ordino.domain.orders.Order;
import com.ordino.domain.warehouse.products.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.logs.WarehouseBatchEventLog;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "warehouse_batches")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"warehouseProduct", "order", "eventLogs"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WarehouseBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    private WarehouseProduct warehouseProduct;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 3) UNSIGNED")
    private BigDecimal quantity;

    @Column(name = "expiry_date", nullable = true)
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @OneToMany(mappedBy = "warehouseBatch")
    private List<WarehouseBatchEventLog> eventLogs;
}
