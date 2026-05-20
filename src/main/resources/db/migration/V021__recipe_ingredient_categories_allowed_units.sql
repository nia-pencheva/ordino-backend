CREATE TABLE recipe_ingredient_categories_allowed_units (
    id                             INT UNSIGNED NOT NULL AUTO_INCREMENT,
    recipes_ingredient_category_id INT UNSIGNED NOT NULL,
    unit_id                        INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_ricu (recipes_ingredient_category_id, unit_id),
    CONSTRAINT fk_ricu_recipe_ingredient_category_id FOREIGN KEY (recipes_ingredient_category_id) REFERENCES recipe_ingredient_categories (id),
    CONSTRAINT fk_ricu_unit_id FOREIGN KEY (unit_id) REFERENCES units (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_ingredient_categories_allowed_units TO '${appUser}'@'%';
