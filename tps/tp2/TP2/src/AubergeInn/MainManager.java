package AubergeInn;

import java.sql.SQLException;

import Manager.ManagerChambre;
import Manager.ManagerClient;
import Manager.ManagerCommodite;
import Manager.ManagerChambreCommodite;

import Tables.TableClient;
import Tables.TableCommodite;
import Tables.TableChambre;
import Tables.TableChambreCommodite;


public class MainManager {

    private final TableClient tableClient;
    private final TableChambre tableChambre;
    private final TableCommodite tableCommodite;
    private final TableChambreCommodite tableChambreCommodite;

    private final ManagerClient managerClient;
    private final ManagerChambre managerChambre;
    private final ManagerCommodite managerCommodite;
    private final ManagerChambreCommodite managerChambreCommodite;

    public MainManager(Connexion cx) throws Exception {
        try {
            // Regroupement package Tables
            this.tableClient = new TableClient(cx);
            this.tableChambre = new TableChambre(cx);
            this.tableCommodite = new TableCommodite(cx);
            this.tableChambreCommodite = new TableChambreCommodite(cx);

            // Regroupement package Manager
            this.managerClient = new ManagerClient(tableClient);
            this.managerChambre = new ManagerChambre(tableChambre);
            this.managerCommodite = new ManagerCommodite(tableCommodite);
            this.managerChambreCommodite = new ManagerChambreCommodite(
                tableChambre, 
                tableCommodite, 
                tableChambreCommodite
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
    
}
