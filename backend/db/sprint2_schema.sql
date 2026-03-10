-- Table vehicule
CREATE TABLE IF NOT EXISTS vehicule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) NOT NULL,
    nb_place INT NOT NULL,
    type_carburant ENUM('Diesel', 'Essence', 'Hybride', 'ES') NOT NULL
);

-- Table token
CREATE TABLE IF NOT EXISTS token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(20) NOT NULL UNIQUE,
    date_expiration DATETIME NOT NULL
);
