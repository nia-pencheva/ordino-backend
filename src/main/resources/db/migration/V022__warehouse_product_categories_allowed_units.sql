CREATE TABLE warehouse_product_categories_allowed_units (
    id                            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    warehouse_product_category_id INT UNSIGNED NOT NULL,
    unit_id                       INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_wpcu (warehouse_product_category_id, unit_id),
    CONSTRAINT fk_wpcu_warehouse_product_category_id FOREIGN KEY (warehouse_product_category_id) REFERENCES warehouse_product_categories (id),
    CONSTRAINT fk_wpcu_unit_id FOREIGN KEY (unit_id) REFERENCES units (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.warehouse_product_categories_allowed_units TO '${appUser}'@'%';
