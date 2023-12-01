package AubergeInn;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TableCommodite {
    
    private Connexion cx;
    private final static String sqlCheckCommodite = "select * from Commodite where idCommodite = ?";
    private final static String sqlAjouterCommodite = "insert into Commodite (idCommodite, description, surplusPrix) values(?,?,?)";
    private final static String sqlSupprimerCommodite = "delete from Commodite where idCommodite = ?";

    private final PreparedStatement stmtCheckCommodite;
    private final PreparedStatement stmtAjouterCommodite;
    private final PreparedStatement stmtSupprimerCommodite;

    public TableCommodite(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckCommodite = cx.getConnection().prepareStatement(sqlCheckCommodite);
            this.stmtAjouterCommodite = cx.getConnection().prepareStatement(sqlAjouterCommodite);
            this.stmtSupprimerCommodite = cx.getConnection().prepareStatement(sqlSupprimerCommodite);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur prepareStatement dans TableCommodite");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Verif si commodite existe selon id
     * 
     * @param idCommodite
     * @return commoditeExiste
     * @throws SQLException
     */
    public boolean checkCommodite(int idCommodite) throws SQLException{
        try {
            stmtCheckCommodite.setInt(1, idCommodite);
            ResultSet rs = stmtCheckCommodite.executeQuery();
            boolean commoditeExiste = rs.next();
            return commoditeExiste;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkCommodite dans TableCommodite");
        }
    }

    /**
     * Ajout d'une commodite
     * 
     * @param commodite
     * @return nbCommoditeAj
     * @throws SQLException
     */
    public int ajouterCommodite(TupleCommodite commodite) throws SQLException {
        try {
            // maj ps avec attributs de commodite
            stmtAjouterCommodite.setInt(1, commodite.getIdCommodite());
            stmtAjouterCommodite.setString(2, commodite.getDescription());
            stmtAjouterCommodite.setInt(3, commodite.getSurplusPrix());

            // execution du insert
            int nbCommoditeAj = stmtAjouterCommodite.executeUpdate();
            return nbCommoditeAj;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur ajouterCommodite dans TableCommodite");
        }
    }

    /**
     * Supprimer une commodite
     * 
     * @param idCommodite
     * @return nbCommoditeSup
     * @throws SQLException
     */
    public int supprimerCommodite(int idCommodite) throws SQLException {
        try {
            // maj ps et supprimer dans db
            stmtSupprimerCommodite.setInt(1, idCommodite);
            int nbCommoditeSup = stmtSupprimerCommodite.executeUpdate();
            return nbCommoditeSup;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur supprimerCommodite dans TableCommodite");
        }
    }
}
