package com.ordino.domain.orders;

import com.ordino.domain.suppliers.Supplier;
import com.ordino.domain.users.User;
import com.ordino.domain.warehouse.warehouse_batches.WarehouseBatch;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"placedBy", "finalizedBy", "supplier", "orderStatus", "orderProducts", "warehouseBatches"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "placed_by", nullable = false)
    private User placedBy;

    @ManyToOne
    @JoinColumn(name = "finalized_by", nullable = true)
    private User finalizedBy;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "expected_delivery", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant expectedDelivery;

    @Column(name = "actual_delivery", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant actualDelivery;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "order")
    private List<WarehouseBatch> warehouseBatches;
}
