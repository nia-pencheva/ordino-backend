package com.ordino.domain.notifications.model.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private Instant createdAt;
}
