-- min/max price, 1 vs 28 nights, apostrophe, null, and one inactive row
INSERT INTO guests (id, firstname, lastname, totalprice, depositpaid, nights, additionalneeds, active) VALUES
 (1, 'Amina',   'Khan',      1,    TRUE,  1,  'Breakfast',        TRUE),
 (2, 'Oliver',  'Bennett',   9999, FALSE, 28, 'Late checkout',    TRUE),
 (3, 'Zainab',  'O''Connor', 450,  TRUE,  3,  'Airport transfer', TRUE),
 (4, 'Hamza',   'Ali-Shah',  120,  FALSE, 2,  NULL,               TRUE),
 (5, 'Retired', 'Record',    100,  TRUE,  1,  'Should not run',   FALSE);
