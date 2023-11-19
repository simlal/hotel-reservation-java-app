package com.ift287lalonde;

public class MainManager {

    private final Connexion cx;
    private final AccesClient accesClient;
    private final AccesChambre accesChambre;
    private final AccesCommodite accesCommodite;
    private final AccesReservation accesReservation;

    private final ManagerClient managerClient;
    private final ManagerChambre managerChambre;
    private final ManagerCommodite managerCommodite;
    private final ManagerReservation managerReservation;

    
    /**
     * Constructeur pour gestionnaire des gestionnaires
     * 
     * @param serveur
     * @param bd
     * @param user
     * @param pass
     * @throws IFT287Exception
     */
    public MainManager(
        String serveur,
        String bd,
        String user,
        String pass
    ) throws IFT287Exception {
        // Creation connexion avec MongoDB
        this.cx = new Connexion(serveur, bd, user, pass);

        // Regroupement package Tables
        this.accesClient = new AccesClient(cx);
        this.accesChambre = new AccesChambre(cx);
        this.accesCommodite = new AccesCommodite(cx);
        this.accesReservation = new AccesReservation(cx);

        // Regroupement package Manager
        this.managerClient = new ManagerClient(
            accesClient, 
            accesReservation, 
            accesChambre, 
            accesCommodite
        );
        this.managerChambre = new ManagerChambre(
            accesChambre, 
            accesReservation, 
            accesCommodite
        );
        this.managerCommodite = new ManagerCommodite(
            accesChambre, 
            accesCommodite
        );
        this.managerReservation = new ManagerReservation(
            accesClient, 
            accesChambre, 
            accesReservation
        );
    }
    // Getters pour Acces pour prints dans main
    public AccesClient getAccesClient() {
        return accesClient;
    }
    public AccesChambre getAccesChambre() {
        return accesChambre;
    }
    public AccesCommodite getAccesCommodite() {
        return accesCommodite;
    }
    public AccesReservation getAccesReservation() {
        return accesReservation;
    }


    // Getters pour Manager
    public ManagerClient getManagerClient() {
        return managerClient;
    }
    public ManagerChambre getManagerChambre() {
        return managerChambre;
    }
    public ManagerCommodite getManagerCommodite() {
        return managerCommodite;
    }
    public ManagerReservation getManagerReservation() {
        return managerReservation;
    }

    public void fermer()
    {
        // fermeture de la connexion
        cx.fermer();
    }
}