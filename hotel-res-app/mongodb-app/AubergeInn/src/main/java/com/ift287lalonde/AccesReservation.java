package com.ift287lalonde;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Sorts.ascending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

// import com.ift287lalonde.TupleReservation;

public class AccesReservation {
    
    private Connexion cx;
    private MongoCollection<Document> reservationsCollection;

    public AccesReservation(Connexion cx) {
        this.cx = cx;
        this.reservationsCollection = cx.getDatabase().getCollection("reservations");
    }
    
    /**
     * Cherche la connection associee
     * 
     * @return Connexion
     */
    public Connexion getConnexion() {
        return cx;
    }
    /**
     * Cherche la collection associee
     * 
     * @return MongoCollection<Document>
     */
    public MongoCollection<Document> getReservationsCollection() {
        return reservationsCollection;
    }

    /**
     * Verifie si la reservation existe
     * 
     * @param idReservation
     * @return true si la reservation existe, false sinon
     */
    public boolean reservationExiste(int idReservation) {
        boolean reservationExiste = false;
        if (reservationsCollection.find(eq("idClient", idReservation)).first() != null) {
            reservationExiste = true;
        }
        return reservationExiste;
    }

    /**
     * Cherche la reservation avec l'id fourni
     * 
     * @param idReservation
     * @return TupleReservation
     */
    public TupleReservation getReservation(int idReservation) {
        TupleReservation reservation = null;
        if (reservationExiste(idReservation)) {
            Document reservationDoc = reservationsCollection
                .find(eq("idReservation", idReservation))
                .first();
            reservation = new TupleReservation(reservationDoc);
        }
        return reservation;
    }

    /**
     * Lister les reservations pour un client
     * 
     * @param idClient
     * @return reservations pour le client
     */
    public List<TupleReservation> listerReservationsClient(int idClient) {
        // Chercher les reservations d'un client
        MongoCursor<Document> reservationsCursor = reservationsCollection
            .find(eq("idClient", idClient))
            .iterator();
        
        // Creer la liste
        List<TupleReservation> reservations = new ArrayList<>();
        try {
            while (reservationsCursor.hasNext()) {
                Document reservationDoc = reservationsCursor.next();
                TupleReservation reservation = new TupleReservation(reservationDoc);
                reservations.add(reservation);
            }
        } finally {
            reservationsCursor.close();
        }
        return reservations;
    }
    public boolean clientReservationEnCours(int idClient) {
        // Construction requete client et reservation
        Date maintenant = new Date();
        Bson query = and(
            eq("idClient", idClient),
            lte("dateDebut", maintenant),
            gte("dateFin", maintenant)

        );
        // Verif si reservation correspond a requete
        boolean clientAvecReservationEnCours = false;
        if (reservationsCollection.find(query).first() != null) {
            clientAvecReservationEnCours = true;
        }
        return clientAvecReservationEnCours;
    }

    /**
     * Verifie si la chambre est reservee pour periode specifiee
     * 
     * @param idChambre
     * @param dateDebut
     * @param dateFin
     * @return true si la chambre est reservee, false sinon
     */
    // public boolean checkChambreReserveSpecif(int idChambre, Date dateDebut, Date dateFin) {
    //     // Chercher toutes les reservations pour une chambre
    //     MongoCursor<Document> reservationsCursor = reservationsCollection
    //         .find(eq("idChambre", idChambre))
    //         .iterator();

