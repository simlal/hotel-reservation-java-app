package AubergeInn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerCommodite {
    
    private Connexion cx;
    private final TableCommodite tableCommodite;

    public ManagerCommodite(TableCommodite tableCommodite) {
        this.cx = tableCommodite.getConnexion();
        this.tableCommodite = tableCommodite;
    }

    /**
     * Ajout commodite si n'existe pas dans db
     * 
     * @param commodite
     * @throws SQLException
     */
    public void ajouterCommodite(
        String description,
        int surplusPrix
        ) throws SQLException {
        try {
            tableCommodite.ajouterCommodite(description, surplusPrix);
            cx.commit();
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur ajouterCommodite dans ManagerCommodite");
        }
    }

    /**
     * Supprimer commodite si existe dans db
     * 
     * @param idCommodite
     * @throws SQLException
     */
    public void supprimerCommodite(
        int idCommodite
        ) throws SQLException {
            try {
                // Check si commodite existe
                if (tableCommodite.checkCommodite(idCommodite)) {
                    tableCommodite.supprimerCommodite(idCommodite);
                    cx.commit();
                } else {
                    throw new SQLException(
                        "Impossible supprimer commodite avec idCommodite=" + idCommodite + ": n'existe pas dans db."
                    );
                }
                // TODO DEAL SI COMMODITE ASSOCIE A CHAMBRE
            } catch (SQLException se) {
                cx.rollback();
                se.printStackTrace();
                throw new SQLException("Erreur supprimerCommodite dans ManagerCommodite");
            }
        }
    public List<TupleCommodite> getListCommodites() throws SQLException {
        List<TupleCommodite> commodites = new ArrayList<>();
        try {
            commodites = tableCommodite.getListCommodites();
            cx.commit();
            return commodites;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getListCommodites dans ManagerCommodite");
        }
    }

    public TupleCommodite getCommodite(int idCommodite) throws SQLException {
        try {
            if (!tableCommodite.checkCommodite(idCommodite)) {
                throw new SQLException(
                        "Impossible supprimer commodite avec idCommodite=" + idCommodite + ": n'existe pas dans db."
                );
            }
            TupleCommodite commodite = tableCommodite.getCommodite(idCommodite);
            cx.commit();
            return commodite;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getCommodite dans ManagerCommodite");
        }
    }
}
