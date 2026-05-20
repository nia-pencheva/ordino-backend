package com.ordino.domain.warehouse.warehouse_batches.logs;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "warehouse_batch_events")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "logs")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WarehouseBatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String type;

    @OneToMany(mappedBy = "warehouseBatchEvent")
    private List<WarehouseBatchEventLog> logs;
}
