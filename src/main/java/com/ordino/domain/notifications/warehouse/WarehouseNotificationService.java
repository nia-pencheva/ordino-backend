package com.ordino.domain.notifications.warehouse;

import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;

public interface WarehouseNotificationService {
    void sendBatchExpiringNotifications();

    void sendLowQuantityNotification(WarehouseProduct product);

    void sendNewProductsInRecipeNotification(Recipe recipe);
}
