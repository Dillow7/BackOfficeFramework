-- Initialisation DB MySQL (Sprint 3)
CREATE DATABASE IF NOT EXISTS hotel;
USE hotel;

CREATE TABLE IF NOT EXISTS hotel (
  id_hotel INT PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  code VARCHAR(20) NOT NULL,
  aeroport TINYINT(1) NOT NULL DEFAULT 0,
  CONSTRAINT uq_hotel_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS reservation (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_client VARCHAR(4) NOT NULL,
  nb_passager INT NOT NULL,
  date_heure_arrive DATETIME NOT NULL,
  id_hotel INT NOT NULL,
  CONSTRAINT fk_reservation_hotel
    FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel)
);

CREATE TABLE IF NOT EXISTS vehicule (
  id INT AUTO_INCREMENT PRIMARY KEY,
  reference VARCHAR(50) NOT NULL,
  nb_place INT NOT NULL,
  type_carburant ENUM('Diesel', 'Essence', 'Hybride', 'ES') NOT NULL
);

CREATE TABLE IF NOT EXISTS token (
  id INT AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(20) NOT NULL UNIQUE,
  date_expiration DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS distance (
  code_from VARCHAR(20) NOT NULL,
  code_to VARCHAR(20) NOT NULL,
  valeur_km DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (code_from, code_to),
  CONSTRAINT fk_distance_from FOREIGN KEY (code_from) REFERENCES hotel(code),
  CONSTRAINT fk_distance_to FOREIGN KEY (code_to) REFERENCES hotel(code)
);

INSERT INTO hotel (id_hotel, nom, code, aeroport) VALUES
  (1, 'Colbert', 'HOT_COLBERT', 0),
  (2, 'Novotel', 'HOT_NOVOTEL', 0),
  (3, 'Ibis', 'HOT_IBIS', 0),
  (4, 'Lokanga', 'HOT_LOKANGA', 0),
  (101, 'Aéroport Ivato', 'TNR', 1),
  (102, 'Aéroport Tamatave', 'TMM', 1)
ON DUPLICATE KEY UPDATE
  nom = VALUES(nom),
  aeroport = VALUES(aeroport);

INSERT INTO distance (code_from, code_to, valeur_km) VALUES
  ('TNR', 'HOT_COLBERT', 16.5),
  ('TNR', 'HOT_NOVOTEL', 18.0),
  ('TNR', 'HOT_IBIS', 14.2),
  ('TNR', 'HOT_LOKANGA', 20.3),
  ('TMM', 'HOT_COLBERT', 280.0),
  ('TMM', 'HOT_NOVOTEL', 277.0),
  ('TMM', 'HOT_IBIS', 282.0),
  ('TMM', 'HOT_LOKANGA', 279.0)
ON DUPLICATE KEY UPDATE
  valeur_km = VALUES(valeur_km);
