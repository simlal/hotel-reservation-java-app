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
    nom varchar(255) NOT NULL,
    typeLit varchar(255) NOT NULL,
    prixBase int CHECK(prixBase > 0) NOT NULL
);

-- Table Reservation
CREATE TABLE Reservation(
    dateDebut date NOT NULL,
    dateFin date NOT NULL,
    idClient int,
    idChambre int,
    PRIMARY KEY (idClient, idChambre),
    FOREIGN KEY (idClient) REFERENCES Client(idClient) ON DELETE CASCADE,
    FOREIGN KEY (idChambre) REFERENCES Chambre(idChambre) ON DELETE CASCADE
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
    FOREIGN KEY (idChambre) REFERENCES Chambre(idChambre) ON DELETE CASCADE,
    FOREIGN KEY (idCommodite) REFERENCES Commodite(idCommodite) ON DELETE CASCADE
);

-- Table Utilisateurs
CREATE TABLE Utilisateurs(
    utilisateurName varchar(63),
    motDePasse varchar(63),
    acces int,
    nom varchar(255),
    telephone bigint
);
INSERT INTO Utilisateurs (utilisateurName, motDePasse, acces, nom, telephone) values ('AubergeInnAdmin', 'admin', 0, 'Administrateur', 5551234567);
