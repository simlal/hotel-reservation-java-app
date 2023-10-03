package Tables;

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

    private Connexion cx;
    private final PreparedStatement stmtCheckClient;
    private final PreparedStatement stmtAjouterClient;
    private final PreparedStatement stmtSupprimerClient;

    public TableClient(Connexion cx) throws SQLException{
        this.cx = cx;
        this.stmtCheckClient = cx.getConnection().prepareStatement(sqlCheckClient);
        this.stmtAjouterClient = cx.getConnection().prepareStatement(sqlAjouterClient);
        this.stmtSupprimerClient = cx.getConnection().prepareStatement(sqlSupprimerClient);
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
        stmtCheckClient.setInt(1, idClient);
        ResultSet rs = stmtCheckClient.executeQuery();
        boolean clientExiste = rs.next();
        rs.close();
        return clientExiste;
    }

    /**
     * Ajout client dans bd
     * 
     * @param client
     * @return nbClientAj
     * @throws SQLException
     */
    public int ajouterClient(TupleClient client) throws SQLException{
        
        // Modif ps avec info client
        stmtAjouterClient.setInt(1, client.getIdClient());
        stmtAjouterClient.setString(2, client.getPrenom());
        stmtAjouterClient.setString(3, client.getNom());
        stmtAjouterClient.setInt(4, client.getAge());

        // Ajout client si existe pas
        int nbClientAj = stmtAjouterClient.executeUpdate();
        return nbClientAj;

    }


    /**
     * Supprimer client dans bd
     * 
     * @param idClient
     * @return nbClient Supp
     * @throws SQLException
     */
    public int supprimerClient(int idClient) throws SQLException {
        stmtSupprimerClient.setInt(1, idClient);
        int nbClientSupp = stmtSupprimerClient.executeUpdate();
        return nbClientSupp;
    }
}
