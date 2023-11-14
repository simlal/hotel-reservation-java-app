package com.ift287lalonde;

// import com.ift287lalonde.AccesClient;
// import com.ift287lalonde.Connexion;
// import com.ift287lalonde.IFT287Exception;
// import com.ift287lalonde.TupleClient;

public class ManagerClient {
    
    private Connexion cx;
    private AccesClient accesClient;


    public ManagerClient(AccesClient accesClient) {
        this.cx = accesClient.getConnexion();
        this.accesClient = accesClient;
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Ajout client si n'existe pas dans db
     * 
     * @param client
     * @throws IFT287Exception
     */
    public void ajouterClient(TupleClient client) throws IFT287Exception{
            try {
                cx.demarreTransaction();
                accesClient.ajouterClient(client);                       

                // Check si client n'existe pas
                if (accesClient.clientExiste((int)client.getId())) {
                    throw new IFT287Exception(
                        "Impossible ajouter client avec idClient=" + client.getId() + ": existe deja dans db."
                    );
                }
                cx.commit();
            } catch (IFT287Exception e) {
                cx.rollback();
                e.printStackTrace();
                throw new IFT287Exception("Erreur ajouterClient dans ManagerClient");
            }
        }
    
    /**
     * Supprimer client si existe dans db
     * et si n'a pas de reservation en cours
     * 
     * @param idClient
     * @throws IFT287Exception
     */
    public void supprimerClient(int idClient) throws IFT287Exception{
        
        try {
            cx.demarreTransaction();
            // Check si client existe et s'il n'a pas de reservation en cours avant del
            if (!accesClient.clientExiste(idClient)) {
                throw new IFT287Exception(
                    "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
                );  
            }
            if (accesClient.checkClientReservationEnCours(idClient)) {
                throw new IFT287Exception(
                    "Impossible supprimer client avec idClient=" + idClient + ": a des reservations en cours."
                );  
            }
            accesClient.supprimerClient(idClient);
            cx.commit();
            
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur supprimerClient dans ManagerClient");
        }
    }

    /**
     * Afficher client si existe dans db 
     * et ses informations de reservations
     * 
     * @param idClient
     * @throws IFT287Exception
     */
    public void afficherClient(int idClient) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            // Check si client n'existe pas avant affichage
            if (!accesClient.clientExiste(idClient)) {
                throw new IFT287Exception(
                    "Impossible afficher client avec idClient=" + idClient + ": n'existe pas dans db."
                );
            }
            accesClient.afficherClient(idClient);
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur afficherClient dans ManagerClient");
        }
    }
}
