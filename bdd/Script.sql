CREATE TABLE hotel (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE client (
    id CHAR(4) PRIMARY KEY,                 -- ex: A123, C001
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    idClient CHAR(4) NOT NULL,
    nbPassager INT CHECK (nbPassager > 0),
    dateHeureArrive TIMESTAMP NOT NULL,
    idHotel INT NOT NULL,

    FOREIGN KEY (idClient) REFERENCES client(id),
    FOREIGN KEY (idHotel) REFERENCES hotel(id)
);
