package com.ordino.domain.orders;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "order_statuses")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String status;

    @OneToMany(mappedBy = "orderStatus")
    private List<Order> orders;
}
