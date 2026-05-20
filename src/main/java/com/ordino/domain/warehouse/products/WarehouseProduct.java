package com.ordino.domain.warehouse.products;

import com.ordino.domain.orders.OrderProduct;
import com.ordino.domain.products.Product;
import com.ordino.domain.suppliers.SupplierProduct;
import com.ordino.domain.units.Unit;
import com.ordino.domain.warehouse.warehouse_batches.WarehouseBatch;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "min_quantity", nullable = false, columnDefinition = "DECIMAL(10, 3) UNSIGNED")
    private BigDecimal minQuantity;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<WarehouseBatch> warehouseBatches;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<SupplierProduct> supplierProducts;

    @OneToMany(mappedBy = "warehouseProduct")
    private List<OrderProduct> orderProducts;
}
