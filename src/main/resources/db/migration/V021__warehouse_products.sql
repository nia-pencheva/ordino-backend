CREATE TABLE warehouse_products (
    id           INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    product_id   INT UNSIGNED   NOT NULL,
    unit_id      INT UNSIGNED   NOT NULL,
    min_quantity DECIMAL(10, 3) UNSIGNED NOT NULL,
    active       BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_warehouse_products_product_id (product_id),
    CONSTRAINT fk_warehouse_products_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_warehouse_products_unit_id FOREIGN KEY (unit_id) REFERENCES units (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.warehouse_products TO '${appUser}'@'%';
