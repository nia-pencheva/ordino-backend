CREATE TABLE warehouse_batch_event_logs (
    id                       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                  INT UNSIGNED    NOT NULL,
    warehouse_batch_id       BIGINT UNSIGNED NOT NULL,
    warehouse_batch_event_id INT UNSIGNED    NOT NULL,
    quantity_delta           DECIMAL(10, 3)  NOT NULL,
    loss_reason_id           INT UNSIGNED    NULL,
    notes                    TEXT            NULL DEFAULT NULL,
    created_at               TIMESTAMP       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_wbel_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_wbel_warehouse_batch_id FOREIGN KEY (warehouse_batch_id) REFERENCES warehouse_batches (id),
    CONSTRAINT fk_wbel_warehouse_batch_event_id FOREIGN KEY (warehouse_batch_event_id) REFERENCES warehouse_batch_events (id),
    CONSTRAINT fk_wbel_loss_reason_id FOREIGN KEY (loss_reason_id) REFERENCES loss_reasons (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT ON ${dbName}.warehouse_batch_event_logs TO '${appUser}'@'%';
