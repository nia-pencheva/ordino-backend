CREATE TABLE notifications (
    id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    title                VARCHAR(250)    NOT NULL,
    message              TEXT            NOT NULL,
    notification_type_id INT UNSIGNED    NOT NULL,
    user_id              INT UNSIGNED    NOT NULL,
    read_at              TIMESTAMP       NULL DEFAULT NULL,
    created_at           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_notifications_user_read_created (user_id, read_at, created_at),
    CONSTRAINT fk_notifications_notification_type_id FOREIGN KEY (notification_type_id) REFERENCES notification_types (id),
    CONSTRAINT fk_notifications_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.notifications TO '${appUser}'@'%';
