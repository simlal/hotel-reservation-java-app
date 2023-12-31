package com.ift287lalonde;

import java.util.ArrayList;
import java.util.List;

public class ManagerClient {

    private AccesClient accesClient;
    private AccesReservation accesReservation;
    private AccesChambre accesChambre;
    private AccesCommodite accesCommodite;

    public ManagerClient(
        AccesClient accesClient,
        AccesReservation accesReservation,
        AccesChambre accesChambre,
        AccesCommodite accesCommodite
    ) throws IFT287Exception {
        if (accesClient.getConnexion() != accesReservation.getConnexion() || accesClient.getConnexion() != accesChambre.getConnexion() || accesClient.getConnexion() != accesCommodite.getConnexion()) {
            throw new IFT287Exception(
                "Les instances de AccesClient, AccesReservation, AccesChambre et AccesCommodite n'utilisent pas la meme connexion au serveur."
            );
        }
        this.accesClient = accesClient;
        this.accesReservation = accesReservation;
        this.accesChambre = accesChambre;
        this.accesCommodite = accesCommodite;
    }


    /**
     * Ajout client si n'existe pas dans db
     * 
     * @param client
     * @throws IFT287Exception
     */
    public void ajouterClient(TupleClient client) throws IFT287Exception{
        System.out.println("Checking if client exists");
        // Check si client n'existe pas
        if (accesClient.clientExiste(client.getId())) {
            throw new IFT287Exception(
                "Impossible ajouter client avec idClient=" + client.getId() + ": existe deja dans db."
            );
        }
        System.out.println("pre ajouterClient manager");
        accesClient.ajouterClient(client);
        System.out.println("done");

    }
    
    /**
     * Supprimer client si existe dans db
     * et si n'a pas de reservation en cours
     * 
     * @param idClient
     * @throws IFT287Exception
     */
    public void supprimerClient(int idClient) throws IFT287Exception{
        // Check si client existe et s'il n'a pas de reservation en cours avant del
        if (!accesClient.clientExiste(idClient)) {
            throw new IFT287Exception(
                "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
            );  
        }
        if (accesReservation.clientReservationEnCours(idClient)) {
            throw new IFT287Exception(
                "Impossible supprimer client avec idClient=" + idClient + ": a des reservations en cours."
            );  
        }
        accesClient.supprimerClient(idClient);
    }

    /**
     * Afficher client si existe dans db 
     * et ses informations de reservations
     * 
     * @param idClient
     * @throws IFT287Exception
     */
    public void afficherClient(int idClient) throws IFT287Exception {
        // Check si client n'existe pas avant affichage
        if (!accesClient.clientExiste(idClient)) {
            throw new IFT287Exception(
                "Impossible afficher client avec idClient=" + idClient + ": n'existe pas dans db."
            );
        }
        
        // Chercher les informations et reservations du client
        TupleClient client = accesClient.getClient(idClient);
        // Info de base client
        String infoClient = "\nInformations client: " +
            "\n\tidClient: " + client.getId() +
            "\n\tPrenom: " + client.getPrenom() +
            "\n\tNom: " + client.getNom() +
            "\n\tAge: " + client.getAge();

        // Chercher les reservations associees
        List<TupleReservation> reservations = new ArrayList<>();
        reservations = accesReservation.listerReservationsClient(idClient);
        String infoReservations = "\nReservations associees: ";
        // Aucune reservation
        if (reservations.isEmpty() || reservations.size() == 0) {
            infoReservations = "\nAucune reservation pour ce client";
        }
        // Chercher les informations de chaque chambre
        for (TupleReservation reservation : reservations) {
            TupleChambre chambre = accesChambre.getChambreReservation(reservation.getId());
            int prixTotal = chambre.getPrixBase();
            // Calcul prix avec inclusions commodites par chambre
            List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(chambre.getId());
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
