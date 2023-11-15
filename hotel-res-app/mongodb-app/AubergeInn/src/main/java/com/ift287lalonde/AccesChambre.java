package com.ift287lalonde;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

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
        if (chambreExiste(idChambre)) {
            Document chambreDoc = chambresCollection.find(eq("idChambre", idChambre).first());
            TupleChambre chambre = new TupleChambre(chambreDoc);
            return chambre;
        } else {
            return null;
        }
    }

    /**
     * Ajoute une chambre dans la base de donnees.
     * 
     * @param chambre
     */
    public void ajouterChambre(TupleChambre chambre) {
        chambresCollection.insertOne(chambre.toDocument());
    }

    /**
     * Supprime une chambre de la base de donnees.
     * 
     * @param idChambre
     * @return true si chambre supprimer
     */
    public boolean supprimerChambre(TupleChambre chambre) {
        if (!chambreExiste(chambre.getId())) {
            throw new IllegalArgumentException("Chambre inexistante: " + chambre.getId());
        }
        boolean chambreSupprime = chambresCollection.deleteOne(eq("idChambre", chambre.getId())).getDeletedCount() > 0;
        return chambreSupprime;
    }

    /**
     * Verifie si une chambre a une reservation en cours
     * 
     * @param chambre la chambre à verifier
     * @return true si la chambre a une reservation en cours, false sinon
     */
    public boolean checkChambreReservationEnCours(TupleChambre chambre) {
        // Chercher la collection reservation
        boolean chambreReservee = false;
        MongoCollection<Document> reservationsCollection = new AccesReservation(getConnexion()).getReservationsCollection();
        MongoCursor<Document> reservationsCursor = reservationsCollection.find(eq("idChambre", chambre.getId()))
            .sort(ascending("dateDebut"))
            .iterator();
        // Chambre jamais reservee
        if (!reservationsCursor.hasNext()) {
            return chambreReservee;
        }
        // Chercher reservations associe a chambre
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
        // Verifier si reservation est en cours
        // Verifie si la reservation est en cours
        Date maintenant = new Date();
        for (TupleReservation reservation : reservations) {
            if (reservation.getDateDebut().before(maintenant) && reservation.getDateFin().after(maintenant)) {
                chambreReservee = true;
                break;
            }
        }
        return chambreReservee;
    }

    /**
     * Verifie si une chambre a des reservations futures.
     * 
     * @param idChambre l'identifiant de la chambre à verifier
     * @return true si la chambre a des reservations futures, false sinon
     */
    public boolean checkChambreReservationFuture(int idChambre) {
        // maj statement avec date maintenant
        Date maintenant = new Date();
        stmtCheckChambreReservationFuture.setParameter("idChambre", idChambre);
        stmtCheckChambreReservationFuture.setParameter("maintenant", maintenant);
        List<TupleReservation> reservations = stmtCheckChambreReservationFuture.getResultList();
        return !reservations.isEmpty();
    }

    /**
     * Affiche les informations d'une chambre
     * 
     * @param idChambre l'identifiant de la chambre à afficher
     * @throws IllegalArgumentException si la chambre avec id existe pas
     */
    public void afficherChambre(int idChambre) {
        // Chercher la chambre + prixBase
        TupleChambre chambre = getChambre(idChambre);
        if (chambre == null) {
            throw new IllegalArgumentException("Chambre inexistante: " + idChambre);
        }
        int prixTotal = chambre.getPrixBase();

        // Chercher commodites associees a chambre
        List<TupleCommodite> commodites = chambre.getCommodites();

        // Representation en string des infos
        String infoCommodites = "";
        for (TupleCommodite commodite : commodites) {
            infoCommodites += commodite.getDescription() + "=";
            infoCommodites += commodite.getSurplusPrix() + "$" + ", ";
            prixTotal += commodite.getSurplusPrix();
        }
        // Enlever derniere virgule
        if (infoCommodites.endsWith(", ")) {
            infoCommodites = infoCommodites.substring(0, infoCommodites.length() - 2);
        }
        // Pour les chambres sans commodites
        if (infoCommodites.isEmpty()) {
            infoCommodites = "Aucune commodite";
        }

        // Afficher informations chambre
        System.out.println(
                "\nChambre: " + chambre.getNom() +
                        "\n\tidChambre: " + chambre.getId() +
                        "\n\tType de lit: " + chambre.getTypeLit() +
                        "\n\tPrix de base: " + chambre.getPrixBase() + "$" +
                        "\n\tCommodites: " + infoCommodites +
                        "\n\tPrix total: " + prixTotal + "$");
    }

    /**
     * Affiche les chambres libres pour une periode donnee.
     * 
     * @param dateDebut La date de debut de la periode.
     * @param dateFin   La date de fin de la periode.
     */
    public void afficherChambresLibres(Date dateDebut, Date dateFin) {
        // Chercher les chambres avec reservations
        List<TupleChambre> chambres = stmtAfficherChambres.getResultList();

        // Verifier si chambres libres pour dates donnees
        List<TupleChambre> chambresLibres = new ArrayList<TupleChambre>();
        for (TupleChambre chambre : chambres) {
            List<TupleReservation> reservations = chambre.getReservations();
            boolean isFree = true;
            for (TupleReservation reservation : reservations) {
                if (reservation.getDateDebut().compareTo(dateFin) < 0
                        && reservation.getDateFin().compareTo(dateDebut) > 0) {
                    isFree = false;
                    break;
                }
            }
            if (isFree) {
                chambresLibres.add(chambre);
            }
        }
        // affichage en fonction des dates
        if (chambresLibres.isEmpty()) {
            System.out.println(
                    "\nAucune chambre libre pour la periode:" +
                            "\n\tDate debut: " + dateDebut +
                            "\n\tDate fin: " + dateFin);
        }

        for (TupleChambre chambre : chambresLibres) {
            int prixTotal = chambre.getPrixBase();

            // Chercher commodites associees a chambre
            List<TupleCommodite> commodites = chambre.getCommodites();

            // Representation en string des infos
            String infoCommodites = "";
            for (TupleCommodite commodite : commodites) {
                infoCommodites += commodite.getDescription() + "=";
                infoCommodites += commodite.getSurplusPrix() + "$" + ", ";
                prixTotal += commodite.getSurplusPrix();
            }
            // Enlever derniere virgule
            if (infoCommodites.endsWith(", ")) {
                infoCommodites = infoCommodites.substring(0, infoCommodites.length() - 2);
            }
            // Pour les chambres sans commodites
            if (infoCommodites.isEmpty()) {
                infoCommodites = "Aucune commodite";
            }

            // Afficher informations chambres libres
            System.out.println(
                    "\nChambre: " + chambre.getNom() +
                            "\n\tidChambre: " + chambre.getId() +
                            "\n\tType de lit: " + chambre.getTypeLit() +
                            "\n\tCommodites: " + infoCommodites +
                            "\n\tPrix total: " + prixTotal);
        }
    }
}
