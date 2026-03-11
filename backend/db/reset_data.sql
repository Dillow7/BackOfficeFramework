-- Script pour réinitialiser les données (Sprint 3)
USE hotel;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE distance;
TRUNCATE TABLE reservation;
TRUNCATE TABLE hotel;
TRUNCATE TABLE vehicule;
TRUNCATE TABLE token;
SET FOREIGN_KEY_CHECKS = 1;

-- Lieux (hôtels + aéroports)
INSERT INTO hotel (id_hotel, nom, code, aeroport) VALUES
  (1, 'Colbert', 'HOT_COLBERT', 0),
  (2, 'Novotel', 'HOT_NOVOTEL', 0),
  (3, 'Ibis', 'HOT_IBIS', 0),
  (4, 'Lokanga', 'HOT_LOKANGA', 0),
  (101, 'Aéroport Ivato', 'TNR', 1),
  (102, 'Aéroport Tamatave', 'TMM', 1);

-- Distances (km)
-- HOT_LOKANGA n'a qu'une distance inverse pour tester la symétrie
INSERT INTO distance (code_from, code_to, valeur_km) VALUES
  ('TNR', 'HOT_COLBERT', 16.5),
  ('TNR', 'HOT_NOVOTEL', 18.0),
  ('TNR', 'HOT_IBIS', 14.2),
  ('HOT_LOKANGA', 'TNR', 20.3),
  ('HOT_COLBERT', 'HOT_NOVOTEL', 6.0),
  ('HOT_COLBERT', 'HOT_IBIS', 5.5),
  ('HOT_COLBERT', 'HOT_LOKANGA', 8.0),
  ('HOT_NOVOTEL', 'HOT_IBIS', 0.5),
  ('HOT_NOVOTEL', 'HOT_LOKANGA', 2.0),
  ('HOT_IBIS', 'HOT_LOKANGA', 1.5);


-- Réservations de test (03 mars 2026 + autres dates)
INSERT INTO reservation (id_client, nb_passager, date_heure_arrive, id_hotel) VALUES
  ('4631', 4,  '2026-03-03 09:00:00', 1),
  ('4394', 4,  '2026-03-03 09:20:00', 3),
  ('8054', 2,  '2026-03-03 09:10:00', 4),
  ('1432', 5,  '2026-03-03 09:15:00', 1),
  ('7861', 6,  '2026-03-03 10:00:00', 2),
  ('3308', 12, '2026-03-03 10:30:00', 1),
  ('4484', 10, '2026-03-03 10:45:00', 2),
  ('9687', 13, '2026-03-03 11:00:00', 2),
  ('6302', 3,  '2026-03-04 13:00:00', 1),
  ('8640', 1,  '2026-02-18 22:55:00', 4);

-- Véhicules de test
INSERT INTO vehicule (reference, nb_place, type_carburant) VALUES
  ('CAR-004-D', 4,  'Diesel'),
  ('CAR-005-E', 5,  'Essence'),
  ('CAR-005-D', 5,  'Diesel'),
  ('CAR-007-H', 7,  'Hybride'),
  ('CAR-012-D', 12, 'Diesel'),
  ('CAR-002-ES', 2, 'ES');

-- Vérifications
SELECT COUNT(*) AS nb_hotels FROM hotel;
SELECT COUNT(*) AS nb_distances FROM distance;
SELECT COUNT(*) AS nb_reservations FROM reservation;
SELECT COUNT(*) AS nb_vehicules FROM vehicule;
