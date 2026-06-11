-- =============================================================
-- Ordino — seed data for units of measure and product categories
-- =============================================================
-- Execution order:
--   1. unit_categories
--   2. units
--   3. recipe_ingredient_categories
--   4. warehouse_product_categories
-- =============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------------
-- 1. unit_categories
-- -------------------------------------------------------------
INSERT INTO unit_categories (id, category) VALUES
  (1, 'Weight'),
  (2, 'Volume'),
  (3, 'Piece'),
  (4, 'Packaging');

-- -------------------------------------------------------------
-- 2. units
-- unit_category_id: 1=Weight, 2=Volume, 3=Piece, 4=Packaging
-- -------------------------------------------------------------
INSERT INTO units (id, unit, abbreviation, unit_category_id) VALUES
  -- Weight
  (1,  'milligram', 'mg',  1),
  (2,  'gram',      'g',   1),
  (3,  'kilogram',  'kg',  1),
  -- Volume
  (4,  'milliliter', 'ml',  2),
  (5,  'centiliter', 'cl',  2),
  (6,  'deciliter',  'dl',  2),
  (7,  'liter',      'l',   2),
  -- Piece
  (8,  'piece',      'pcs', 3),
  (9,  'bunch',      'bch', 3),
  -- Packaging (warehouse only)
  (10, 'pack',       'pck', 4),
  (11, 'box',        'box', 4),
  (12, 'bottle',     'btl', 4),
  (13, 'crate',      'crt', 4),
  (14, 'barrel',     'brl', 4),
  (15, 'tube',       'tub', 4),
  (16, 'bucket',     'bkt', 4),
  (17, 'sack',       'sck', 4);

