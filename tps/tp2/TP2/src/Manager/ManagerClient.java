package Manager;

import java.sql.SQLException;

import AubergeInn.Connexion;
import Tables.TableClient;
import Tuples.TupleClient;

public class ManagerClient {
    
    private Connexion cx;
    private TableClient tableClient;


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
     * @throws IllegalStateException
     * @throws SQLException
     */
    public void ajouterClient(TupleClient client) throws IllegalStateException, SQLException{
            try {
                // Check si client existe et maj db
                if (!tableClient.checkClient(client.getIdClient())) {
                    tableClient.ajouterClient(client);   
                }
                else {    
                    throw new IllegalStateException(
                        "Impossible ajouter client avec idClient=" + client.getIdClient() + ": existe deja dans db."
                    );
                }
                cx.commit();
            }
            catch (SQLException se) {
                cx.rollback();
                System.out.println(se.getMessage());
            }
        }
    
    /**
     * Supprimer client si existe dans db
     * 
     * @param idClient
     * @throws IllegalStateException
     * @throws SQLException
     */
    public void supprimerClient(int idClient) throws IllegalStateException, SQLException{
        try {
            // Check si client existe et maj db
            if (tableClient.checkClient(idClient)) {
                tableClient.supprimerClient(idClient);   
            }
            else {    
                throw new IllegalStateException(
                    "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
                );
            }
            cx.commit();
        }
        catch (SQLException se) {
            cx.rollback();
            System.out.println(se.getMessage());
        }
    }

    public void afficherClient(int idClient) throws SQLException {
        // tableClient.afficherClient(idClient);
        // TODO
    }
}
