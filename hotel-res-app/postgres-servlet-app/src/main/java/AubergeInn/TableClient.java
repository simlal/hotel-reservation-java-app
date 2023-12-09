package AubergeInn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


// Interactions avec table Client
public class TableClient {

    private static final String sqlCheckClient = "select * from Client where idClient = ?";
    private static final String sqlAjouterClient = "insert into Client (idClient, prenom, nom, age) values(?,?,?,?)";
    private static final String sqlSupprimerClient = "delete from Client where idClient = ?";
    private static final String sqlAfficherClient = 
    "SELECT Client.*, Reservation.dateDebut, Reservation.dateFin, Chambre.*, Commodite.*, Chambre.prixBase + COALESCE(SUM(Commodite.surplusPrix), 0) AS prixTotalCommodites " +
    "FROM Client " +
    "LEFT JOIN Reservation ON Client.idClient = Reservation.idClient " +
    "LEFT JOIN Chambre ON Reservation.idChambre = Chambre.idChambre " +
    "LEFT JOIN ChambreCommodite ON Chambre.idChambre = ChambreCommodite.idChambre " +
    "LEFT JOIN Commodite ON ChambreCommodite.idCommodite = Commodite.idCommodite " +
    "WHERE Client.idClient = ? " +
    "GROUP BY Client.idClient, Reservation.dateDebut, Reservation.dateFin, Chambre.idChambre, Commodite.idCommodite";

    private static final String sqlCheckClientReservationEnCours = 
    "select * from Client " +
    "join Reservation on Client.idClient = Reservation.idClient " +
    "where Client.idClient = ? and Reservation.dateDebut < ? and Reservation.dateFin > ?";

    private static final String sqlGetClients = "select * from Client";


    private Connexion cx;
    private final PreparedStatement stmtCheckClient;
    private final PreparedStatement stmtAjouterClient;
    private final PreparedStatement stmtSupprimerClient;
    private final PreparedStatement stmtAfficherClient;
    private final PreparedStatement stmtCheckClientReservationEnCours;
    private final PreparedStatement stmtGetClients;

    public TableClient(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckClient = cx.getConnection().prepareStatement(sqlCheckClient);
            this.stmtAjouterClient = cx.getConnection().prepareStatement(sqlAjouterClient);
            this.stmtSupprimerClient = cx.getConnection().prepareStatement(sqlSupprimerClient);
            this.stmtAfficherClient = cx.getConnection().prepareStatement(sqlAfficherClient);
            this.stmtCheckClientReservationEnCours = 
            cx.getConnection().prepareStatement(sqlCheckClientReservationEnCours);
            this.stmtGetClients = cx.getConnection().prepareStatement(sqlGetClients);
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

    public boolean checkClientReservationEnCours(int idClient) throws SQLException {
        try {
            // update ps avec idclient et date maintenant
            Date maintenant = new Date(System.currentTimeMillis());
            stmtCheckClientReservationEnCours.setInt(1, idClient);
            stmtCheckClientReservationEnCours.setDate(2, maintenant);
            stmtCheckClientReservationEnCours.setDate(3, maintenant);

            ResultSet rs = stmtCheckClientReservationEnCours.executeQuery();
            boolean clientReserve = rs.next();
            rs.close();
            return clientReserve;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkClientReservation dans TableClient");
        }
    }

    public List<TupleClient> getListClients() throws SQLException {
        try {
            ResultSet rs = stmtGetClients.executeQuery();
            List<TupleClient> clients = new ArrayList<TupleClient>();
            while (rs.next()) {
                Integer idClient = rs.getInt(1);
                String prenom = rs.getString(2);
                String nom = rs.getString(3);
                Integer age = rs.getInt(4);
                TupleClient client = new TupleClient(idClient, prenom, nom, age);

                clients.add(client);
            }
            rs.close();
            return clients;
        } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Erreur getListClients dans TableClient");
            }
    }
    public void afficherClient(int idClient) throws SQLException {
        try {
            // update ps avec idclient
            stmtAfficherClient.setInt(1, idClient);

            ResultSet rs = stmtAfficherClient.executeQuery();
            while (rs.next()) {
                System.out.println(
                    "idClient: " + rs.getInt(1) + 
                    ", prenom: " + rs.getString(2) + 
                    ", nom: " + rs.getString(3) + 
                    ", age: " + rs.getInt(4) + 
                    ", dateDebut: " + rs.getDate(5) + 
                    ", dateFin: " + rs.getDate(6) + 
                    ", idChambre: " + rs.getInt(7) +
                    ", nomChambre: " + rs.getString(8) +
                    ", prixBaseChambre: " + rs.getInt(10) +
                    ", prixTotalCommodites: " + rs.getInt(13)
                );
                
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherClient dans TableClient");
        }
    }
}
