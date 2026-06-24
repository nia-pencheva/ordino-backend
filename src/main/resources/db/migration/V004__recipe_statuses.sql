CREATE TABLE recipe_statuses (
    id     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    status VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipe_statuses_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO recipe_statuses (status) VALUES
    ('DRAFT'),
    ('WAITING_FOR_APPROVAL'),
    ('RETURNED_FOR_REVISION'),
    ('DISCARDED'),
    ('APPROVED'),
    ('ARCHIVED');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_statuses TO '${appUser}'@'%';
