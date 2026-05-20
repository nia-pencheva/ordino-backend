CREATE TABLE notification_types (
    id   INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    type VARCHAR(100)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_notification_types_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO notification_types (type) VALUES
    ('BATCH_EXPIRED'),
    ('LOW_QUANTITY'),
    ('NEW_PRODUCT');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.notification_types TO '${appUser}'@'%';
