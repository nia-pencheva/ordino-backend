CREATE TABLE menus_sections_recipes (
    id              INT UNSIGNED NOT NULL AUTO_INCREMENT,
    menu_section_id INT UNSIGNED NOT NULL,
    recipe_id       INT UNSIGNED NOT NULL,
    position        INT UNSIGNED NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_menus_sections_recipes_section_recipe (menu_section_id, recipe_id),
    UNIQUE KEY uq_menus_sections_recipes_section_position (menu_section_id, position),
    CONSTRAINT fk_msr_menu_section_id FOREIGN KEY (menu_section_id) REFERENCES menus_sections (id),
    CONSTRAINT fk_msr_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.menus_sections_recipes TO '${appUser}'@'%';
