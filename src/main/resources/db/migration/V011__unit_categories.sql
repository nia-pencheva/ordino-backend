CREATE TABLE unit_categories (
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    category   VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_unit_categories_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.unit_categories TO '${appUser}'@'%';