-- -------------------------------------------------------------
-- 3. recipe_ingredient_categories
--
-- id 1–17   roots (parent_category_id = NULL)
-- id 101+   subcategories
--
--  1  Meat and poultry
--     101 Beef
--     102 Pork
--     103 Lamb and goat
--     104 Chicken
--     105 Turkey
--     106 Duck and goose
--     107 Game
--  2  Fish and seafood
--     201 Fish (marine)
--     202 Fish (freshwater)
--     203 Crustaceans
--     204 Mollusks
--  3  Sausages and processed meat       (no subcategories)
--  4  Eggs and dairy products
--     401 Eggs
--     402 Milk and cream
--     403 Butter and margarine
--     404 Cheeses
--     405 Yogurt and fermented products
--  5  Vegetables
--     501 Leafy and greens
--     502 Root vegetables
--     503 Fruit vegetables
--     504 Onion type
--     505 Legumes (fresh)
--     506 Mushrooms
--  6  Fruits
--     601 Fresh fruits
--     602 Dried fruits
--     603 Nuts and seeds
--  7  Grains and starches
--     701 Flours
--     702 Rice and grains
--     703 Pasta
--     704 Bread and bakery products
--     705 Starch and thickeners
--  8  Legumes (dry)                     (no subcategories)
--  9  Fats and oils
--     901 Vegetable oils
--     902 Animal fats
-- 10  Spices and aromatic herbs
--     1001 Dry spices
--     1002 Fresh herbs
--     1003 Spice blends
-- 11  Sauces, pastes and concentrates
--     1101 Tomato products
--     1102 Broths and stocks
--     1103 Pastes
--     1104 Ready-made sauces
-- 12  Sugar and sweeteners              (no subcategories)
-- 13  Confectionery and dessert products
--     1301 Chocolate and cocoa
--     1302 Gelling agents
--     1303 Vanilla and flavors
-- 14  Alcohol (for cooking)
--     1401 Wine
--     1402 Beer
--     1403 Spirits
-- 15  Non-alcoholic beverages (for cooking)
--     1501 Juices and purees
--     1502 Carbonated drinks
-- 16  Canned food and pickles           (no subcategories)
-- 17  Other                             (no subcategories)
-- -------------------------------------------------------------
INSERT INTO recipe_ingredient_categories (id, category, parent_category_id) VALUES
  -- Roots
  (1,    'Meat and poultry',                      NULL),
  (2,    'Fish and seafood',                      NULL),
  (3,    'Sausages and processed meat',           NULL),
  (4,    'Eggs and dairy products',               NULL),
  (5,    'Vegetables',                            NULL),
  (6,    'Fruits',                                NULL),
  (7,    'Grains and starches',                   NULL),
  (8,    'Legumes (dry)',                         NULL),
  (9,    'Fats and oils',                         NULL),
  (10,   'Spices and aromatic herbs',             NULL),
  (11,   'Sauces, pastes and concentrates',       NULL),
  (12,   'Sugar and sweeteners',                  NULL),
  (13,   'Confectionery and dessert products',    NULL),
  (14,   'Alcohol (for cooking)',                 NULL),
  (15,   'Non-alcoholic beverages (for cooking)', NULL),
  (16,   'Canned food and pickles',               NULL),
  (17,   'Other',                                 NULL),
  -- Meat and poultry
  (101,  'Beef',                                  1),
  (102,  'Pork',                                  1),
  (103,  'Lamb and goat',                         1),
  (104,  'Chicken',                               1),
  (105,  'Turkey',                                1),
  (106,  'Duck and goose',                        1),
  (107,  'Game',                                  1),
  -- Fish and seafood
  (201,  'Fish (marine)',                         2),
  (202,  'Fish (freshwater)',                     2),
  (203,  'Crustaceans',                           2),
  (204,  'Mollusks',                              2),
  -- Eggs and dairy products
  (401,  'Eggs',                                  4),
  (402,  'Milk and cream',                        4),
  (403,  'Butter and margarine',                  4),
  (404,  'Cheeses',                               4),
  (405,  'Yogurt and fermented products',         4),
  -- Vegetables
  (501,  'Leafy and greens',                      5),
  (502,  'Root vegetables',                       5),
  (503,  'Fruit vegetables',                      5),
  (504,  'Onion type',                            5),
  (505,  'Legumes (fresh)',                       5),
  (506,  'Mushrooms',                             5),
  -- Fruits
  (601,  'Fresh fruits',                          6),
  (602,  'Dried fruits',                          6),
  (603,  'Nuts and seeds',                        6),
  -- Grains and starches
  (701,  'Flours',                                7),
  (702,  'Rice and grains',                       7),
  (703,  'Pasta',                                 7),
  (704,  'Bread and bakery products',             7),
  (705,  'Starch and thickeners',                 7),
  -- Fats and oils
  (901,  'Vegetable oils',                        9),
  (902,  'Animal fats',                           9),
  -- Spices and aromatic herbs
  (1001, 'Dry spices',                            10),
  (1002, 'Fresh herbs',                           10),
  (1003, 'Spice blends',                          10),
  -- Sauces, pastes and concentrates
  (1101, 'Tomato products',                       11),
  (1102, 'Broths and stocks',                     11),
  (1103, 'Pastes',                                11),
  (1104, 'Ready-made sauces',                     11),
  -- Confectionery and dessert products
  (1301, 'Chocolate and cocoa',                   13),
  (1302, 'Gelling agents',                        13),
  (1303, 'Vanilla and flavors',                   13),
  -- Alcohol (for cooking)
  (1401, 'Wine',                                  14),
  (1402, 'Beer',                                  14),
  (1403, 'Spirits',                               14),
  -- Non-alcoholic beverages (for cooking)
  (1501, 'Juices and purees',                     15),
  (1502, 'Carbonated drinks',                     15);

