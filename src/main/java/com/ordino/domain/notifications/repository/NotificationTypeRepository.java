package com.ordino.domain.notifications.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.notifications.model.entity.NotificationType;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    Optional<NotificationType> findByType(String type);
}
