package Acces;

import javax.persistence.TypedQuery;

import AubergeInn.Connexion;

import java.util.Date;

import Tuples.TupleReservation;

public class AccesReservation {
    
    private Connexion cx;

    private final static String queryCheckReservation = 
        "select reservation from TupleReservation reservation where reservation.id = :idReservation";
    private final static String queryCheckChambreReserve = 
        "select reservation from TupleReservation reservation where chambre.id = :idChambre " + 
        "and reservation.dateDebut < :dateFin and reservation.dateFin > :dateDebut";
    
    private TypedQuery<TupleReservation> stmtCheckReservation;
    private TypedQuery<TupleReservation> stmtCheckChambreReserve;

    public AccesReservation(Connexion cx) {
        this.cx = cx;

        this.stmtCheckReservation = cx.getConnection().createQuery(queryCheckReservation, TupleReservation.class);
        this.stmtCheckChambreReserve = cx.getConnection().createQuery(queryCheckChambreReserve, TupleReservation.class);
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
     * Verifie si la reservation existe
     * 
     * @param idReservation
     * @return true si la reservation existe, false sinon
     */
    public boolean reservationExiste(int idReservation) {
        stmtCheckReservation.setParameter("idReservation", idReservation);
        boolean reservationExiste = !stmtCheckReservation.getResultList().isEmpty();
        return reservationExiste;
    }

    /**
     * Verifie si la chambre est reservee
     * 
     * @param idChambre
     * @param dateDebut
     * @param dateFin
     * @return true si la chambre est reservee, false sinon
     */
    public boolean checkChambreReserve(int idChambre, Date dateDebut, Date dateFin) {
        stmtCheckChambreReserve.setParameter("idChambre", idChambre);
        stmtCheckChambreReserve.setParameter("dateDebut", dateDebut);
        stmtCheckChambreReserve.setParameter("dateFin", dateFin);
        boolean chambreReserve = !stmtCheckChambreReserve.getResultList().isEmpty();
        return chambreReserve;
    }

    /**
     * Cherche la reservation avec l'id fourni
     * 
     * @param idReservation
     * @return TupleReservation
     */
    public TupleReservation getReservation(int idReservation) {
        if (reservationExiste(idReservation)) {
            stmtCheckReservation.setParameter("idReservation", idReservation);
            return stmtCheckReservation.getSingleResult();
        } else {
            return null;
        }
    }

    /**
     * Ajoute une reservation dans la db
     * 
     * @param reservation
     */
    public void ajouterReservation(TupleReservation reservation) {
        cx.getConnection().persist(reservation);
    }

    /**
     * Supprime une reservation de la db
     *
     * @param reservation 
     */
    // ?Pas necessaire pour l'instant
    public void supprimerReservation(TupleReservation reservation) {
        cx.getConnection().remove(reservation);
    }
}
