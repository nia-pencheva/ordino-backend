package com.ordino.domain.notifications.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.notifications.model.dto.NotificationResponseDTO;
import com.ordino.domain.notifications.model.dto.NotificationsPageResponseDTO;
import com.ordino.domain.notifications.model.dto.RegisterDeviceRequestDTO;
import com.ordino.domain.notifications.service.NotificationService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notifications")
@Validated
@AllArgsConstructor
public class NotificationsController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<NotificationsPageResponseDTO> getNotificationsForUser(
        @PathVariable @Positive Long id,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam Boolean read
    ) {
        return ResponseEntity.ok().body(notificationService.getNotificationsForUser(id, page, pageSize, read));
    }

    @GetMapping("/{userId}/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotification(
        @PathVariable @Positive Long userId,
        @PathVariable @Positive Long notificationId
    ) {
        return ResponseEntity.ok().body(notificationService.getNotification(userId, notificationId));
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable @Positive Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/has-unread")
    public ResponseEntity<Boolean> hasUnreadNotifications() {
        return ResponseEntity.ok(notificationService.hasUnreadNotifications());
    }

    @PostMapping("/register-device")
    public ResponseEntity<Void> registerFcmToken(@RequestBody RegisterDeviceRequestDTO dto) {
        notificationService.registerFcmToken(dto.getToken());
        return ResponseEntity.noContent().build();
    }
}
