package com.ift287lalonde;

import javax.persistence.TypedQuery;

import com.ift287lalonde.Connexion;

import java.util.List;
import java.util.Date;

// import com.ift287lalonde.TupleChambre;
// import com.ift287lalonde.TupleClient;
// import com.ift287lalonde.TupleCommodite;
// import com.ift287lalonde.TupleReservation;

public class AccesClient {

    private Connexion cx;

    private static final String queryCheckClient = "select client from TupleClient client where client.id = :idClient";

    private TypedQuery<TupleClient> stmtCheckClient;

    /**
     * Creation instance representant l'acces a un client
     * 
     * @param cx
     */
    public AccesClient(Connexion cx) {
        this.cx = cx;

        stmtCheckClient = cx.getConnection().createQuery(queryCheckClient, TupleClient.class);

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
     * Verif client existe
     * 
     * @param idClient
     * @return boolean
     */
    public boolean clientExiste(int idClient) {
        stmtCheckClient.setParameter("idClient", idClient);
        boolean cExiste = !stmtCheckClient.getResultList().isEmpty();
        return cExiste;
    }

    /**
     * Chercher un client
     * 
     * @param idClient
     * @return TupleClient
     */
    public TupleClient getClient(int idClient) {
        if (clientExiste(idClient)) {
            stmtCheckClient.setParameter("idClient", idClient);
            return stmtCheckClient.getSingleResult();
        } else {
            return null;
        }
    }

    /**
     * Ajout d'un nouveau client
     * 
     * @param client
     */
    public void ajouterClient(TupleClient client) {
        cx.getConnection().persist(client);
    }

    /**
     * Suppression d'un client avec le id
     * 
     * @param client
     */
    public void supprimerClient(int idClient) {
        TupleClient client = getClient(idClient);
        if (client == null) {
            throw new IllegalArgumentException("Client inexistant pour idClient" + idClient);
        }
        cx.getConnection().remove(client);
    }

    /**
     * Cherche un client avec reservations dans db
     * 
     * @param idClient
     * @return TupleClient
     */
    public boolean checkClientReservationEnCours(int idClient) {
        TupleClient client = getClient(idClient);
        if (client == null) {
            throw new IllegalArgumentException("Client inexistant :" + idClient);
        }

        // Verif si client a reservation dans db
        List<TupleReservation> reservations = client.getReservations();
        boolean clientAvecReservation = !reservations.isEmpty();
        if (!clientAvecReservation) {
            return false; // Pas de reserv du tout
        }
        // Verif si reservation(s) sont en cours
        boolean reservationEnCours = false;
        Date maintenant = new Date();
        for (TupleReservation reservation : reservations) {
            if (reservation.getDateDebut().before(maintenant) && reservation.getDateFin().after(maintenant)) {
                reservationEnCours = true;
                break;
            }
        }
        return reservationEnCours;
    }

    /**
     * Affiche les informations d'un client avec reservations associees
     * 
     * @param idClient l'identifiant du client dont les informations doivent être affichees
     */
    public void afficherClient(int idClient) {
        // Info base de cllient
        TupleClient client = getClient(idClient);
        String infoClient = "\nInformations client: " +
                "\n\tidClient: " + client.getId() +
                "\n\tPrenom: " + client.getPrenom() +
                "\n\tNom: " + client.getNom() +
                "\n\tAge: " + client.getAge();

        // Informations chambre et comm sur reservations
        List<TupleReservation> reservations = client.getReservations();
        String infoReservations = "\nReservations associees: ";
        if (reservations.isEmpty() || reservations.size() == 0) {
            infoReservations = "\nAucune reservation pour ce client";
        }

        for (TupleReservation reservation : reservations) {
            // info chambre par reserv
            TupleChambre chambre = reservation.getChambre();
            int prixTotal = chambre.getPrixBase();

            // Calcul prix avec inclusions commodites par chambre
            List<TupleCommodite> commodites = chambre.getCommodites();
            if (!commodites.isEmpty()) {
                for (TupleCommodite commodite : commodites) {
                    prixTotal += commodite.getSurplusPrix();
                }
            }
            // Ajout information chambre/commodites pour prixTotal
            infoReservations += "\n\tidReservation: " + reservation.getId() +
                    "\n\t\tChambre: " + chambre.getNom() +
                    "\n\t\tdateDebut: " + reservation.getDateDebut() +
                    "\n\t\tdateFin: " + reservation.getDateFin() +
                    "\n\t\tprixTotal: " + prixTotal + "$";
        }

        // Affichage complet pour client
        System.out.println(infoClient + infoReservations);
    }
}