    //     // Verifier si chambre reserve pour plage demandee
    //     boolean chambreReservee = false;
    //     try {
    //         while (reservationsCursor.hasNext()) {
    //             Document reservationDoc = reservationsCursor.next();
    //             TupleReservation reservation = new TupleReservation(reservationDoc);
    //             if (reservation.getDateDebut().before(dateFin)
    //                     && reservation.getDateFin().after(dateDebut)) {
    //                 chambreReservee = true;
    //                 break;
    //             }
    //         }
    //     } finally {
    //         reservationsCursor.close();
    //     }
    //     return chambreReservee;
    // }
    public boolean checkChambreReserveSpecif(int idChambre, Date dateDebut, Date dateFin) {
        // Requete pour chambre et periode specif
        Bson query = and(
            eq("idChambre", idChambre),
            lt("dateDebut", dateFin),
            gt("dateFin", dateDebut)
        );

        // Verif si reservation existe pour requete
        boolean chambreReservee = false;
        if (reservationsCollection.find(query).first() != null) {
            chambreReservee = true;
        }
        return chambreReservee;
    }

    /**
     * Verifie si une chambre a une reservation en cours ou futur
     * 
     * @param idChambre id de la chambre a verifier
     * @return true si la chambre a une reservation en cours, false sinon
     */
    // public boolean checkChambreReservee(int idChambre, String periode) {
    //     // Verifie quelle periode
    //     List<String> periodesPossibles = new ArrayList<String> ();
    //     periodesPossibles.add("enCours");
    //     periodesPossibles.add("futur");
    //     if (!periodesPossibles.contains(periode)) {
    //         throw new IllegalArgumentException("Periode doit etre 'enCours' ou 'futur");
    //     }

    //     // Chercher la collection reservation
    //     boolean chambreReservee = false;
    //     MongoCursor<Document> reservationsCursor = reservationsCollection.find(eq("idChambre", idChambre))
    //         .sort(ascending("dateDebut"))
    //         .iterator();
    //     // Chambre jamais reservee
    //     if (!reservationsCursor.hasNext()) {
    //         return chambreReservee;
    //     }
    //     // Chercher reservations associe a chambre
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
    //         switch (periode) {
    //             case "enCours":
    //                 if (reservation.getDateDebut().before(maintenant) 
    //                     && reservation.getDateFin().after(maintenant)) {
    //                     chambreReservee = true;
    //                     break;
    //                 }
    //                 break;
    //             case "futur":
    //                 if (reservation.getDateDebut().after(maintenant)) {
    //                     chambreReservee = true;
    //                     break;
    //                 }
    //                 break;
    //         }
    //     }
    //     return chambreReservee;
    // }
    public boolean checkChambreReservee(int idChambre, String periode) {
        // Verifie quelle periode
        List<String> periodesPossibles = new ArrayList<>();
        periodesPossibles.add("enCours");
        periodesPossibles.add("futur");
        
        // Construire la requete en fonction de la periode
        Date maintenant = new Date();
        Bson query = null;
        
        if (periode.equals("enCours")) {
            query = and(
                eq("idChambre", idChambre),
                lte("dateDebut", maintenant),
                gte("dateFin", maintenant)
            );
        } else if (periode.equals("futur")) {
            query = and(
                eq("idChambre", idChambre),
                gt("dateDebut", maintenant)
            );
        } else {
            throw new IllegalArgumentException("Periode doit etre 'enCours' ou 'futur");
        }
        // Verifie si une reservation correspond a requete
        boolean chambreReservee = false;
        if (reservationsCollection.find(query).first() != null) {
            chambreReservee = true;
        }
        return chambreReservee;
}

    /**
     * Lister chambres libres pour une periode specifiee
     * 
     * @param reservation
     */

    /**
     * Ajoute une reservation dans la db
     * 
     * @param reservation
     */
    public void ajouterReservation(TupleReservation reservation) {
        reservationsCollection.insertOne(reservation.toDocument());
    }

    /**
     * Supprime une reservation de la db
     *
     * @param reservation 
     */
    // ?Pas necessaire pour l'instant
    public boolean supprimerReservation(TupleReservation reservation) {
        boolean reservationSupprimee = reservationsCollection
            .deleteOne(eq("idReservation", reservation.getId()))
            .getDeletedCount() > 0;
        return reservationSupprimee;

    }
}
