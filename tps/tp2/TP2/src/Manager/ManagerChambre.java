package Manager;

import AubergeInn.Connexion;
import Tables.TableChambre;
import Tuples.TupleChambre;

import java.sql.SQLException;

public class ManagerChambre {
    
    private Connexion cx;
    private TableChambre tableChambre;
    
    public ManagerChambre(TableChambre tableChambre) {
        this.cx = tableChambre.getConnexion();
        this.tableChambre = tableChambre;
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Ajout chambre si n'existe pas dans db
     * 
     * @param chambre
     * @throws IllegalStateException
     * @throws SQLException
     */
    public void ajouterChambre(
        TupleChambre chambre
        ) throws IllegalStateException, SQLException {
        try {
            // Check si chambre existe pas et maj db
            if (!tableChambre.checkChambre(chambre.getIdChambre())) {
                tableChambre.ajouterChambre(chambre);
                cx.commit();
            }
            else {    
                throw new IllegalStateException(
                    "Impossible ajouter chambre avec idChambre=" + chambre.getIdChambre() + ": existe deja dans db."
                );
            }
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur ajouterChambre dans ManagerChambre");
        }
    }
    

    public void supprimerChambre(
        int idChambre) throws IllegalStateException, SQLException {
        try {
            // Check si chambre existe et maj db
            if (tableChambre.checkChambre(idChambre)) {
                tableChambre.supprimerChambre(idChambre);
                cx.commit();
            }
            else {    
                throw new IllegalStateException(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
                );
            }
            // TODO CHECK SI CHAMBRE LIBRE AVANT SUPPRIMER
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new IllegalStateException("Erreur supprimerChambre dans ManagerChambre");
        }
    }
}
