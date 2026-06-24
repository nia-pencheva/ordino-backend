package com.ordino.domain.notifications.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationsPageResponseDTO {
    private List<NotificationResponseDTO> notifications;
    private Long totalElements;
    private Integer totalPages;
}
