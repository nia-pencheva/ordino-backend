package com.ordino.domain.orders.model.entity;

import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "order")
    private List<WarehouseBatch> warehouseBatches;
}
