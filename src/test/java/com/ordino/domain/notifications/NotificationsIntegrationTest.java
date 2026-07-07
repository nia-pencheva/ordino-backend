package com.ordino.domain.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ordino.domain.notifications.model.entity.Notification;
import com.ordino.domain.notifications.model.entity.NotificationType;
import com.ordino.domain.notifications.repository.NotificationRepository;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;
import com.ordino.support.AbstractIntegrationTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void seedData() {
        fixtures.seedNotificationTypes();
    }

    @Test
    void asAnyRole_registerFcmDeviceToken_returns200AndPersistsToken() throws Exception {
        User chef = fixtures.chef("chef.registerdevice");
        String token = loginAndGetToken("chef.registerdevice", "Passw0rd!");

        mockMvc.perform(post("/notifications/register-device")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content("{\"token\":\"fcm-device-token-123\"}"))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        User reloaded = userRepository.findById(chef.getId()).orElseThrow();
        assertThat(reloaded.getFcmToken()).isEqualTo("fcm-device-token-123");
    }

    @Test
    void asRecipient_markNotificationAsRead_returns200AndReadFlagPersisted() throws Exception {
        User chef = fixtures.chef("chef.marknotification");
        String token = loginAndGetToken("chef.marknotification", "Passw0rd!");

        NotificationType type = fixtures.notificationType("NEW_PRODUCT");
        Notification notification = new Notification();
        notification.setUser(chef);
        notification.setNotificationType(type);
        notification.setTitle("Test notification");
        notification.setMessage("This is a test");
        notification = notificationRepository.save(notification);

        mockMvc.perform(post("/notifications/" + notification.getId() + "/mark-read")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        Notification reloaded = notificationRepository.findById(notification.getId()).orElseThrow();
        assertThat(reloaded.getReadAt()).isNotNull();
    }

    @Test
    void asRecipient_hasUnreadNotifications_returnsTrueThenFalseAfterMarkingRead() throws Exception {
        User chef = fixtures.chef("chef.unreadcheck");
        String token = loginAndGetToken("chef.unreadcheck", "Passw0rd!");

        NotificationType type = fixtures.notificationType("NEW_PRODUCT");
        Notification notification = new Notification();
        notification.setUser(chef);
        notification.setNotificationType(type);
        notification.setTitle("Unread test");
        notification.setMessage("Body");
        notification = notificationRepository.save(notification);

        mockMvc.perform(get("/notifications/has-unread").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("true"));

        mockMvc.perform(post("/notifications/" + notification.getId() + "/mark-read")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/notifications/has-unread").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void chefSelfApprovesRecipeWithNewProduct_warehouseManagerReceivesNewProductNotification() throws Exception {
        fixtures.seedRecipeReferenceData();
        User warehouseManager = fixtures.warehouseManager("wm.newproductnotify");

        Product product = fixtures.product("Saffron");
        Unit unit = fixtures.unit("gram", "g");
        fixtures.recipeCategory("Spices");

        User chef = fixtures.chef("chef.newproductrecipe");
        String chefToken = loginAndGetToken("chef.newproductrecipe", "Passw0rd!");

        String body = """
                {
                  "title": "Saffron Rice",
                  "preparationTime": 30,
                  "servings": 4,
                  "instructions": "[{\\"text\\":\\"Cook rice with saffron\\"}]",
                  "notes": "notes",
                  "description": "description",
                  "recipeProducts": [{"productId": %d, "position": 1, "quantity": 1, "unitId": %d}],
                  "recipeCategories": [{"recipeCategoryId": %d}]
                }
                """.formatted(product.getId(), unit.getId(), fixtures.recipeCategory("Spices").getId());

        String response = mockMvc.perform(post("/recipes/draft")
                        .header("Authorization", bearer(chefToken))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long recipeId = Long.parseLong(response);

        mockMvc.perform(post("/recipes/" + recipeId + "/self-approve")
                        .header("Authorization", bearer(chefToken)))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        assertThat(notificationRepository.findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(
                warehouseManager.getId(), org.springframework.data.domain.PageRequest.of(0, 10)
        ).getContent()).anyMatch(n -> n.getTitle().contains("New products"));
    }
}
