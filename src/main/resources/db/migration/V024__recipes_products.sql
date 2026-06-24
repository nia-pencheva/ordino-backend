CREATE TABLE recipes_products (
    id         INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    recipe_id  INT UNSIGNED   NOT NULL,
    position   INT UNSIGNED   NOT NULL,
    product_id INT UNSIGNED   NOT NULL,
    quantity   DECIMAL(10, 3) UNSIGNED NOT NULL,
    unit_id    INT UNSIGNED   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipes_products_recipe_product (recipe_id, product_id),
    UNIQUE KEY uq_recipes_products_recipe_position (recipe_id, position),
    CONSTRAINT fk_recipes_products_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipes_products_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_recipes_products_unit_id FOREIGN KEY (unit_id) REFERENCES units (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipes_products TO '${appUser}'@'%';
