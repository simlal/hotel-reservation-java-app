package AubergeInn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerClient {
    
    private Connexion cx;
    private final TableClient tableClient;


    public ManagerClient(TableClient tableClient) {
        this.cx = tableClient.getConnexion();
        this.tableClient = tableClient;
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Ajout client si n'existe pas dans db
     * 
     * @param prenom nom age
     * @throws SQLException
     */
    public void ajouterClient(String prenom, String nom, int age) throws SQLException{
            try {
                tableClient.ajouterClient(prenom, nom, age);
                cx.commit();
            }
            catch (SQLException se) {
                cx.rollback();
                se.printStackTrace();
                throw new SQLException("Erreur ajouterClient dans ManagerClient");
            }
        }
    
    /**
     * Supprimer client si existe dans db
     * 
     * @param idClient
     * @throws SQLException
     */
    public void supprimerClient(int idClient) throws SQLException{
        try {
            // Check si client existe et maj db
            if (!tableClient.checkClient(idClient)) {
                throw new SQLException(
                    "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
                );  
            }
            else if (tableClient.checkClientReservationEnCours(idClient)) {
                throw new SQLException(
                    "Impossible supprimer client avec idClient=" + idClient + ": a des reservations en cours."
                );  
            }
            else {
                tableClient.supprimerClient(idClient);
                cx.commit();
            }
        }
        catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur supprimerClient dans ManagerClient");
        }
    }

    public void afficherClient(int idClient) throws SQLException {
        try {
            // Check si client existe et maj db
            if (!tableClient.checkClient(idClient)) {
                throw new SQLException(
                    "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
                ); 
            }
            else {
                tableClient.afficherClient(idClient);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherClient dans ManagerClient");
        }
    }

    public List<TupleClient> getListClients() throws SQLException {
        List<TupleClient> clients = new ArrayList<>();
        try {
            clients = tableClient.getListClients();
            cx.commit();
            return clients;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getListClients dans ManagerClient");
        }
    }
    public List<TupleClient> getListClientsReservEnCours() throws SQLException {
        List<TupleClient> clientsReservEnCours = new ArrayList<>();
        try {
            clientsReservEnCours = tableClient.getListClientsReservEnCours();
            cx.commit();
            return clientsReservEnCours;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur clientsReservEnCours dans ManagerClient");
        }
    }

    public TupleClient getClient(int idClient) throws SQLException {
        try {
            if (!tableClient.checkClient(idClient)) {
                throw new SQLException("Client avec idClient=" + idClient + " n'existe pas");
            }

            TupleClient client = tableClient.getClient(idClient);
            cx.commit();
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
            cx.rollback();
            throw new SQLException("Erreur getClient dans ManagerClient");
        }
    }
}
