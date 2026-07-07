package com.ordino.domain.suppliers.model.entity;

import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "suppliers_products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"supplier", "warehouseProduct"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SupplierProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    private WarehouseProduct warehouseProduct;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal price;

    @Column(name = "min_order_quantity", nullable = false, columnDefinition = "DECIMAL(10, 3)")
    private BigDecimal minOrderQuantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;
}
