CREATE TABLE orders_products (
    id                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id             BIGINT UNSIGNED NOT NULL,
    warehouse_product_id INT UNSIGNED    NOT NULL,
    price                DECIMAL(10, 2) NOT NULL,
    expected_quantity    DECIMAL(10, 3) NOT NULL,
    received_quantity    DECIMAL(10, 3) NULL,
    created_at           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_orders_products (order_id, warehouse_product_id),
    CONSTRAINT fk_op_order_id FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_op_warehouse_product_id FOREIGN KEY (warehouse_product_id) REFERENCES warehouse_products (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE ON ${dbName}.orders_products TO '${appUser}'@'%';
