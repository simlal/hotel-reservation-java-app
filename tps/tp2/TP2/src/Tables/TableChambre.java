package Tables;

import AubergeInn.Connexion;
import Tuples.TupleChambre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;

// Interactions avec table Chambre
public class TableChambre {
    private static final String sqlCheckChambre = "select * from Chambre where idChambre = ?";
    private static final String sqlAjouterChambre = 
    "insert into Chambre (idChambre, nom, typeLit, prixBase) values(?,?,?,?)";
    private static final String sqlSupprimerChambre = "delete from Chambre where idChambre = ?";
    private static final String sqlCheckChambreResFuture = 
    "select * from Chambre " +
    "join Reservation on Chambre.idChambre = Reservation.idChambre " +
    "where Chambre.idChambre = ? and Reservation.dateDebut >= ?";
    private static final String sqlCheckChambreReservation = 
    "select * from Chambre " +
    "join Reservation on Chambre.idChambre = Reservation.idChambre " +
    "where Chambre.idChambre = ?";
    
    private Connexion cx;
    private final PreparedStatement stmtCheckChambre;
    private final PreparedStatement stmtAjouterChambre;
    private final PreparedStatement stmtSupprimerChambre;
    private final PreparedStatement stmtCheckChambreResFuture;
    private final PreparedStatement stmtCheckChambreReservation;

    public TableChambre(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckChambre = cx.getConnection().prepareStatement(sqlCheckChambre);
            this.stmtAjouterChambre = cx.getConnection().prepareStatement(sqlAjouterChambre);
            this.stmtSupprimerChambre = cx.getConnection().prepareStatement(sqlSupprimerChambre);
            this.stmtCheckChambreResFuture = cx.getConnection().prepareStatement(sqlCheckChambreResFuture);
            this.stmtCheckChambreReservation = cx.getConnection().prepareStatement(sqlCheckChambreReservation);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            throw new SQLException("Erreur prepareStatement dans TableChambre");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Verification si quitchambre existe (true si existe, false sinon)
     * 
     * @param idChambre
     * @return chambreExiste
     * @throws SQLException
     */
    public boolean checkChambre(int idChambre) throws SQLException {
        try {
            stmtCheckChambre.setInt(1, idChambre);
            ResultSet rs = stmtCheckChambre.executeQuery();
            boolean chambreExiste = rs.next();
            rs.close();
            return chambreExiste;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambre dans TableChambre");
        }
    }

    public boolean checkChambreResFuture(int idChambre) throws SQLException {
        try {
            // update ps avec idchambre et date du query
            Date maintenant = new Date(System.currentTimeMillis());
            stmtCheckChambreResFuture.setInt(1, idChambre);
            stmtCheckChambreResFuture.setDate(2, maintenant);

            ResultSet rs = stmtCheckChambreResFuture.executeQuery();
            boolean chambreReserveeFuture = rs.next();
            rs.close();
            return chambreReserveeFuture;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambreResFuture dans TableChambre");
        }
    }

    public boolean checkChambreReservation(int idChambre) throws SQLException {
        try {
            // update ps avec idchambre
            stmtCheckChambreReservation.setInt(1, idChambre);

            ResultSet rs = stmtCheckChambreReservation.executeQuery();
            boolean chambreReservee = rs.next();
            rs.close();
            return chambreReservee;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambreReservation dans TableChambre");
        }
    }

    /**
     * Operation ajout chambre a table
     * 
     * @param chambre
     * @return nbChambreAj
     * @throws SQLException
     */
    public int ajouterChambre(TupleChambre chambre) throws SQLException {
        try {
            // Modif ps avec info chambre
            stmtAjouterChambre.setInt(1, chambre.getIdChambre());
            stmtAjouterChambre.setString(2, chambre.getNom());
            stmtAjouterChambre.setString(3, chambre.getTypeLit());
            stmtAjouterChambre.setInt(4, chambre.getPrixBase());
            
            // Ajout chambre
            int nbChambreAj = stmtAjouterChambre.executeUpdate();
            return nbChambreAj;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur ajouterChambre dans TableChambre");
        }
    }

    /**
     * Operation supprimer chambre sur table
     * 
     * @param idChambre
     * @return nbChambreSup
     * @throws SQLException
     */
    public int supprimerChambre(int idChambre) throws SQLException{
        try {
            // Modif ps avec info chambre
            stmtSupprimerChambre.setInt(1, idChambre);
            
            // Supprimer chambre
            int nbChambreSup = stmtSupprimerChambre.executeUpdate();
            return nbChambreSup;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur supprimerChambre dans TableChambre");
        }
    }

    // TODO CHECK CHAMBRES LIBRES
}
