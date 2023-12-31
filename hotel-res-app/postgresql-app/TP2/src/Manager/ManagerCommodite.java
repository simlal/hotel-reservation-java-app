package Manager;

import java.sql.SQLException;

import Tables.TableCommodite;
import Tuples.TupleCommodite;
import AubergeInn.Connexion;

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
        TupleCommodite commodite
        ) throws SQLException {
        try {
            // Check si commodite existe pas et maj db
            if (!tableCommodite.checkCommodite(commodite.getIdCommodite())) {
                tableCommodite.ajouterCommodite(commodite);
                cx.commit();
            } else {    
                throw new SQLException(
                    "Impossible ajouter commodite avec idCommodite=" + commodite.getIdCommodite() + ": existe deja dans db."
                );
            }
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
}
