package com.ift287lalonde;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

public class AccesChambre {

    private Connexion cx;
    private MongoCollection<Document> chambresCollection;

    public AccesChambre(Connexion cx) {
        this.cx = cx;
        this.chambresCollection = cx.getDatabase().getCollection("chambres");
    }

    /**
     * Chercher la connexion mongodb
     * 
     * @return
     */
    public Connexion getConnexion() {
        return cx;
    }
    /**
     * Chercher la collection
     * 
     * @return chambresCollection
     */
    public MongoCollection<Document> getChambresCollection() {
        return chambresCollection;
    }

    /**
     * Verifie si une chambre existe dans la base de donnees avec l'identifiant
     * specifie.
     * 
     * @param idChambre l'identifiant de la chambre à verifier
     * @return true si la chambre existe, false sinon
     */
    public boolean chambreExiste(int idChambre) {
        boolean chambreExiste = false;
        if (chambresCollection.find(eq("idChambre", idChambre)).first() != null) {
            chambreExiste = true;
        }
        return chambreExiste;
    }

    /**
     * Chercher la chambre avec id
     * 
     * @param idChambre id de la chambre
     * @return TupleChambre
     */
    public TupleChambre getChambre(int idChambre) {
        TupleChambre chambre = null;
        if (chambreExiste(idChambre)) {
            Document chambreDoc = chambresCollection.find(eq("idChambre", idChambre)).first();
            chambre = new TupleChambre(chambreDoc);
        }
        return chambre;
    }

    /**
     * lister toutes les chambres
     * 
     * @return chambres
     */
    public List<TupleChambre> listerChambres() {
        List<TupleChambre> chambres = new ArrayList<>();
        MongoCursor<Document> chambresCursor = chambresCollection.find().iterator();
        try {
            while (chambresCursor.hasNext()) {
                Document chambresDoc = chambresCursor.next();
                TupleChambre chambre = new TupleChambre(chambresDoc);
                chambres.add(chambre);
            }
        } finally {
            chambresCursor.close();
        }
        return chambres;
    }

     /**
     * Chercher la chambre associee a reservation
     * 
     * @param idReservation
     * @return
     */
    public TupleChambre getChambreReservation(ObjectId idReservation) {
        // Faire la requete pour chambre avec reservation
        Document chambreDoc = chambresCollection
            .find(in("idReservation", idReservation))
            .first();
        // Creer instance de chambre
        TupleChambre chambre = null;
        if (chambreDoc != null) {
            chambre = new TupleChambre(chambreDoc);
        }
        return chambre;
    }

    /**
     * Chercher les chambres associees a une commodite
     *  
     * @param commodite
     * @return liste de commodites
     */
     public List<TupleChambre> getChambresCommodite(TupleCommodite commodite) {
        // Faire la requete pour chambre avec commodite
        List<TupleChambre> chambres = new ArrayList<>();
        MongoCursor<Document> chambresCursor = chambresCollection
            .find(in("commoditesId", commodite.getId()))
            .iterator();
        try {
            while (chambresCursor.hasNext()) {
                Document chambreDoc = chambresCursor.next();
                TupleChambre chambre = new TupleChambre(chambreDoc);
                chambres.add(chambre);
            }
        } finally {
            chambresCursor.close();
        }
        return chambres;
     }

    /**
     * Ajoute une chambre dans la base de donnees.
     * 
     * @param chambre une instance de chambre
     */
    public void ajouterChambre(TupleChambre chambre) {
        chambresCollection.insertOne(chambre.toDocument());
    }
    public boolean ajouterReservationChambre(
        TupleChambre chambre, 
        ObjectId idReservation
    ) {
        UpdateResult result =  chambresCollection
            .updateOne(
                eq("idChambre", chambre.getId()), 
                set("idReservation", idReservation)
            );
        boolean reservationIncluse = result.getModifiedCount() > 0;
        return reservationIncluse;
    }
    

    /**
     * Supprime une chambre de la base de donnees.
     * 
     * @param idChambre id de la chambre
     * @return true si chambre supprimer
     */
    public boolean supprimerChambre(int idChambre) {
        boolean chambreSupprime = chambresCollection
            .deleteOne(eq("idChambre", idChambre))
            .getDeletedCount() > 0;
        return chambreSupprime;
    }

    /**
     * Faire le lien entre la chambre et la commodite
     * 
     * @param chambre
     * @param commodite
     */
    public boolean inclureCommodite(TupleChambre chambre, TupleCommodite commodite) {
        UpdateResult result = chambresCollection
            .updateOne(
                eq("idChambre", chambre.getId()),
                push("commoditesId", commodite.getId())
            );
        boolean commoditeIncluse = result.getModifiedCount() > 0;
        return commoditeIncluse;
    }