-- -------------------------------------------------------------
-- 4. warehouse_product_categories
--
-- id 1–8    roots
-- id 11–83  subcategories
-- -------------------------------------------------------------
INSERT INTO warehouse_product_categories (id, category, parent_category_id) VALUES
  -- Roots
  (1,  'Fresh products (short shelf life)', NULL),
  (2,  'Frozen products',                  NULL),
  (3,  'Dry goods (long shelf life)',       NULL),
  (4,  'Canned food and jars',              NULL),
  (5,  'Oils and fats',                     NULL),
  (6,  'Beverages',                         NULL),
  (7,  'Bakery and confectionery products', NULL),
  (8,  'Disposables and consumables',       NULL),
  -- Fresh products
  (11, 'Meat and poultry (fresh)',          1),
  (12, 'Fish and seafood (fresh)',          1),
  (13, 'Dairy products',                    1),
  (14, 'Eggs',                              1),
  (15, 'Fresh fruits and vegetables',       1),
  (16, 'Fresh herbs',                       1),
  -- Frozen products
  (21, 'Frozen meat and poultry',           2),
  (22, 'Frozen fish and seafood',           2),
  (23, 'Frozen vegetables and fruits',      2),
  (24, 'Frozen semi-finished products',     2),
  -- Dry goods
  (31, 'Flours and starches',               3),
  (32, 'Rice, grains and legumes',          3),
  (33, 'Pasta',                             3),
  (34, 'Sugar and sweeteners',              3),
  (35, 'Dry spices and herbs',              3),
  (36, 'Nuts and seeds',                    3),
  -- Canned food and jars
  (41, 'Canned meat and fish',              4),
  (42, 'Canned vegetables and fruits',      4),
  (43, 'Tomato products',                   4),
  (44, 'Sauces and pastes',                 4),
  -- Beverages
  (61, 'Alcoholic beverages',               6),
  (62, 'Non-alcoholic beverages',           6),
  (63, 'Juices',                            6),
  (64, 'Water',                             6),
  -- Disposables and consumables
  (81, 'Packaging materials',               8),
  (82, 'Cleaning agents',                   8),
  (83, 'Hygiene supplies',                  8);

-- -------------------------------------------------------------
-- products
-- Columns: id, name, notes, active
-- -------------------------------------------------------------
INSERT INTO products (id, name, active) VALUES
  -- Meat and poultry
  (1,  'Chicken fillet',           TRUE),
  (2,  'Minced beef',              TRUE),
  (3,  'Pork tenderloin',          TRUE),
  (4,  'Chicken leg',              TRUE),
  -- Fish and seafood
  (5,  'Salmon (fillet)',          TRUE),
  (6,  'Shrimp',                   TRUE),
  -- Sausages and processed meat
  (7,  'Bakone',                   TRUE),
  (8,  'Ham',                      TRUE),
  -- Eggs and dairy products
  (9,  'Eggs',                     TRUE),
  (10, 'Fresh milk',               TRUE),
  (11, 'Heavy cream 35%',          TRUE),
  (12, 'Butter',                   TRUE),
  (13, 'Kashkaval (Yellow cheese)',TRUE),
  (14, 'White cheese',             TRUE),
  (15, 'Yogurt',                   TRUE),
  -- Vegetables
  (16, 'Tomatoes',                 TRUE),
  (17, 'Peppers',                  TRUE),
  (18, 'Spinach',                  TRUE),
  (19, 'Carrots',                  TRUE),
  (20, 'Potatoes',                 TRUE),
  (21, 'Onions',                   TRUE),
  (22, 'Garlic',                   TRUE),
  (23, 'Mushrooms',                TRUE),
  -- Fruits
  (24, 'Lemons',                   TRUE),
  (25, 'Apples',                   TRUE),
  (26, 'Raisins',                  TRUE),
  (27, 'Walnuts',                  TRUE),
  -- Grains and starches
  (28, 'Flour type 500',           TRUE),
  (29, 'Flour type 1150',          TRUE),
  (30, 'Rice',                     TRUE),
  (31, 'Spaghetti',                TRUE),
  (32, 'Bread',                    TRUE),
  (33, 'Starch',                   TRUE),
  -- Legumes (dry)
  (34, 'Lentils',                  TRUE),
  (35, 'Chickpeas',                TRUE),
  -- Fats and oils
  (36, 'Sunflower oil',            TRUE),
  (37, 'Olive oil',                TRUE),
  -- Spices
  (38, 'Salt',                     TRUE),
  (39, 'Black pepper (ground)',    TRUE),
  (40, 'Paprika',                  TRUE),
  (41, 'Cumin',                    TRUE),
  (42, 'Oregano',                  TRUE),
  (43, 'Fresh parsley',            TRUE),
  (44, 'Fresh dill',               TRUE),
  -- Sauces, pastes and concentrates
  (45, 'Tomato puree',             TRUE),
  (46, 'Tomato paste',             TRUE),
  (47, 'Chicken broth',            TRUE),
  (48, 'Beef broth',               TRUE),
  (49, 'Soy sauce',                TRUE),
  -- Sugar and sweeteners
  (50, 'White sugar',              TRUE),
  (51, 'Powdered sugar',           TRUE),
  (52, 'Honey',                    TRUE),
  -- Confectionery and dessert
  (53, 'Dark chocolate 70%',       TRUE),
  (54, 'Cocoa powder',             TRUE),
  (55, 'Gelatin',                  TRUE),
  (56, 'Vanilla (extract)',        TRUE),
  -- Alcohol for cooking
  (57, 'White wine (for cooking)', TRUE),
  (58, 'Red wine (for cooking)',   TRUE),
  -- Canned food and pickles
  (59, 'Canned tomatoes',          TRUE),
  (60, 'Canned corn',              TRUE);

