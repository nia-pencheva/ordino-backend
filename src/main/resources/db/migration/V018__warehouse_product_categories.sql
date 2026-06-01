CREATE TABLE warehouse_product_categories (
    id                 INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    category           VARCHAR(100)  NOT NULL,
    parent_category_id INT UNSIGNED  NULL,
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_warehouse_product_categories (category, parent_category_id),
    CONSTRAINT fk_warehouse_product_categories_parent FOREIGN KEY (parent_category_id) REFERENCES warehouse_product_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.warehouse_product_categories TO '${appUser}'@'%';