    /**
     * Retirer le lien entre chambre et commodite
     * 
     * @param chambre
     * @param commodite
     */
    public boolean enleverCommodite(TupleChambre chambre, TupleCommodite commodite) {
        UpdateResult result = chambresCollection
            .updateOne(
                eq("idChambre", chambre.getId()),
                pull("commoditesId", commodite.getId())
            );
        boolean commoditeRetiree = result.getModifiedCount() > 0;
        return commoditeRetiree;
    }

    // /**
    //  * Affiche les informations d'une chambre
    //  * 
    //  * @param idChambre l'identifiant de la chambre à afficher
    //  * @throws IllegalArgumentException si la chambre avec id existe pas
    //  */
    // public void afficherChambre(int idChambre) {
    //     // Chercher la chambre + prixBase
    //     TupleChambre chambre = getChambre(idChambre);
    //     int prixTotal = chambre.getPrixBase();

    //     // Chercher commodites associees a chambre
    //     // TODO COMMODITE HERE
    //     List<TupleCommodite> commodites = chambre.getCommodites();

    //     // Representation en string des infos
    //     String infoCommodites = "";
    //     for (TupleCommodite commodite : commodites) {
    //         infoCommodites += commodite.getDescription() + "=";
    //         infoCommodites += commodite.getSurplusPrix() + "$" + ", ";
    //         prixTotal += commodite.getSurplusPrix();
    //     }
    //     // Enlever derniere virgule
    //     if (infoCommodites.endsWith(", ")) {
    //         infoCommodites = infoCommodites.substring(0, infoCommodites.length() - 2);
    //     }
    //     // Pour les chambres sans commodites
    //     if (infoCommodites.isEmpty()) {
    //         infoCommodites = "Aucune commodite";
    //     }

    //     // Afficher informations chambre
    //     System.out.println(
    //             "\nChambre: " + chambre.getNom() +
    //                     "\n\tidChambre: " + chambre.getId() +
    //                     "\n\tType de lit: " + chambre.getTypeLit() +
    //                     "\n\tPrix de base: " + chambre.getPrixBase() + "$" +
    //                     "\n\tCommodites: " + infoCommodites +
    //                     "\n\tPrix total: " + prixTotal + "$");
    // }
    
    // /**
    //  * Affiche les chambres libres pour une periode donnee.
    //  * 
    //  * @param dateDebut La date de debut de la periode.
    //  * @param dateFin   La date de fin de la periode.
    //  */
    // public void afficherChambresLibres(Date dateDebut, Date dateFin) {
    //     // Chercher les chambres avec reservations
    //     List<TupleChambre> chambres = stmtAfficherChambres.getResultList();

    //     // Verifier si chambres libres pour dates donnees
    //     List<TupleChambre> chambresLibres = new ArrayList<TupleChambre>();
    //     for (TupleChambre chambre : chambres) {
    //         List<TupleReservation> reservations = chambre.getReservations();
    //         boolean isFree = true;
    //         for (TupleReservation reservation : reservations) {
    //             if (reservation.getDateDebut().compareTo(dateFin) < 0
    //                     && reservation.getDateFin().compareTo(dateDebut) > 0) {
    //                 isFree = false;
    //                 break;
    //             }
    //         }
    //         if (isFree) {
    //             chambresLibres.add(chambre);
    //         }
    //     }
    //     // affichage en fonction des dates
    //     if (chambresLibres.isEmpty()) {
    //         System.out.println(
    //                 "\nAucune chambre libre pour la periode:" +
    //                         "\n\tDate debut: " + dateDebut +
    //                         "\n\tDate fin: " + dateFin);
    //     }

    //     for (TupleChambre chambre : chambresLibres) {
    //         int prixTotal = chambre.getPrixBase();

    //         // Chercher commodites associees a chambre
    //         List<TupleCommodite> commodites = chambre.getCommodites();

    //         // Representation en string des infos
    //         String infoCommodites = "";
    //         for (TupleCommodite commodite : commodites) {
    //             infoCommodites += commodite.getDescription() + "=";
    //             infoCommodites += commodite.getSurplusPrix() + "$" + ", ";
    //             prixTotal += commodite.getSurplusPrix();
    //         }
    //         // Enlever derniere virgule
    //         if (infoCommodites.endsWith(", ")) {
    //             infoCommodites = infoCommodites.substring(0, infoCommodites.length() - 2);
    //         }
    //         // Pour les chambres sans commodites
    //         if (infoCommodites.isEmpty()) {
    //             infoCommodites = "Aucune commodite";
    //         }

    //         // Afficher informations chambres libres
    //         System.out.println(
    //                 "\nChambre: " + chambre.getNom() +
    //                         "\n\tidChambre: " + chambre.getId() +
    //                         "\n\tType de lit: " + chambre.getTypeLit() +
    //                         "\n\tCommodites: " + infoCommodites +
    //                         "\n\tPrix total: " + prixTotal);
    //     }
    // }
}
