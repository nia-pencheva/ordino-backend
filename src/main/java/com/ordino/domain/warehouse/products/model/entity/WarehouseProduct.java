package com.ordino.domain.warehouse.products.model.entity;

import com.ordino.domain.orders.model.entity.OrderProduct;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "warehouse_products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"product", "unit", "warehouseBatches", "supplierProducts", "orderProducts"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WarehouseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "min_quantity", nullable = false, columnDefinition = "DECIMAL(10, 3)")
    private BigDecimal minQuantity;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<WarehouseBatch> warehouseBatches;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<SupplierProduct> supplierProducts;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<OrderProduct> orderProducts;
}
