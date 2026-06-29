package com.ordino.domain.notifications.service;

import com.ordino.domain.notifications.model.dto.NotificationResponseDTO;
import com.ordino.domain.notifications.model.dto.NotificationsPageResponseDTO;
import com.ordino.domain.notifications.model.entity.NotificationType;
import com.ordino.domain.users.model.entity.User;

public interface NotificationService {
    NotificationsPageResponseDTO getNotificationsForUser(Long id, Integer page, Integer pageSize, Boolean read);

    NotificationResponseDTO getNotification(Long userId, Long notificationId);

    void markAsRead(Long notificationId);

    boolean hasUnreadNotifications();

    void createNotification(User user, NotificationType notificationType, String title, String message);

    void registerFcmToken(String token);
}
