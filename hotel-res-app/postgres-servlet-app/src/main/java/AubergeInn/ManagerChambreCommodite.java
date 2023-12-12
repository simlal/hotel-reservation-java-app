package AubergeInn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerChambreCommodite {

    private Connexion cx;
    private final TableChambre tableChambre;
    private final TableCommodite tableCommodite;
    private final TableChambreCommodite tableChambreCommodite;

    public ManagerChambreCommodite(
        TableChambre tableChambre,
        TableCommodite tableCommodite,
        TableChambreCommodite tableChambreCommodite
        ) throws IFT287Exception{
        this.cx = tableChambreCommodite.getConnexion();
        if (
                tableChambre.getConnexion() != tableCommodite.getConnexion() ||
                        tableChambre.getConnexion() != tableChambreCommodite.getConnexion() ||
                        tableCommodite.getConnexion() != tableChambreCommodite.getConnexion()
        ) {
            throw new IFT287Exception(
                    "Les instances de TableChambre, TableCommodite et/ou TableChambreCommodite n'utilisent pas la meme connexion au serveur."
            );
        }
        this.tableChambre = tableChambre;
        this.tableCommodite = tableCommodite;
        this.tableChambreCommodite = tableChambreCommodite;
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Ajout d'une commodite a une chambre d'une relation existante
     * 
     * @param chambreCommodite
     * @throws SQLException
     */
    public void inclureChambreCommodite(
        TupleChambreCommodite chambreCommodite
    ) throws SQLException {
        try {
            // Check si idChambre et idCommodite existent independemment
            if (!tableChambre.checkChambre(chambreCommodite.getIdChambre())) {
                throw new SQLException(
                    "idChambre = " + chambreCommodite.getIdChambre() + "n'existe pas."
                );
            }
            else if (!tableCommodite.checkCommodite(chambreCommodite.getIdCommodite())) {
                throw new SQLException(
                    "idCommodite = " + chambreCommodite.getIdCommodite() + "n'existe pas."
                );
            } else {
                // Check si entree chambreCommodite
                if (!tableChambreCommodite.chambreCommoditeExiste(chambreCommodite)) {
                    tableChambreCommodite.inclureChambreCommodite(chambreCommodite);
                    cx.commit();
                } else {
                    throw new SQLException(
                        "Impossible inclure chambreCommodite avec idChambre=" + chambreCommodite.getIdChambre() + " et idCommodite=" + chambreCommodite.getIdCommodite() + ": existe deja dans db."
                    );
                }
            }
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur inclureCommodite avec chambreCommodite");
        }
    }

    /**
     * Enlever une commodite d'une chambre d'une relation existante
     * 
     * @param chambreCommodite
     * @throws SQLException
     */
    public void enleverChambreCommodite(
        TupleChambreCommodite chambreCommodite
    ) throws SQLException {
        try {
            // Check si idChambre et idCommodite existent independemment
            if (!tableChambre.checkChambre(chambreCommodite.getIdChambre())) {
                throw new SQLException(
                    "idChambre = " + chambreCommodite.getIdChambre() + "n'existe pas."
                );
            }
            else if (!tableCommodite.checkCommodite(chambreCommodite.getIdCommodite())) {
                throw new SQLException(
                    "idCommodite = " + chambreCommodite.getIdCommodite() + "n'existe pas."
                );
            } else {
                // Check si entree chambreCommodite
                if (tableChambreCommodite.chambreCommoditeExiste(chambreCommodite)) {
                    tableChambreCommodite.enleverChambreCommodite(chambreCommodite);
                    cx.commit();
                } else {
                    throw new SQLException(
                        "Impossible enlever chambreCommodite avec idChambre=" + chambreCommodite.getIdChambre() + " et idCommodite=" + chambreCommodite.getIdCommodite() + ": n'existe pas dans db."
                    );
                }
            }
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur enleverCommodite avec chambreCommodite");
        }
    }

    public List<TupleCommodite> getCommoditesDeChambre(TupleChambre chambre) throws SQLException {
        List<TupleCommodite> commoditesDeChambre = new ArrayList<>();
        try {
            if (!tableChambre.checkChambre(chambre.getIdChambre())) {
                throw new SQLException(
                        "idChambre = " + chambre.getIdChambre() + " n'existe pas"
                );
            }
            commoditesDeChambre = tableChambreCommodite.getCommoditesdeChambre(chambre);
            cx.commit();
            return commoditesDeChambre;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getCommoditesDeChambre dans ManagerCommodite");
        }
    }

    public List<TupleChambre> getChambresDeCommodite(TupleCommodite commodite) throws SQLException {
        List<TupleChambre> chambresDeCommodite = new ArrayList<>();
        try {
            if (!tableCommodite.checkCommodite(commodite.getIdCommodite())) {
                throw new SQLException(
                        "idCommodite = " + commodite.getIdCommodite() + " n'existe pas"
                );
            }
            chambresDeCommodite = tableChambreCommodite.getChambresDeCommodite(commodite);
            cx.commit();
            return chambresDeCommodite;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getChambresDeCommodite dans ManagerCommodite");
        }
    }
}
