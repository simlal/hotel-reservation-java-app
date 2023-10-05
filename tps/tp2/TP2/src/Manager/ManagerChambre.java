package Manager;

import AubergeInn.Connexion;
import Tables.TableChambre;
import Tuples.TupleChambre;

import java.sql.SQLException;

public class ManagerChambre {
    
    private Connexion cx;
    private final TableChambre tableChambre;
    
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
     * @throws SQLException
     */
    public void ajouterChambre(
        TupleChambre chambre
        ) throws SQLException {
        try {
            // Check si chambre existe pas et maj db
            if (!tableChambre.checkChambre(chambre.getIdChambre())) {
                tableChambre.ajouterChambre(chambre);
                cx.commit();
            }
            else {    
                throw new SQLException(
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
        int idChambre) throws SQLException {
        try {
            // Check si chambre existe et maj db
            if (!tableChambre.checkChambre(idChambre)) {
                throw new SQLException(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
                );
            } else if (tableChambre.checkChambreResFuture(idChambre)) {
                throw new SQLException(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre est reservee dans le futur."
                );
            } else {    
                // if (tableChambre.checkChambreReservation(idChambre)) {
                //     tableReservation.supprimerReservationChambre(idChambre);
                // }
                tableChambre.supprimerChambre(idChambre);
                cx.commit();
            }

        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur supprimerChambre dans ManagerChambre");
        }
    }
}
