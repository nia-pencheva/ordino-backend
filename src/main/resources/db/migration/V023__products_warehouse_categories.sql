CREATE TABLE products_warehouse_categories (
    id                            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    product_id                    INT UNSIGNED NOT NULL,
    warehouse_product_category_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_products_warehouse_categories (product_id, warehouse_product_category_id),
    CONSTRAINT fk_pwc_product_id FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_pwc_warehouse_product_category_id FOREIGN KEY (warehouse_product_category_id) REFERENCES warehouse_product_categories (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.products_warehouse_categories TO '${appUser}'@'%';
