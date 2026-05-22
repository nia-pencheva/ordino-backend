CREATE TABLE suppliers_products (
    id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    supplier_id          INT UNSIGNED   NOT NULL,
    warehouse_product_id INT UNSIGNED   NOT NULL,
    price                DECIMAL(10, 2) UNSIGNED NOT NULL,
    min_order_quantity   DECIMAL(10, 3) UNSIGNED NOT NULL,
    created_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP      NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_suppliers_products (supplier_id, warehouse_product_id),
    CONSTRAINT fk_sp_supplier_id FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    CONSTRAINT fk_sp_warehouse_product_id FOREIGN KEY (warehouse_product_id) REFERENCES warehouse_products (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.suppliers_products TO '${appUser}'@'%';
