package Manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import AubergeInn.Connexion;
import Tables.TableClient;
import Tuples.TupleClient;

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
     * @param client
     * @throws SQLException
     */
    public void ajouterClient(TupleClient client) throws SQLException{
            try {
                // Check si client existe et maj db
                if (!tableClient.checkClient(client.getIdClient())) {
                    tableClient.ajouterClient(client);   
                }
                else {    
                    throw new SQLException(
                        "Impossible ajouter client avec idClient=" + client.getIdClient() + ": existe deja dans db."
                    );
                }
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
}
