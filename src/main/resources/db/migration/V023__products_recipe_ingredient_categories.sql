CREATE TABLE products_recipe_ingredient_categories (
    id                            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    product_id                    INT UNSIGNED NOT NULL,
    recipe_ingredient_category_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_products_recipe_ingredient_categories (product_id, recipe_ingredient_category_id),
    CONSTRAINT fk_pric_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_pric_recipe_ingredient_category_id FOREIGN KEY (recipe_ingredient_category_id) REFERENCES recipe_ingredient_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.products_recipe_ingredient_categories TO '${appUser}'@'%';
