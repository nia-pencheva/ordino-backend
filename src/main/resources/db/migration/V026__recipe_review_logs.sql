CREATE TABLE recipe_review_logs (
    id                     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    recipe_id              INT UNSIGNED    NOT NULL,
    reviewer_id            INT UNSIGNED    NOT NULL,
    snapshot               JSON            NOT NULL,
    return_notes           TEXT            NULL,
    recipe_review_event_id INT UNSIGNED    NOT NULL,
    created_at             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_recipe_review_logs_recipe_created (recipe_id, created_at),
    CONSTRAINT fk_rrl_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id),
    CONSTRAINT fk_rrl_reviewer_id FOREIGN KEY (reviewer_id) REFERENCES users (id),
    CONSTRAINT fk_rrl_recipe_review_event_id FOREIGN KEY (recipe_review_event_id) REFERENCES recipe_review_events (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT ON ${dbName}.recipe_review_logs TO '${appUser}'@'%';
