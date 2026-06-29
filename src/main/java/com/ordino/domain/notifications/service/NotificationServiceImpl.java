package com.ordino.domain.notifications.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.core.util.FCMSender;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.ordino.domain.notifications.model.dto.NotificationResponseDTO;
import com.ordino.domain.notifications.model.dto.NotificationsPageResponseDTO;
import com.ordino.domain.notifications.model.entity.Notification;
import com.ordino.domain.notifications.model.entity.NotificationType;
import com.ordino.domain.notifications.repository.NotificationRepository;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final Integer pageSize = 10;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FCMSender fcmSender;

    public NotificationsPageResponseDTO getNotificationsForUser(Long id, Integer page, Integer pageSize, Boolean read) {
        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        if (!currentUserId.equals(id)) {
            throw new EntityNotFoundException("Notifications not found");
        }

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<Notification> notificationsPage;
        if (read) {
            notificationsPage = notificationRepository.findByUserIdAndReadAtIsNotNullOrderByCreatedAtDesc(id, pageRequest);
        } else {
            notificationsPage = notificationRepository.findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(id, pageRequest);
        }

        NotificationsPageResponseDTO responseDTO = new NotificationsPageResponseDTO();

        responseDTO.setNotifications(
            notificationsPage.stream()
                .map(this::mapToDTO)
                .toList()
        );

        responseDTO.setTotalElements(notificationsPage.getTotalElements());
        responseDTO.setTotalPages(notificationsPage.getTotalPages());

        return responseDTO;
    }

    public NotificationResponseDTO getNotification(Long userId, Long notificationId) {
        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        if (!currentUserId.equals(userId)) {
            throw new EntityNotFoundException("Notification not found");
        }

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("Notification not found");
        }

        return mapToDTO(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Notification not found");
        }

        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
    }

    public boolean hasUnreadNotifications() {
        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        return notificationRepository.existsByUserIdAndReadAtIsNull(currentUserId);
    }

    public void createNotification(User user, NotificationType notificationType, String title, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setNotificationType(notificationType);
        notification.setTitle(title);
        notification.setMessage(message);
        notificationRepository.saveAndFlush(notification);

        if (user.getFcmToken() != null) {
            String token = user.getFcmToken();
            Runnable sendFcm = () -> {
                try {
                    fcmSender.send(token, title, message);
                } catch (FirebaseMessagingException e) {
                    log.warn("Failed to send FCM notification to user {}: {}", user.getId(), e.getMessage());
                }
            };

            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendFcm.run();
                    }
                });
            } else {
                sendFcm.run();
            }
        }
    }

    @Transactional
    public void registerFcmToken(String token) {
        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFcmToken(token);
        userRepository.save(user);
    }

    private NotificationResponseDTO mapToDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
