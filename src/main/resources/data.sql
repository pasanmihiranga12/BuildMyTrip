-- Destinations (LKR, per person per day)
INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 1 AS id, 'Sigiriya Rock Fortress' AS name, 'Matale' AS location,
       'Ancient rock fortress with panoramic views and 5th-century frescoes.' AS description,
       'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Sigiriya_%28141688197%29.jpeg/330px-Sigiriya_%28141688197%29.jpeg' AS image_url,
       8500.00 AS base_price) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 1);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 2, 'Galle Fort', 'Galle',
       'Dutch colonial fort by the Indian Ocean with cobbled streets and ramparts.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Galle_Fort.jpg/330px-Galle_Fort.jpg', 4500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 2);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 3, 'Ella Nine Arches', 'Ella',
       'Iconic stone viaduct in lush hill country; misty trains and tea estates.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/The_Nine_Arches_Bridge.jpg/330px-The_Nine_Arches_Bridge.jpg', 5500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 3);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 4, 'Yala National Park', 'Yala',
       'Wildlife safari home to leopards, elephants, sloth bears and rare birds.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Yala_Beach.jpg/330px-Yala_Beach.jpg', 12000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 4);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 5, 'Temple of the Tooth', 'Kandy',
       'Sacred Buddhist temple in Kandy housing the relic of the tooth of the Buddha.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/SL_Kandy_asv2020-01_img33_Sacred_Tooth_Temple.jpg/330px-SL_Kandy_asv2020-01_img33_Sacred_Tooth_Temple.jpg', 6500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 5);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 6, 'Nuwara Eliya', 'Central Province',
       'Cool hill-country town wrapped in tea plantations — known as "Little England".',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c6/NuwaraEliya_from_top.jpg/330px-NuwaraEliya_from_top.jpg', 7500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 6);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 7, 'Mirissa Beach', 'Mirissa',
       'Crescent of golden sand on the south coast — surf, palm trees, blue whales offshore.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Mirissa-Plage_%283%29.jpg/330px-Mirissa-Plage_%283%29.jpg', 5000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 7);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 8, 'Polonnaruwa Ancient City', 'North Central Province',
       'Medieval royal capital — palaces, stupas and colossal carved Buddhas in stone.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Polonnaruwa_01.jpg/330px-Polonnaruwa_01.jpg', 6000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 8);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 9, 'Pinnawala Elephant Orphanage', 'Sabaragamuwa',
       'Sanctuary for orphaned and injured elephants — feedings and river bathing.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Pinnawala_01.jpg/330px-Pinnawala_01.jpg', 4500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 9);

INSERT INTO destinations (id, name, location, description, image_url, base_price)
SELECT * FROM (SELECT 10, 'Adam''s Peak (Sri Pada)', 'Ratnapura',
       'Sacred mountain pilgrimage — pre-dawn climb to a footprint shrine and sunrise.',
       'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Sri_Pada.JPG/330px-Sri_Pada.JPG', 5500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 10);

-- Activities (LKR, per person)
INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 1 AS id, 'Whale Watching' AS name, 12000.00 AS price_per_person) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 1);

INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 2, 'Tea Factory Tour', 3500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 2);

INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 3, 'Surfing Lesson', 6000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 3);

INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 4, 'Cooking Class', 5000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 4);

INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 5, 'Ayurvedic Spa Session', 8500.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 5);

INSERT INTO activities (id, name, price_per_person)
SELECT * FROM (SELECT 6, 'Hot Air Balloon (Sigiriya)', 45000.00) AS t
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE id = 6);

-- Promo codes
INSERT INTO promo_codes (id, code, discount_percent, active)
SELECT * FROM (SELECT 1 AS id, 'WELCOME10' AS code, 10 AS discount_percent, TRUE AS active) AS t
WHERE NOT EXISTS (SELECT 1 FROM promo_codes WHERE id = 1);

INSERT INTO promo_codes (id, code, discount_percent, active)
SELECT * FROM (SELECT 2, 'SUMMER25', 25, TRUE) AS t
WHERE NOT EXISTS (SELECT 1 FROM promo_codes WHERE id = 2);

-- Admin user is created at deploy time via POST /api/auth/register + UPDATE role,
-- so we don't seed it here (BCrypt hashes can't be hand-written).
