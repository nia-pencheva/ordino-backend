CREATE TABLE units (
    id               INT UNSIGNED NOT NULL AUTO_INCREMENT,
    unit             VARCHAR(50)  NOT NULL,
    abbreviation     VARCHAR(30)  NOT NULL,
    unit_category_id INT UNSIGNED NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_units_unit (unit),
    UNIQUE KEY uq_units_abbreviation (abbreviation),
    CONSTRAINT fk_units_unit_category_id FOREIGN KEY (unit_category_id) REFERENCES unit_categories (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.units TO '${appUser}'@'%';
