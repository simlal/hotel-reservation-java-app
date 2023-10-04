-- Creation toutes les tables de la bd
-- Table Client
CREATE TABLE Client (
    idClient int PRIMARY KEY,
    prenom varchar(255) NOT NULL,
    nom varchar(255) NOT NULL,
    age int CHECK(age > 0)
);

-- Table Chambre
CREATE TABLE Chambre(
    idChambre int PRIMARY KEY,
    typeLit varchar(255) NOT NULL,
    prixBase int CHECK(prixBase > 0) NOT NULL
);

-- Table Reservation
CREATE TABLE Reservation(
    idReservation int PRIMARY KEY,
    dateDebut date NOT NULL,
    dateFin date NOT NULL,
    idClient int REFERENCES Client(idClient),
    idChambre int REFERENCES Chambre(idChambre)
);

-- Table Commodite
CREATE TABLE Commodite(
    idCommodite int PRIMARY KEY,
    description varchar(255),
    surplusPrix int CHECK(surplusPrix > 0) NOT NULL
);

-- Table ChambreCommodite
CREATE TABLE ChambreCommodite(
    idChambre int,
    idCommodite int,
    PRIMARY KEY (idChambre, idCommodite),
    FOREIGN KEY (idChambre) REFERENCES Chambre(idChambre),
    FOREIGN KEY (idCommodite) REFERENCES Commodite(idCommodite)
);