-- -------------------------------------------------------------
-- products_recipe_ingredient_categories
--
-- Used categories (leaves only):
--   101–107  subcategories of Meat and poultry
--   201–204  subcategories of Fish and seafood
--   3        Sausages and processed meat (no subcategories)
--   401–405  subcategories of Eggs and dairy products
--   501–506  subcategories of Vegetables
--   601–603  subcategories of Fruits
--   701–705  subcategories of Grains and starches
--   8        Legumes (dry) (no subcategories)
--   901–902  subcategories of Fats and oils
--   1001–1003 subcategories of Spices
--   1101–1104 subcategories of Sauces, pastes and concentrates
--   12       Sugar and sweeteners (no subcategories)
--   1301–1303 subcategories of Confectionery and dessert
--   1401–1403 subcategories of Alcohol
--   16       Canned food and pickles (no subcategories)
-- -------------------------------------------------------------
INSERT INTO products_recipe_ingredient_categories
  (product_id, recipe_ingredient_category_id) VALUES
  -- Chicken fillet, Chicken leg → Chicken (104)
  (1,  104),
  (4,  104),
  -- Minced beef → Beef (101)
  (2,  101),
  -- Pork tenderloin → Pork (102)
  (3,  102),
  -- Salmon → Fish (marine) (201)
  (5,  201),
  -- Shrimp → Crustaceans (203)
  (6,  203),
  -- Bacon, Ham → Sausages and processed meat (3)
  (7,  3),
  (8,  3),
  -- Eggs → Eggs (401)
  (9,  401),
  -- Fresh milk, Heavy cream → Milk and cream (402)
  (10, 402),
  (11, 402),
  -- Butter → Butter and margarine (403)
  (12, 403),
  -- Yellow cheese, White cheese → Cheeses (404)
  (13, 404),
  (14, 404),
  -- Yogurt → Yogurt and fermented products (405)
  (15, 405),
  -- Tomatoes, Peppers → Fruit vegetables (503)
  (16, 503),
  (17, 503),
  -- Spinach → Leafy and greens (501)
  (18, 501),
  -- Carrots, Potatoes → Root vegetables (502)
  (19, 502),
  (20, 502),
  -- Onions, Garlic → Onion type (504)
  (21, 504),
  (22, 504),
  -- Mushrooms → Mushrooms (506)
  (23, 506),
  -- Lemons, Apples → Fresh fruits (601)
  (24, 601),
  (25, 601),
  -- Raisins → Dried fruits (602)
  (26, 602),
  -- Walnuts → Nuts and seeds (603)
  (27, 603),
  -- Flour 500, Flour 1150 → Flours (701)
  (28, 701),
  (29, 701),
  -- Rice → Rice and grains (702)
  (30, 702),
  -- Spaghetti → Pasta (703)
  (31, 703),
  -- Bread → Bread and bakery products (704)
  (32, 704),
  -- Starch → Starch and thickeners (705)
  (33, 705),
  -- Lentils, Chickpeas → Legumes (dry) (8)
  (34, 8),
  (35, 8),
  -- Sunflower oil, Olive oil → Vegetable oils (901)
  (36, 901),
  (37, 901),
  -- Salt, Black pepper, Paprika, Cumin, Oregano → Dry spices (1001)
  (38, 1001),
  (39, 1001),
  (40, 1001),
  (41, 1001),
  (42, 1001),
  -- Fresh parsley, Fresh dill → Fresh herbs (1002)
  (43, 1002),
  (44, 1002),
  -- Tomato puree, Tomato paste → Tomato products (1101)
  (45, 1101),
  (46, 1101),
  -- Chicken broth, Beef broth → Broths and stocks (1102)
  (47, 1102),
  (48, 1102),
  -- Soy sauce → Ready-made sauces (1104)
  (49, 1104),
  -- White sugar, Powdered sugar, Honey → Sugar and sweeteners (12)
  (50, 12),
  (51, 12),
  (52, 12),
  -- Dark chocolate, Cocoa → Chocolate and cocoa (1301)
  (53, 1301),
  (54, 1301),
  -- Gelatin → Gelling agents (1302)
  (55, 1302),
  -- Vanilla extract → Vanilla and flavors (1303)
  (56, 1303),
  -- White wine, Red wine → Wine (1401)
  (57, 1401),
  (58, 1401),
  -- Canned tomatoes → Tomato products (1101) + Canned food and pickles (16)
  (59, 1101),
  (59, 16),
  -- Canned corn → Canned food and pickles (16)
  (60, 16);

