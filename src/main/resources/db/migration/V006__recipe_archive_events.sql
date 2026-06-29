CREATE TABLE recipe_archive_events (
    id    INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    event VARCHAR(100)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipe_archive_events_event (event)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO recipe_archive_events (event) VALUES
    ('ARCHIVED'),
    ('RETURNED_TO_APPROVED');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_archive_events TO '${appUser}'@'%';
