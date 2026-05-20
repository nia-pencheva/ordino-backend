CREATE TABLE recipe_categories (
    id         INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    category   VARCHAR(100)  NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    updated_at TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipe_categories_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_categories TO '${appUser}'@'%';
