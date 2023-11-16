# Hotel reservation app in Java with different database solutions

Build a simple Java app with a CLI frontend for PostgreSQL, ObjectDB and MongoDB versions. Add a static frontend for one of those lastly.

## Project structure

Find the sub-projects inside `hotel-res-app/` with the name according to the db stack used. No project manager was used except for Maven in the MongoDB app.

Libs aren't provided so you would need to fetch the proper  JDBC driver (4.2), postgresql(11.2) and objectdb (2.8.8) .jar files. For MongoDB, see the pom.xml file.

## Overview

You can either work in a local server/db or a remote. See each `Connexion.java` for details on how to connect.

CLI interface for pgsql, odb and mongodb accepts either interactive commands or a .txt file with a single command on each line (// for non executable lines). Connection to the db is ended when providing a `quitter` command.

### Example run with ObjectDB
1. Clone and enter the project
```bash
git clone https://github.com/simlal/hotel-reservation-java-app.git
cd hotel-reservation-java-app/hotel-res-app/objectdb-app/TP3/
```

2. Install the objectdb jar
```bash
mkdir lib && cd lib
curl -L -O https://www.objectdb.com/get-file/b633d1af-e7a0-4fcc-93fe-09d7bbcf496a/objectdb-2.8.8.zip
unzip objectdb-2.8.8.zip && rm objectdb-2.8.8.zip
# Go back to root
cd ../../
```

3. Compile and run the main
```bash
javac -cp .:TP3/lib/objectdb-2.8.8/bin/objectdb.jar TP3/src/AubergeInn/*.java
java -cp .:TP3/src/:TP3/lib/objectdb-2.8.8/bin/objectdb.jar AubergeInn.AubergeInn
Usage: java AubergeInn.AubergeInn <serveur> <bd> <user> <password> [<fichier-transactions>]

# For local .odb server enter
mkdir -p db/ && touch db/odb
OBJECTDB_PATH=$(pwd)/db/mydb.odb
if [[ -e $OBJECTDB_PATH ]]; then echo "$(basename $OBJECTDB_PATH)" created; fi

# Example run for local testing to enter CLI
java -cp .:TP3/src/:TP3/lib/objectdb-2.8.8/bin/objectdb.jar AubergeInn.AubergeInn local $OBJECTDB_PATH myusername mypassword
```
4. CRUD operations examples

```bash
# Add a client
ajouterClient John Smith 25      
> Ajout client idClient=1 avec succes.

# Add a chamber
ajouterChambre LaCarotte Double 75
> Ajout chambre idChambre=2 avec succes.

# Add and include/remove amenities
ajouterCommodite MiniBar 10
> Ajout commodite idCommodite=3 avec succes.

ajouterCommodite Tele 25
> Ajout commodite idCommodite=4 avec succes.

inclureCommodite 2 3
> Inclusion de la commodite MiniBar dans la chambre LaCarotte avec succes.

inclureCommodite 2 4
> Inclusion de la commodite Tele dans la chambre LaCarotte avec succes.

# Print the state of a chamber
afficherChambre 2
> Chambre: LaCarotte
        idChambre: 2
        Type de lit: Double
        Prix de base: 75$
        Commodites: MiniBar=10$, Tele=25$
        Prix total: 110$


enleverCommodite 2 3
> Suppression de la commodite MiniBar dans la chambre LaCarotte avec succes.

afficherChambre 2
> Chambre: LaCarotte
        idChambre: 2
        Type de lit: Double
        Prix de base: 75$
        Commodites: Tele=25$
        Prix total: 100$

# Reserve a chamber for a client
reserver 1 2 2023-12-01 2023-12-15
> Reservation de la chambre LaCarotte pour le client Smith du 2023-12-01 au 2023-12-15 avec succes.

# Print available chambers for a period
ajouterChambre LePoisChiche Simple 60
> Ajout chambre idChambre=6 avec succes.

afficherChambresLibres 2023-12-05 2023-12-20
> Chambre: LePoisChiche
        idChambre: 6
        Type de lit: Simple
        Commodites: Aucune commodite
        Prix total: 60

# Print a client's info and reservations
afficherClient 1
> Informations client: 
        idClient: 1
        Prenom: John
        Nom: Smith
        Age: 25
> Reservations associees: 
        idReservation: 5
                Chambre: LaCarotte
                dateDebut: 2023-12-01
                dateFin: 2023-12-15
                prixTotal: 100$
# Quit
quitter
> Connexion fermée
```