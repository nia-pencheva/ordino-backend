CREATE TABLE warehouse_batches (
    id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    warehouse_product_id INT UNSIGNED    NOT NULL,
    quantity             DECIMAL(10, 3) UNSIGNED NOT NULL,
    expiry_date          DATE            NULL,
    order_id             BIGINT UNSIGNED NULL DEFAULT NULL,
    created_at           TIMESTAMP       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_wb_warehouse_product_id FOREIGN KEY (warehouse_product_id) REFERENCES warehouse_products (id),
    CONSTRAINT fk_wb_order_id FOREIGN KEY (order_id) REFERENCES orders (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE ON ${dbName}.warehouse_batches TO '${appUser}'@'%';
