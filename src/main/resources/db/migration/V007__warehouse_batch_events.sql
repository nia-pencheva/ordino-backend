CREATE TABLE warehouse_batch_events (
    id   INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    type VARCHAR(100)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_warehouse_batch_events_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO warehouse_batch_events (type) VALUES
    ('USED'),
    ('LOST'),
    ('RECEIVED');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.warehouse_batch_events TO '${appUser}'@'%';
