CREATE TABLE recipes_images (
    id         INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    recipe_id  INT UNSIGNED  NOT NULL,
    image_path VARCHAR(500)  NOT NULL,
    position   INT UNSIGNED  NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    deleted_at TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX idx_recipes_images_recipe_id (recipe_id),
    UNIQUE KEY uq_recipes_images_recipe_position (recipe_id, position),
    CONSTRAINT fk_recipes_images_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipes_images TO '${appUser}'@'%';
