CREATE TABLE recipe_edit_logs (
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id    INT UNSIGNED    NOT NULL,
    recipe_id  INT UNSIGNED    NOT NULL,
    old_data   JSON            NULL,
    new_data   JSON            NULL,
    created_at TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_recipe_edit_logs_recipe_created (recipe_id, created_at),
    CONSTRAINT fk_rel_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_rel_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT ON ${dbName}.recipe_edit_logs TO '${appUser}'@'%';
