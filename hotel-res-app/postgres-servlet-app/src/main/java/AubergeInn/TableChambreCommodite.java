package AubergeInn;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class TableChambreCommodite {
    
    private Connexion cx;
    private final static String sqlCheckChambreCommodite = "select * from ChambreCommodite where idChambre = ? and idCommodite = ?";
    private final static String sqlInclureChambreCommodite = "insert into ChambreCommodite (idChambre, idCommodite) values (?,?)";
    private final static String sqlEnleverChambreCommodite = "delete from ChambreCommodite where idChambre = ? and idCommodite = ?";
    private final static String sqlGetCommmoditesDeChambre =
            "select Commodite.* from Chambre " +
                    "join ChambreCommodite on Chambre.idChambre = ChambreCommodite.idChambre " +
                    "join Commodite on Commodite.idCommodite = ChambreCommodite.idCommodite " +
                    "where Chambre.idChambre = ? " +
                    "order by Commodite.idCommodite";

    private final static String sqlGetChambresDeCommodite =
            "select Chambre.* from Commodite " +
            "join ChambreCommodite on Commodite.idCommodite = ChambreCommodite.idCommodite " +
            "join Chambre on Chambre.idChambre = ChambreCommodite.idChambre " +
            "where Commodite.idCommodite = ? " +
            "order by Chambre.idChambre";

    private final PreparedStatement stmtCheckChambreCommodite;
    private final PreparedStatement stmtInclureChambreCommodite;
    private final PreparedStatement stmtEnleverChambreCommodite;
    private final PreparedStatement stmtGetCommoditesDeChambre;
    private final PreparedStatement stmtGetChambresDeCommodite;

    public TableChambreCommodite(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckChambreCommodite = cx.getConnection().prepareStatement(sqlCheckChambreCommodite);
            this.stmtInclureChambreCommodite = cx.getConnection().prepareStatement(sqlInclureChambreCommodite);
            this.stmtEnleverChambreCommodite = cx.getConnection().prepareStatement(sqlEnleverChambreCommodite);
            this.stmtGetCommoditesDeChambre = cx.getConnection().prepareStatement(sqlGetCommmoditesDeChambre);
            this.stmtGetChambresDeCommodite = cx.getConnection().prepareStatement(sqlGetChambresDeCommodite);
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
            rs.close();
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

    public List<TupleCommodite> getCommoditesdeChambre(TupleChambre chambre) throws SQLException {
        List<TupleCommodite> commoditesDeChambre = new ArrayList<>();

        try {
            stmtGetCommoditesDeChambre.setInt(1, chambre.getIdChambre());
            ResultSet rs = stmtGetCommoditesDeChambre.executeQuery();

            while (rs.next()) {
                int idCommodite = rs.getInt(1);
                String description = rs.getString(2);
                int surplusPrix = rs.getInt(3);
                TupleCommodite commodite = new TupleCommodite(idCommodite, description, surplusPrix);

                commoditesDeChambre.add(commodite);
            }
            rs.close();
            return commoditesDeChambre;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getCommoditesDeChambre dans TableChambreCommodite");
        }
    }

    public List<TupleChambre> getChambresDeCommodite(TupleCommodite commodite) throws SQLException {
        List<TupleChambre> chambresDeCommodite = new ArrayList<>();

        try {
            stmtGetChambresDeCommodite.setInt(1, commodite.getIdCommodite());
            ResultSet rs = stmtGetChambresDeCommodite.executeQuery();

            while (rs.next()) {
                int idChambre = rs.getInt(1);
                String nomChambre = rs.getString(2);
                String typeLit = rs.getString(3);
                int prixBase = rs.getInt(4);
                TupleChambre chambre = new TupleChambre(idChambre, nomChambre, typeLit, prixBase);

                chambresDeCommodite.add(chambre);
            }
            rs.close();
            return chambresDeCommodite;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getChambresDeCommodite dans TableChambreCommodite");
        }
    }
}
