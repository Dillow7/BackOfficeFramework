CREATE TABLE hotel (
  id_hotel INT PRIMARY KEY,
  nom VARCHAR(100) NOT NULL
);

CREATE TABLE reservation (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_client VARCHAR(4) NOT NULL,
  nb_passager INT NOT NULL,
  date_heure_arrive DATETIME NOT NULL,
  id_hotel INT NOT NULL,
  CONSTRAINT fk_reservation_hotel
    FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel)
);
