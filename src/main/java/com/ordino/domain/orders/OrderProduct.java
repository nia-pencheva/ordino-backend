package com.ordino.domain.orders;

import com.ordino.domain.warehouse.products.WarehouseProduct;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders_products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"order", "warehouseProduct"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    private WarehouseProduct warehouseProduct;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2) UNSIGNED")
    private BigDecimal price;

    @Column(name = "expected_quantity", nullable = false, columnDefinition = "DECIMAL(10, 3) UNSIGNED")
    private BigDecimal expectedQuantity;

    @Column(name = "received_quantity", nullable = true, columnDefinition = "DECIMAL(10, 3) UNSIGNED")
    private BigDecimal receivedQuantity;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;
}
