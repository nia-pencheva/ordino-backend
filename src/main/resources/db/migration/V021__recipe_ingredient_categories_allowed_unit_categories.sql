CREATE TABLE recipe_ingredient_categories_allowed_unit_categories (
    id                             INT UNSIGNED NOT NULL AUTO_INCREMENT,
    recipes_ingredient_category_id INT UNSIGNED NOT NULL,
    unit_category_id               INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_ricuc (recipes_ingredient_category_id, unit_category_id),
    CONSTRAINT fk_ricuc_recipe_ingredient_category_id FOREIGN KEY (recipes_ingredient_category_id) REFERENCES recipe_ingredient_categories (id),
    CONSTRAINT fk_ricuc_unit_category_id FOREIGN KEY (unit_category_id) REFERENCES unit_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_ingredient_categories_allowed_unit_categories TO '${appUser}'@'%';
