package com.ift287lalonde;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
public class AccesClient {

    private Connexion cx;
    private MongoCollection<Document> clientsCollection;


    /**
     * Creation instance representant l'acces a un client
     * 
     * @param cx
     */
    public AccesClient(Connexion cx) {
        this.cx = cx;
        this.clientsCollection = cx.getDatabase().getCollection("clients");

    }

    /**
     * Retourne la connexion associee
     * 
     * @return Connexion
     */
    public Connexion getConnexion() {
        return cx;
    }

    /**
     * 
     * @return MongoCollection<Document>
     */
    public MongoCollection<Document> getClientsCollection() {
        return clientsCollection;
    }

    /**
     * Verif client existe
     * 
     * @param idClient
     * @return boolean
     */
    public boolean clientExiste(int idClient) {
        boolean clientExiste = false;
        if (clientsCollection.find(eq("idClient", idClient)).first() != null) {
            clientExiste = true;
        }
        return clientExiste;
    }

    /**
     * Chercher un client
     * 
     * @param idClient
     * @return TupleClient
     */
    public TupleClient getClient(int idClient) {
        TupleClient client = null;
        if (clientExiste(idClient)) {
            Document clientDoc = clientsCollection.find(eq("idClient", idClient)).first();
            client = new TupleClient(clientDoc);
            return client;
        }
        return client;
    }

    /**
     * Ajout d'un nouveau client
     * 
     * @param client
     */
    public void ajouterClient(TupleClient client) {
        clientsCollection.insertOne(client.toDocument());
    }

    /**
     * Suppression d'un client avec le id
     * 
     * @param idClient
     * @return true si client supprimer
     */
    public boolean supprimerClient(int idClient) {
        boolean clientSupprime = clientsCollection
            .deleteOne(eq("idClient", idClient))
            .getDeletedCount() > 0;
        return clientSupprime;
    }

    // /**
    //  * Cherche un client avec reservations dans db
    //  * 
    //  * @param idClient
    //  * @return boolean
    //  */
    // public boolean checkClientReservationEnCours(int idClient) {
    //     // Chercher les reservations du client
    //     boolean clientAvecReservation = false;
    //     MongoCollection<Document> reservationsCollection = new AccesReservation(getConnexion()).getReservationsCollection();
    //     MongoCursor<Document> reservationsCursor = reservationsCollection.find(eq("idClient", idClient)).iterator();
    //     List<TupleReservation> reservations = new ArrayList<>();
    //     try {
    //         while (reservationsCursor.hasNext()) {
    //             Document reservationDoc = reservationsCursor.next();
    //             TupleReservation reservation = new TupleReservation(reservationDoc);
    //             reservations.add(reservation);
    //         }
    //     } finally {
    //         reservationsCursor.close();
    //     }
    //     // Verifie si la reservation est en cours
    //     Date maintenant = new Date();
    //     for (TupleReservation reservation : reservations) {
    //         if (reservation.getDateDebut().before(maintenant) && reservation.getDateFin().after(maintenant)) {
    //             clientAvecReservation = true;
    //             break;
    //         }
    //     }
    //     return clientAvecReservation;
    
    // }
    

    // /**
    //  * Affiche les informations d'un client avec reservations associees
    //  * 
    //  * @param idClient l'identifiant du client dont les informations doivent être affichees
    //  */
    // public void afficherClient(int idClient) {
    //     // Info base de client
    //     TupleClient client = getClient(idClient);
        
    //     String infoClient = "\nInformations client: " +
    //             "\n\tidClient: " + client.getId() +
    //             "\n\tPrenom: " + client.getPrenom() +
    //             "\n\tNom: " + client.getNom() +
    //             "\n\tAge: " + client.getAge();

    //     // Creer instances de chacunes des collections 
    //     MongoCollection<Document> reservationsCollection = new AccesReservation(getConnexion()).getReservationsCollection();
    //     MongoCollection<Document> chambresCollection = new AccesChambre(getConnexion()).getChambresCollection();
    //     MongoCollection<Document> commoditesCollection = new AccesCommodite(getConnexion()).getCommoditesCollection();

    //     // Chercher les reservations d'un client
    //     MongoCursor<Document> reservationsCursor = reservationsCollection.find(eq("idClient", client.getId()))
    //         .sort(ascending("dateDebut"))
    //         .iterator();
    //     List<TupleReservation> reservations = new ArrayList<>();
    //     try {
    //         while (reservationsCursor.hasNext()) {
    //             Document reservationDoc = reservationsCursor.next();
    //             TupleReservation reservation = new TupleReservation(reservationDoc);
    //             reservations.add(reservation);
    //         }
    //     } finally {
    //         reservationsCursor.close();
    //     }
    //     String infoReservations = "\nReservations associees: ";
    //     if (reservations.isEmpty() || reservations.size() == 0) {
    //         infoReservations = "\nAucune reservation pour ce client";
    //     }

    //     // Chercher les informations de chaque chambre
    //     for (TupleReservation reservation : reservations) {
    //         // info chambre par reserv
    //         MongoCursor<Document> chambresCursor = chambresCollection.find(eq("idReservation", reservation.getId()));
            
    //         // try {
    //             // while (chambresCursor.hasNext()) {
    //                 // Document chambreDoc = chambresCursor.next();
    //                 // TupleChambre chambre = new TupleChambre(chambreDoc);
    //                 // chambres.add(chambre)
    //             // }
    //         }
    //     }
    // }

    //         TupleChambre chambre = reservation.getChambre();
    //         int prixTotal = chambre.getPrixBase();

    //         // Calcul prix avec inclusions commodites par chambre
    //         List<TupleCommodite> commodites = chambre.getCommodites();
    //         if (!commodites.isEmpty()) {
    //             for (TupleCommodite commodite : commodites) {
    //                 prixTotal += commodite.getSurplusPrix();
    //             }
    //         }
    //         // Ajout information chambre/commodites pour prixTotal
    //         infoReservations += "\n\tidReservation: " + reservation.getId() +
    //                 "\n\t\tChambre: " + chambre.getNom() +
    //                 "\n\t\tdateDebut: " + reservation.getDateDebut() +
    //                 "\n\t\tdateFin: " + reservation.getDateFin() +
    //                 "\n\t\tprixTotal: " + prixTotal + "$";
    //     }

    //     // Affichage complet pour client
    //     System.out.println(infoClient + infoReservations);
    // }
}