-- -------------------------------------------------------------
-- products_warehouse_categories
--
-- Used categories (leaves only):
--   11  Meat and poultry (fresh)
--   12  Fish and seafood (fresh)
--   13  Dairy products
--   14  Eggs
--   15  Fresh fruits and vegetables
--   16  Fresh herbs
--   22  Frozen fish and seafood
--   31  Flours and starches
--   32  Rice, grains and legumes
--   33  Pasta
--   34  Sugar and sweeteners
--   35  Dry spices and herbs
--   36  Nuts and seeds
--   42  Canned vegetables and fruits
--   43  Tomato products
--   44  Sauces and pastes
--   5   Oils and fats
--   61  Alcoholic beverages
--   7   Bakery and confectionery products
-- -------------------------------------------------------------
INSERT INTO products_warehouse_categories
  (product_id, warehouse_product_category_id) VALUES
  -- Chicken fillet, Minced beef, Pork, Chicken leg,
  -- Bacon, Ham → Meat and poultry (fresh) (11)
  (1,  11),
  (2,  11),
  (3,  11),
  (4,  11),
  (7,  11),
  (8,  11),
  -- Salmon → Fish and seafood (fresh) (12)
  (5,  12),
  -- Shrimp → Fish (fresh) (12) + Frozen fish (22)
  (6,  12),
  (6,  22),
  -- Eggs → Eggs (14)
  (9,  14),
  -- Fresh milk, Heavy cream, Butter, Yellow cheese,
  -- White cheese, Yogurt → Dairy products (13)
  (10, 13),
  (11, 13),
  (12, 13),
  (13, 13),
  (14, 13),
  (15, 13),
  -- Tomatoes, Peppers, Spinach, Carrots, Potatoes,
  -- Onions, Garlic, Mushrooms, Lemons, Apples
  -- → Fresh fruits and vegetables (15)
  (16, 15),
  (17, 15),
  (18, 15),
  (19, 15),
  (20, 15),
  (21, 15),
  (22, 15),
  (23, 15),
  (24, 15),
  (25, 15),
  -- Raisins, Walnuts → Nuts and seeds (36)
  (26, 36),
  (27, 36),
  -- Flour 500, Flour 1150, Starch → Flours and starches (31)
  (28, 31),
  (29, 31),
  (33, 31),
  -- Rice, Lentils, Chickpeas → Rice, grains and legumes (32)
  (30, 32),
  (34, 32),
  (35, 32),
  -- Spaghetti → Pasta (33)
  (31, 33),
  -- Bread → Bakery and confectionery products (7)
  (32, 7),
  -- Sunflower oil, Olive oil → Oils and fats (5)
  (36, 5),
  (37, 5),
  -- Salt, Black pepper, Paprika, Cumin, Oregano
  -- → Dry spices and herbs (35)
  (38, 35),
  (39, 35),
  (40, 35),
  (41, 35),
  (42, 35),
  -- Fresh parsley, Fresh dill → Fresh herbs (16)
  (43, 16),
  (44, 16),
  -- Tomato puree, Tomato paste
  -- → Tomato products (43) + Sauces and pastes (44)
  (45, 43),
  (45, 44),
  (46, 43),
  (46, 44),
  -- Chicken broth, Beef broth, Soy sauce → Sauces and pastes (44)
  (47, 44),
  (48, 44),
  (49, 44),
  -- White sugar, Powdered sugar, Honey → Sugar and sweeteners (34)
  (50, 34),
  (51, 34),
  (52, 34),
  -- Dark chocolate, Cocoa, Gelatin, Vanilla extract
  -- → Bakery and confectionery products (7)
  (53, 7),
  (54, 7),
  (55, 7),
  (56, 7),
  -- White wine, Red wine → Alcoholic beverages (61)
  (57, 61),
  (58, 61),
  -- Canned tomatoes
  -- → Tomato products (43) + Canned vegetables and fruits (42)
  (59, 43),
  (59, 42),
  -- Canned corn → Canned vegetables and fruits (42)
  (60, 42);

