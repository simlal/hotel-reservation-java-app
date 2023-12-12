package AubergeInn;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableCommodite {
    
    private Connexion cx;
    private final static String sqlCheckCommodite = "select * from Commodite where idCommodite = ?";
    private final static String sqlAjouterCommodite = "insert into Commodite (description, surplusPrix) values(?,?)";
    private final static String sqlSupprimerCommodite = "delete from Commodite where idCommodite = ?";
    private final static String sqlGetListCommodites = "select * from Commodite";

    private final PreparedStatement stmtCheckCommodite;
    private final PreparedStatement stmtAjouterCommodite;
    private final PreparedStatement stmtSupprimerCommodite;
    private final PreparedStatement stmtGetListCommodites;

    public TableCommodite(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckCommodite = cx.getConnection().prepareStatement(sqlCheckCommodite);
            this.stmtAjouterCommodite = cx.getConnection().prepareStatement(sqlAjouterCommodite);
            this.stmtSupprimerCommodite = cx.getConnection().prepareStatement(sqlSupprimerCommodite);
            this.stmtGetListCommodites = cx.getConnection().prepareStatement(sqlGetListCommodites);
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
            rs.close();
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
    public int ajouterCommodite(String description, int surplusPrix) throws SQLException {
        try {
            // maj ps avec attributs de commodite
            stmtAjouterCommodite.setString(1, description);
            stmtAjouterCommodite.setInt(2, surplusPrix);

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

    public List<TupleCommodite> getListCommodites() throws SQLException {
        List<TupleCommodite> commodites = new ArrayList<>();
        try {
            ResultSet rs = stmtGetListCommodites.executeQuery();

            while (rs.next()) {
                int idCommodite = rs.getInt(1);
                String description = rs.getString(2);
                int surplusPrix = rs.getInt(3);

                TupleCommodite commodite = new TupleCommodite(idCommodite, description, surplusPrix);
                commodites.add(commodite);
            }
            rs.close();
            return commodites;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getListCommodites dans TableCommodite");
        }
    }

    public TupleCommodite getCommodite(int idCommodite) throws SQLException{
        try {
            stmtCheckCommodite.setInt(1, idCommodite);
            ResultSet rs = stmtCheckCommodite.executeQuery();
            rs.next();
            int idCommoditeRs = rs.getInt(1);
            String description = rs.getString(2);
            int surplusPrix = rs.getInt(3);

            TupleCommodite commodite = new TupleCommodite(idCommoditeRs, description, surplusPrix);
            rs.close();
            return commodite;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur getCommodite dans TableCommodite");
        }
    }
}
