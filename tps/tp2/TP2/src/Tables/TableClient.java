package Tables;

import java.util.ArrayList;
import java.util.List;

import AubergeInn.Connexion;
import Tuples.TupleClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Interactions avec table Client
public class TableClient {

    private static final String sqlCheckClient = "select * from Client where idClient = ?";
    private static final String sqlAjouterClient = "insert into Client (idClient, prenom, nom, age) values(?,?,?,?)";
    private static final String sqlSupprimerClient = "delete from Client where idClient = ?";
    private static final String sqlChercherClients = "select * from Client";


    private Connexion cx;
    private final PreparedStatement stmtCheckClient;
    private final PreparedStatement stmtAjouterClient;
    private final PreparedStatement stmtSupprimerClient;
    private final PreparedStatement stmtChercherClients;

    public TableClient(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckClient = cx.getConnection().prepareStatement(sqlCheckClient);
            this.stmtAjouterClient = cx.getConnection().prepareStatement(sqlAjouterClient);
            this.stmtSupprimerClient = cx.getConnection().prepareStatement(sqlSupprimerClient);
            this.stmtChercherClients = cx.getConnection().prepareStatement(sqlChercherClients);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            throw new SQLException("Erreur prepareStatement dans TableClient");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }
    
    /**
     * Verification si client existe deja
     * 
     * @param idClient
     * @return clientExiste
     * @throws SQLException
     */
    public boolean checkClient(int idClient) throws SQLException {
        try {
            stmtCheckClient.setInt(1, idClient);
            ResultSet rs = stmtCheckClient.executeQuery();
            boolean clientExiste = rs.next();
            rs.close();
            return clientExiste;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkClient dans TableClient");
        }
    }

    /**
     * Ajout client dans bd
     * 
     * @param client
     * @return nbClientAj
     * @throws SQLException
     */
    public int ajouterClient(TupleClient client) throws SQLException{
        try {
            // Modif ps avec info client
            stmtAjouterClient.setInt(1, client.getIdClient());
            stmtAjouterClient.setString(2, client.getPrenom());
            stmtAjouterClient.setString(3, client.getNom());
            stmtAjouterClient.setInt(4, client.getAge());

            // Ajout client si existe pas
            int nbClientAj = stmtAjouterClient.executeUpdate();
            return nbClientAj;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur ajouterClient dans TableClient");
        }
    }


    /**
     * Supprimer client dans bd
     * 
     * @param idClient
     * @return nbClient Supp
     * @throws SQLException
     */
    public int supprimerClient(int idClient) throws SQLException {
        try {
            // Supprimer client
            stmtSupprimerClient.setInt(1, idClient);
            int nbClientSupp = stmtSupprimerClient.executeUpdate();
            return nbClientSupp;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur supprimerClient dans TableClient");
        }
    }

    public List<TupleClient> chercherClients() throws SQLException {
        List<TupleClient> clients = new ArrayList<>();
        try {
            // Chercher clients avec statement general
            ResultSet rs = stmtChercherClients.executeQuery();
            while (rs.next()) {
                int idClient = rs.getInt("idClient");
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                int age = rs.getInt("age");

                // Creation client et ajout dans container
                TupleClient client = new TupleClient(idClient, prenom, nom, age);
                clients.add(client);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur chercherClients dans TableClient");
        }
        return clients;
    }
}