-- -------------------------------------------------------------
-- Users and their roles
-- -------------------------------------------------------------
INSERT INTO users (email, phone_number, password, password_changed_at, username, full_name) VALUES
    ('remy@gusteaus.fr',     '+33100000002', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'remy',          'Remy'),               -- 2
    ('linguini@gusteaus.fr', '+33100000003', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'linguini',      'Alfredo Linguini'),   -- 3
    ('colette@gusteaus.fr',  '+33100000004', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'colette',       'Colette Tatou'),      -- 4
    ('gusteau@gusteaus.fr',  '+33100000005', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'gusteau',       'Auguste Gusteau'),    -- 5
    ('skinner@gusteaus.fr',  '+33100000006', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'skinner',       'Skinner'),            -- 6
    ('ego@latourdargent.fr', '+33100000007', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'anton_ego',     'Anton Ego'),          -- 7
    ('horst@gusteaus.fr',    '+33100000008', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'horst',         'Horst'),              -- 8
    ('lalo@gusteaus.fr',     '+33100000009', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'lalo',          'Lalo'),               -- 9
    ('larousse@gusteaus.fr', '+33100000010', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'larousse',      'Larousse'),           -- 10
    ('mustafa@gusteaus.fr',  '+33100000011', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'mustafa',       'Mustafa'),            -- 11
    ('django@colony.fr',     '+33100000012', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'django',        'Django'),             -- 12
    ('emile@colony.fr',      '+33100000013', '$2a$12$lhXVFbiWoa3crS6iEMTAcuEHOzppwGHk79RYCkfb46AtNcsF/pyhi', CURRENT_TIMESTAMP, 'emile',         'Emile');              -- 13

INSERT INTO users_roles (user_id, role_id) VALUES
    (2,  3),        -- Remy – chef
    (3,  2),        -- Linguini – line cook
    (4,  2),        -- Colette – line cook
    (5,  3),        -- Gusteau – chef
    (6,  3),        -- Skinner – chef
    (6,  6),        -- Skinner – manager
    (7,  6),        -- Anton Ego – manager
    (8,  3),        -- Horst – chef
    (9,  4),        -- Lalo – kitchen staff
    (10, 4),        -- Larousse – kitchen staff
    (11, 4),        -- Mustafa – kitchen staff
    (12, 5),        -- Django – warehouse manager
    (13, 4),        -- Emile – kitchen staff
    (13, 5);        -- Emile – warehouse manager

SET FOREIGN_KEY_CHECKS = 1;