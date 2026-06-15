CREATE TABLE recipe_archive_logs (
    id                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                 INT UNSIGNED    NOT NULL,
    recipe_id               INT UNSIGNED    NOT NULL,
    snapshot                JSON            NOT NULL,
    recipe_archive_event_id INT UNSIGNED    NOT NULL,
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_recipe_archive_logs_recipe_created (recipe_id, created_at),
    CONSTRAINT fk_ral_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_ral_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id),
    CONSTRAINT fk_ral_recipe_archive_event_id FOREIGN KEY (recipe_archive_event_id) REFERENCES recipe_archive_events (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT ON ${dbName}.recipe_archive_logs TO '${appUser}'@'%';
