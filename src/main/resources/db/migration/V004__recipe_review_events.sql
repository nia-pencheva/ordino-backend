CREATE TABLE recipe_review_events (
    id    INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    event VARCHAR(100)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_recipe_review_events_event (event)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO recipe_review_events (event) VALUES
    ('SUBMITTED_FOR_APPROVAL'),
    ('RETURNED_FOR_REVISION'),
    ('DISCARDED'),
    ('APPROVED');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.recipe_review_events TO '${appUser}'@'%';
