package com.ift287lalonde;

import java.util.Date;

import org.bson.types.ObjectId;
import com.mongodb.client.result.InsertOneResult;

public class ManagerReservation {
    
    private AccesClient accesClient;
    private AccesChambre accesChambre;
    private AccesReservation accesReservation;

    public ManagerReservation(
        AccesClient accesClient,
        AccesChambre accesChambre,
        AccesReservation accesReservation
    ) throws IFT287Exception {
        if (accesClient.getConnexion() != accesChambre.getConnexion() || accesClient.getConnexion() != accesReservation.getConnexion()) {
            throw new IFT287Exception(
                "Les instances de AccesClient, AccesChambre et AccesReservation n'utilisent pas la meme connexion au serveur."
            );
        }
        this.accesClient = accesClient;
        this.accesChambre = accesChambre;
        this.accesReservation = accesReservation;
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
        Date dateFin
    ) throws IFT287Exception {
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
        if (!accesReservation.checkChambreLibre(idChambre, dateDebut, dateFin)) {
            throw new IFT287Exception(
                "Impossible de faire reservation pour la chambre " +
                "idChambre=" + idChambre + " pour la periode: " +
                dateDebut.toString() + " - " + dateFin.toString() +
                ".\n La chambre est deja reservee."
            );
        }

        // Retrouve client et chambre
        TupleClient client = accesClient.getClient(idClient);
        TupleChambre chambre = accesChambre.getChambre(idChambre);
        
        // Creer nouvelle reservation et generer id avec ajout MongoDB
        TupleReservation reservation = new TupleReservation(idClient, idChambre, dateDebut, dateFin);
        InsertOneResult result = accesReservation.ajouterReservation(reservation);
        ObjectId idReservation = result.getInsertedId().asObjectId().getValue();
        
        // Ajouter la reservation dans document client
        if (!accesClient.ajouterReservationClient(client, idReservation)) {
            throw new IFT287Exception(
                "Impossible d'ajouter reservation " + idReservation + " au client " +
                "idClient=" + idClient + " dans le document client."
            );
        }
        
        // Ajouter la reservation dans document chambre
        if (!accesChambre.ajouterReservationChambre(chambre, idReservation)) {
            throw new IFT287Exception(
                "Impossible d'ajouter reservation " + idReservation + " a la chambre " +
                "idChambre=" + idChambre + " dans le document chambre."
            );
        };
    }
}
