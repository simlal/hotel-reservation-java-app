package com.ift287lalonde;

import java.util.Date;

// import com.ift287lalonde.AccesClient;
// import com.ift287lalonde.AccesChambre;
// import com.ift287lalonde.AccesReservation;
// import com.ift287lalonde.Connexion;
// import com.ift287lalonde.IFT287Exception;
// import com.ift287lalonde.TupleClient;
// import com.ift287lalonde.TupleChambre;
// import com.ift287lalonde.TupleReservation;

public class ManagerReservation {
    
    private Connexion cx;
    private AccesClient accesClient;
    private AccesChambre accesChambre;
    private AccesReservation accesReservation;

    public ManagerReservation(
        AccesClient accesClient,
        AccesChambre accesChambre,
        AccesReservation accesReservation) {
        this.cx = accesReservation.getConnexion();
        // TODO verif si connection est semblable pour chaque instance
        this.accesClient = accesClient;
        this.accesChambre = accesChambre;
        this.accesReservation = accesReservation;
        }

    /**
     * Retourne la connexion
     *
     * @return Connexion
     */
    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Effectue une reservation si le client et la chambre existe et si la chambre
     * est libre pour la periode specifiee
     * 
     * @param idClient
     * @param idChambre
     * @param dateDebut
     * @param dateFin
     * @throws IFT287Exception
     */
    public void reserver(
        int idClient, 
        int idChambre, 
        Date dateDebut,
        Date dateFin) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            // Verif si client existe
            if (!accesClient.clientExiste(idClient)) {
                throw new IFT287Exception(
                    "Impossible de reserver car client inexistant:" + idClient
                );
            }
            // Verif si chambre existe
            if (!accesChambre.chambreExiste(idChambre)) {
                throw new IFT287Exception(
                    "Impossible de reserver car chambre inexistante:" + idChambre
                );
            }
            // Verif entree des dates
            if (dateFin.compareTo(dateDebut) <= 0) {
                throw new IFT287Exception(
                    "Impossible faire reservation avec dateDebut=" + dateDebut.toString() +
                    " et dateFin=" + dateFin.toString() + ": " +
                    "dateFin doit etre plus grand que dateDebut."
                );
            }

            // Verifie si la chambre peut etre reserver entre date
            if (accesReservation.checkChambreReserve(idChambre, dateDebut, dateFin)) {
                throw new IFT287Exception(
                    "Impossible de faire reservation pour la chambre " +
                    "idChambre=" + idChambre + " pour la periode: " +
                    dateDebut.toString() + " - " + dateFin.toString() +
                    ".\n La chambre est deja reservee."
                );
            }

            // Retrouve client et chambre + lien avec reservation
            TupleClient client = accesClient.getClient(idClient);
            TupleChambre chambre = accesChambre.getChambre(idChambre);
            TupleReservation reservation = new TupleReservation(client, chambre, dateDebut, dateFin);
            
            // Ajout reservation dans les instances de client et chambre
            client.getReservations().add(reservation);
            chambre.getReservations().add(reservation);
            
            // persister la transaction pour reservation
            accesReservation.ajouterReservation(reservation);
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur reserver dans ManagerReservation");
        }
    }
}
