package com.ordino.domain.notifications.warehouse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ordino.domain.notifications.model.entity.NotificationType;
import com.ordino.domain.notifications.repository.NotificationTypeRepository;
import com.ordino.domain.notifications.service.NotificationService;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.recipes.products.model.entity.RecipeProduct;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseNotificationService {
    private final Integer daysBeforeExpiry = 7;
    private final WarehouseBatchRepository warehouseBatchRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void sendBatchExpiringNotifications() {
        List<WarehouseBatch> batches = warehouseBatchRepository.findByQuantityGreaterThanAndExpiryDateBetween(
                BigDecimal.ZERO, LocalDate.now(), LocalDate.now().plusDays(daysBeforeExpiry));

        if (!batches.isEmpty()) {
            NotificationType notificationType = notificationTypeRepository.findByType("BATCH_EXPIRING")
                    .orElseThrow(() -> new EntityNotFoundException("Notification type BATCH_EXPIRING not found"));

            String title = "Products expiring in the next " + daysBeforeExpiry + " days";

            String message = batches.stream()
                    .map(batch -> "- " + batch.getWarehouseProduct().getProduct().getName()
                            + " (Batch #" + batch.getId() + "): expires " + batch.getExpiryDate())
                    .collect(Collectors.joining("\n"));

            sendNotificationToAllWarehouseManagers(notificationType, title, message);
        }
    }

    public void sendLowQuantityNotification(WarehouseProduct product) {
        NotificationType notificationType = notificationTypeRepository.findByType("LOW_QUANTITY")
                .orElseThrow(() -> new EntityNotFoundException("Notification type LOW_QUANTITY not found"));

        BigDecimal totalQuantity = product.getWarehouseBatches().stream()
                .map(WarehouseBatch::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String title = "Low stock: " + product.getProduct().getName();

        String message = "The stock for " + product.getProduct().getName()
                + " has fallen below the minimum quantity of " + formatQuantity(product.getMinQuantity())
                + " " + product.getUnit().getAbbreviation()
                + ". Current stock: " + formatQuantity(totalQuantity)
                + " " + product.getUnit().getAbbreviation() + ".";

        sendNotificationToAllWarehouseManagers(notificationType, title, message);
    }

    public void sendNewProductsInRecipeNotification(Recipe recipe) {
        List<String> missingProductNames = recipe.getRecipeProducts().stream()
                .map(RecipeProduct::getProduct)
                .filter(product -> !warehouseProductRepository.existsByProductId(product.getId()))
                .map(Product::getName)
                .toList();

        if (missingProductNames.isEmpty()) return;

        NotificationType notificationType = notificationTypeRepository.findByType("NEW_PRODUCT")
                .orElseThrow(() -> new EntityNotFoundException("Notification type NEW_PRODUCT not found"));

        String title = "New products must be added to warehouse";

        String message = "The following products must be added to the warehouse:\n"
                + missingProductNames.stream()
                        .map(name -> "- " + name)
                        .collect(Collectors.joining("\n"));

        sendNotificationToAllWarehouseManagers(notificationType, title, message);
    }

    private String formatQuantity(BigDecimal quantity) {
        return quantity.stripTrailingZeros().toPlainString();
    }

    private void sendNotificationToAllWarehouseManagers(NotificationType notificationType, String title, String message) {
        List<User> warehouseManagers = userRepository.findByRoleName("warehouse manager");

        warehouseManagers.forEach(manager -> notificationService.createNotification(manager, notificationType, title, message));
    }
}
