CREATE TABLE recipes (
    id               INT UNSIGNED      NOT NULL AUTO_INCREMENT,
    title            VARCHAR(250)      NULL,
    preparation_time SMALLINT UNSIGNED NULL,
    servings         TINYINT UNSIGNED  NULL,
    instructions     JSON              NULL,
    notes            TEXT              NULL,
    description      TEXT              NULL,
    recipe_status_id INT UNSIGNED      NOT NULL,
    created_by       INT UNSIGNED      NOT NULL,
    created_at       TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active           BOOLEAN           NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipes_title_active (title, active),
    CONSTRAINT fk_recipes_recipe_status_id FOREIGN KEY (recipe_status_id) REFERENCES recipe_statuses (id),
    CONSTRAINT fk_recipes_created_by FOREIGN KEY (created_by) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipes TO '${appUser}'@'%';
