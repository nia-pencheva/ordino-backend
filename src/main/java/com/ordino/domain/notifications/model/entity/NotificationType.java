package com.ordino.domain.notifications.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "notification_types")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "notifications")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String type;

    @OneToMany(mappedBy = "notificationType")
    private List<Notification> notifications;
}
