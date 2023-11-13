package Acces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.TypedQuery;

import AubergeInn.Connexion;
import Tuples.TupleChambre;
import Tuples.TupleCommodite;
import Tuples.TupleReservation;

public class AccesChambre {

    private Connexion cx;

    private static final String queryCheckChambre = "select chambre from TupleChambre chambre where chambre.id = :idChambre";
    private static final String queryCheckChambreReservationEnCours = "select reservation from TupleReservation reservation where chambre.id = :idChambre "
            +
            "and reservation.dateDebut < :maintenant and reservation.dateFin > :maintenant";
    private static final String queryCheckChambreReservationFuture = "select reservation from TupleReservation reservation where chambre.id = :idChambre "
            +
            "and reservation.dateDebut > :maintenant";
    private static final String queryAfficherChambres = "select chambre from TupleChambre chambre";

    private TypedQuery<TupleChambre> stmtCheckChambre;
    private TypedQuery<TupleReservation> stmtCheckChambreReservationEnCours;
    private TypedQuery<TupleReservation> stmtCheckChambreReservationFuture;
    private TypedQuery<TupleChambre> stmtAfficherChambres;

    public AccesChambre(Connexion cx) {
        this.cx = cx;

        this.stmtCheckChambre = cx.getConnection().createQuery(queryCheckChambre, TupleChambre.class);
        this.stmtCheckChambreReservationEnCours = cx.getConnection().createQuery(queryCheckChambreReservationEnCours,
                TupleReservation.class);
        this.stmtCheckChambreReservationFuture = cx.getConnection().createQuery(queryCheckChambreReservationFuture,
                TupleReservation.class);
        this.stmtAfficherChambres = cx.getConnection().createQuery(queryAfficherChambres, TupleChambre.class);
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Verifie si une chambre existe dans la base de donnees avec l'identifiant
     * specifie.
     * 
     * @param idChambre l'identifiant de la chambre à verifier
     * @return true si la chambre existe, false sinon
     */
    public boolean chambreExiste(int idChambre) {
        stmtCheckChambre.setParameter("idChambre", idChambre);
        return !stmtCheckChambre.getResultList().isEmpty();
    }

    /**
     * Chercher la chambre avec id
     * 
     * @param idChambre id de la chambre
     * @return TupleChambre
     */
    public TupleChambre getChambre(int idChambre) {
        if (chambreExiste(idChambre)) {
            stmtCheckChambre.setParameter("idChambre", idChambre);
            return stmtCheckChambre.getSingleResult();
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
        cx.getConnection().persist(chambre);
    }

    /**
     * Supprime une chambre de la base de donnees.
     * 
     * @param idChambre
     */
    public void supprimerChambre(int idChambre) {
        TupleChambre chambre = getChambre(idChambre);

        if (chambre == null) {
            throw new IllegalArgumentException("Chambre inexistante: " + idChambre);
        }
        cx.getConnection().remove(chambre);
    }

    /**
     * Verifie si une chambre a une reservation en cours
     * 
     * @param idChambre l'identifiant de la chambre à verifier
     * @return true si la chambre a une reservation en cours, false sinon
     */
    public boolean checkChambreReservationEnCours(int idChambre) {
        Date maintenant = new Date();
        stmtCheckChambreReservationEnCours.setParameter("idChambre", idChambre);
        stmtCheckChambreReservationEnCours.setParameter("maintenant", maintenant);

        List<TupleReservation> reservations = stmtCheckChambreReservationEnCours.getResultList();
        return !reservations.isEmpty();
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
