CREATE TABLE units (
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    unit         VARCHAR(50)  NOT NULL,
    abbreviation VARCHAR(30)  NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NULL DEFAULT NULL,
    deleted_at   TIMESTAMP    NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_units_unit (unit),
    UNIQUE KEY uq_units_abbreviation (abbreviation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.units TO '${appUser}'@'%';
