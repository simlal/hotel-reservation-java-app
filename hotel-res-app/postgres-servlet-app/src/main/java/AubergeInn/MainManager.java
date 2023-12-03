package AubergeInn;

import java.sql.SQLException;

public class MainManager {

    private final TableClient tableClient;
    private final TableChambre tableChambre;
    private final TableCommodite tableCommodite;
    private final TableChambreCommodite tableChambreCommodite;
    private final TableReservation tableReservation;

    private final ManagerClient managerClient;
    private final ManagerChambre managerChambre;
    private final ManagerCommodite managerCommodite;
    private final ManagerChambreCommodite managerChambreCommodite;
    private final ManagerReservation managerReservation;

    public MainManager(Connexion cx) throws Exception {
        try {
            // Regroupement package Tables
            this.tableClient = new TableClient(cx);
            this.tableChambre = new TableChambre(cx);
            this.tableCommodite = new TableCommodite(cx);
            this.tableChambreCommodite = new TableChambreCommodite(cx);
            this.tableReservation = new TableReservation(cx);

            // Regroupement package Manager
            this.managerClient = new ManagerClient(tableClient);
            this.managerChambre = new ManagerChambre(tableChambre);
            this.managerCommodite = new ManagerCommodite(tableCommodite);
            this.managerChambreCommodite = new ManagerChambreCommodite(
                tableChambre, 
                tableCommodite, 
                tableChambreCommodite
            );
            this.managerReservation = new ManagerReservation(
                tableClient, 
                tableChambre, 
                tableReservation
            );
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            throw new SQLException("Erreur dans MainManager");
        }
        
    }
    // Getters pour Tables
    public TableClient getTableClient() {
        return tableClient;
    }
    public TableChambre getTableChambre() {
        return tableChambre;
    }
    public TableCommodite getTableCommodite() {
        return tableCommodite;
    }
    public TableChambreCommodite getTableChambreCommodite() {
        return tableChambreCommodite;
    }
    public TableReservation getTableReservation() {
        return tableReservation;
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
    public ManagerChambreCommodite getManagerChambreCommodite() {
        return managerChambreCommodite;
    }
    public ManagerReservation getManagerReservation() {
        return managerReservation;
    }

//    Fermeture connexion
    public void fermer() {
//        cx.close();
    }
    
}
