package AubergeInn;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TableChambreCommodite {
    
    private Connexion cx;
    private final static String sqlCheckChambreCommodite = "select * from ChambreCommodite where idChambre = ? and idCommodite = ?";
    private final static String sqlInclureChambreCommodite = "insert into ChambreCommodite (idChambre, idCommodite) values (?,?)";
    private final static String sqlEnleverChambreCommodite = "delete from ChambreCommodite where idChambre = ? and idCommodite = ?";

    private final PreparedStatement stmtCheckChambreCommodite;
    private final PreparedStatement stmtInclureChambreCommodite;
    private final PreparedStatement stmtEnleverChambreCommodite;

    public TableChambreCommodite(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckChambreCommodite = cx.getConnection().prepareStatement(sqlCheckChambreCommodite);
            this.stmtInclureChambreCommodite = cx.getConnection().prepareStatement(sqlInclureChambreCommodite);
            this.stmtEnleverChambreCommodite = cx.getConnection().prepareStatement(sqlEnleverChambreCommodite);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur prepareStatement dans TableChambreCommodite");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Verif si chambreCommodite existe selon idChambre et idCommodite
     * 
     * @param idChambre
     * @param idCommodite
     * @return chambreCommoditeExiste
     * @throws SQLException
     */
    public boolean chambreCommoditeExiste(
        TupleChambreCommodite chambreCommodite
        ) throws SQLException {
        try {
            // Maj ps avec parametres
            stmtCheckChambreCommodite.setInt(1, chambreCommodite.getIdChambre());
            stmtCheckChambreCommodite.setInt(2, chambreCommodite.getIdCommodite());

            // Verif si chambreCommodite existe
            ResultSet rs = stmtCheckChambreCommodite.executeQuery();
            boolean chambreCommoditeExiste = rs.next();
            return chambreCommoditeExiste;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur chambreCommoditeExiste dans TableChambreCommodite");
        }
    }

    /**
     * Inclure une commodite a une chambre
     * 
     * @param idChambre
     * @param idCommodite
     * @return nbChambreCommoditeIncl
     * @throws SQLException
     */
    public int inclureChambreCommodite(
        TupleChambreCommodite chambreCommodite
    ) throws SQLException{
        try {
            // maj ps avec idChambre et idCommodite
            stmtInclureChambreCommodite.setInt(1, chambreCommodite.getIdChambre());
            stmtInclureChambreCommodite.setInt(2, chambreCommodite.getIdCommodite());

            // execution du insert
            int nbChambreCommoditeIncl = stmtInclureChambreCommodite.executeUpdate();
            return nbChambreCommoditeIncl;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur inclureChambreCommodite dans TableChambreCommodite");
        }
    }

    /**
     * Enlever une commodite d'une chambre
     * 
     * @param idChambre
     * @param idCommodite
     * @return nbChambreCommoditeEnl
     * @throws SQLException
     */
    public int enleverChambreCommodite(
        TupleChambreCommodite chambreCommodite
    ) throws SQLException {
        try {
            // maj ps avec idChambre et idCommodite
            stmtEnleverChambreCommodite.setInt(1, chambreCommodite.getIdChambre());
            stmtEnleverChambreCommodite.setInt(2, chambreCommodite.getIdCommodite());

            // execution du delete
            int nbChambreCommoditeEnl = stmtEnleverChambreCommodite.executeUpdate();
            return nbChambreCommoditeEnl;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur enleverChambreCommodite dans TableChambreCommodite");
        }
    }
}
