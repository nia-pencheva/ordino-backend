package com.ordino.domain.suppliers;

import com.ordino.domain.orders.Order;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"supplierProducts", "orders"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "supplier")
    private List<SupplierProduct> supplierProducts;

    @OneToMany(mappedBy = "supplier")
    private List<Order> orders;
}
