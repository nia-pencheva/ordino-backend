CREATE TABLE warehouse_product_categories_allowed_unit_categories (
    id                            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    warehouse_product_category_id INT UNSIGNED NOT NULL,
    unit_category_id              INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_wpcuc (warehouse_product_category_id, unit_category_id),
    CONSTRAINT fk_wpcuc_warehouse_product_category_id FOREIGN KEY (warehouse_product_category_id) REFERENCES warehouse_product_categories (id),
    CONSTRAINT fk_wpcuc_unit_category_id FOREIGN KEY (unit_category_id) REFERENCES unit_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.warehouse_product_categories_allowed_unit_categories TO '${appUser}'@'%';
