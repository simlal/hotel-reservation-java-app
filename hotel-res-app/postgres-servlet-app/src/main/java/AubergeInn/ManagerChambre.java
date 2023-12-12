package AubergeInn;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
        String nom,
        String description,
        int prixBase
        ) throws SQLException {
        try {
            tableChambre.ajouterChambre(nom, description, prixBase);
            cx.commit();
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
            }
            if (tableChambre.checkChambreResFuture(idChambre)) {
                throw new SQLException(
                        "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre est reservee dans le futur."
                );
            }
            if (tableChambre.checkChambreResEnCours(idChambre)) {
                throw new SQLException(
                        "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre est reservee en se moment."
                );
            }
            tableChambre.supprimerChambre(idChambre);
            cx.commit();

        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur supprimerChambre dans ManagerChambre");
        }
    }

    /**
     * Afficher chambres libres + prix total
     * 
     * @param dateDebut
     * @param dateFin
     * @throws SQLException
     */
    public void afficherChambresLibres(
        Date dateDebut, 
        Date dateFin
    ) throws SQLException {
        try {
            if (dateFin.compareTo(dateDebut) <= 0){
                throw new SQLException(
                    "Impossible faire reservation avec dateDebut=" + 
                    dateDebut.toString() + " et dateFin=" + dateFin.toString() +
                    ": dateFin doit etre plus grand que dateDebut."
                );
            } else {
                tableChambre.afficherChambresLibres(dateDebut, dateFin);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherChambresLibres dans ManagerChambre");
        }
    }

    /**
     * Afficher info chambre
     * 
     * @param idChambre
     * @throws SQLException
     */
    public void afficherChambre(
        int idChambre
    ) throws SQLException {
        try {
            if (!tableChambre.checkChambre(idChambre)) {
                throw new SQLException(
                    "Impossible afficher info chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
                );
            } else {
                tableChambre.afficherChambre(idChambre);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherInfoChambre dans ManagerChambre");
        }
    }

    public List<TupleChambre> getListChambres()throws SQLException {
        List<TupleChambre> chambres = new ArrayList<>();
        try {
            chambres = tableChambre.getListChambres();
            cx.commit();
            return chambres;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getListChambres dans ManagerChambre");
        }
    }

    public List<TupleChambre> getListChambresLibres(Date dateDebut, Date dateFin)throws SQLException {
        List<TupleChambre> chambresLibres = new ArrayList<>();
        try {
            chambresLibres = tableChambre.getListChambresLibres(dateDebut, dateFin);
            cx.commit();
            return chambresLibres;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getListChambresLibres dans ManagerChambre");
        }
    }

    public TupleChambre getChambre(int idChambre) throws SQLException {
        try {
            if (!tableChambre.checkChambre(idChambre)) {
                throw new SQLException("Chambre avec idChambre=" + idChambre + " n'existe pas");
            }
            TupleChambre chambre = tableChambre.getChambre(idChambre);
            cx.commit();
            return chambre;
        } catch (SQLException e) {
            e.printStackTrace();
            cx.rollback();
            throw new SQLException("Erreur getChambre dans ManagerChambre");
        }
    }
}
