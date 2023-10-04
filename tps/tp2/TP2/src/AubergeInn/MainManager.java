package AubergeInn;

import java.sql.SQLException;

import Manager.ManagerChambre;
import Manager.ManagerClient;

import Tables.TableClient;
import Tables.TableChambre;


public class MainManager {

    private final TableClient tableClient;
    private final TableChambre tableChambre;

    private final ManagerClient managerClient;
    private final ManagerChambre managerChambre;

    public MainManager(Connexion cx) throws Exception {
        try {
            // Regroupement package Tables
            this.tableClient = new TableClient(cx);
            this.tableChambre = new TableChambre(cx);
            // this.

            // Regroupement package Manager
            this.managerClient = new ManagerClient(tableClient);
            this.managerChambre = new ManagerChambre(tableChambre);
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


    // Getters pour Manager
    public ManagerClient getManagerClient() {
        return managerClient;
    }
    public ManagerChambre getManagerChambre() {
        return managerChambre;
    }
    
}
