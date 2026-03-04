-- Migration Sprint 3
USE hotel;

ALTER TABLE hotel
    ADD COLUMN IF NOT EXISTS code VARCHAR(20) NULL,
    ADD COLUMN IF NOT EXISTS aeroport TINYINT(1) NOT NULL DEFAULT 0;

UPDATE hotel
SET code = CONCAT('HOTEL_', id_hotel)
WHERE code IS NULL OR code = '';

ALTER TABLE hotel
    MODIFY code VARCHAR(20) NOT NULL;

-- A lancer une seule fois. Si l'index existe deja, ignorer l'erreur.
ALTER TABLE hotel ADD CONSTRAINT uq_hotel_code UNIQUE (code);

CREATE TABLE IF NOT EXISTS distance (
    code_from VARCHAR(20) NOT NULL,
    code_to VARCHAR(20) NOT NULL,
    valeur_km DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (code_from, code_to),
    CONSTRAINT fk_distance_from FOREIGN KEY (code_from) REFERENCES hotel(code),
    CONSTRAINT fk_distance_to FOREIGN KEY (code_to) REFERENCES hotel(code)
);
