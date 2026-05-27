package com.ordino.domain.users.model.entity;

import com.ordino.domain.notifications.model.entity.Notification;
import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveLog;
import com.ordino.domain.recipes.logs.edits.model.entity.RecipeEditLog;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEventLog;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"roles", "createdByRecipes", "reviewLogs", "editLogs", "archiveLogs",
        "placedByOrders", "finalizedByOrders", "warehouseBatchEventLogs", "notifications"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "password_changed_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant passwordChangedAt;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant deletedAt;

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "createdBy")
    private List<Recipe> createdByRecipes;

    @OneToMany(mappedBy = "reviewer")
    private List<RecipeReviewLog> reviewLogs;

    @OneToMany(mappedBy = "user")
    private List<RecipeEditLog> editLogs;

    @OneToMany(mappedBy = "user")
    private List<RecipeArchiveLog> archiveLogs;

    @OneToMany(mappedBy = "placedBy")
    private List<Order> placedByOrders;

    @OneToMany(mappedBy = "finalizedBy")
    private List<Order> finalizedByOrders;

    @OneToMany(mappedBy = "user")
    private List<WarehouseBatchEventLog> warehouseBatchEventLogs;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;
}
