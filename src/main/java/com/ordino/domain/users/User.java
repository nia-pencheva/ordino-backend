package com.ordino.domain.users;

import com.ordino.domain.notifications.Notification;
import com.ordino.domain.orders.Order;
import com.ordino.domain.recipes.Recipe;
import com.ordino.domain.recipes.logs.archive.RecipeArchiveLog;
import com.ordino.domain.recipes.logs.edits.RecipeEditLog;
import com.ordino.domain.recipes.logs.review.RecipeReviewLog;
import com.ordino.domain.warehouse.warehouse_batches.logs.WarehouseBatchEventLog;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

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
