CREATE TABLE recipes_recipe_categories (
    id                 INT UNSIGNED NOT NULL AUTO_INCREMENT,
    recipe_id          INT UNSIGNED NOT NULL,
    recipe_category_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipes_recipe_categories (recipe_id, recipe_category_id),
    CONSTRAINT fk_rrc_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id),
    CONSTRAINT fk_rrc_recipe_category_id FOREIGN KEY (recipe_category_id) REFERENCES recipe_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipes_recipe_categories TO '${appUser}'@'%';
