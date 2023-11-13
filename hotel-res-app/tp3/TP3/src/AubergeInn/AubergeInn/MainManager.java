package AubergeInn;

import Acces.AccesClient;
import Acces.AccesCommodite;
import Acces.AccesChambre;
import Acces.AccesReservation;
import Manager.ManagerChambre;
import Manager.ManagerClient;
import Manager.ManagerCommodite;
import Manager.ManagerReservation;

public class MainManager {

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
     * @param cx
     * @throws IFT287Exception
     */
    public MainManager(Connexion cx) throws IFT287Exception {
        // Regroupement package Tables
        this.accesClient = new AccesClient(cx);
        this.accesChambre = new AccesChambre(cx);
        this.accesCommodite = new AccesCommodite(cx);
        this.accesReservation = new AccesReservation(cx);

        // Regroupement package Manager
        this.managerClient = new ManagerClient(accesClient);
        this.managerChambre = new ManagerChambre(accesChambre);
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
    // Getters pour Acces
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
}
