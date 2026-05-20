CREATE TABLE order_statuses (
    id     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    status VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_order_statuses_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO order_statuses (status) VALUES
    ('PENDING'),
    ('RECEIVED'),
    ('CANCELLED');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.order_statuses TO '${appUser}'@'%';
