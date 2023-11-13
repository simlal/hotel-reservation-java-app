package Tables;

import AubergeInn.Connexion;
import Tuples.TupleChambre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Date;

// Interactions avec table Chambre
public class TableChambre {
    private static final String sqlCheckChambre = "select * from Chambre where idChambre = ?";
    private static final String sqlAjouterChambre = 
    "insert into Chambre (idChambre, nom, typeLit, prixBase) values(?,?,?,?)";
    private static final String sqlSupprimerChambre = "delete from Chambre where idChambre = ?";
    private static final String sqlCheckChambreResFuture = 
    "select * from Chambre " +
    "join Reservation on Chambre.idChambre = Reservation.idChambre " +
    "where Chambre.idChambre = ? and Reservation.dateDebut >= ?";
    private static final String sqlCheckChambreReservation = 
    "select * from Chambre " +
    "join Reservation on Chambre.idChambre = Reservation.idChambre " +
    "where Chambre.idChambre = ?";
    private static final String sqlAfficherChambresLibres = 
    "SELECT Chambre.idChambre, Chambre.nom, Chambre.typeLit, Chambre.prixBase, SUM(COALESCE(Commodite.surplusPrix, 0)) AS prixTotalCommodite " +
    "FROM Chambre " +
    "LEFT JOIN ChambreCommodite ON Chambre.idChambre = ChambreCommodite.idChambre " +
    "LEFT JOIN Commodite ON ChambreCommodite.idCommodite = Commodite.idCommodite " +
    "WHERE Chambre.idChambre NOT IN (SELECT idChambre FROM Reservation WHERE (dateDebut < ? AND dateFin > ?)) " +
    "GROUP BY Chambre.idChambre " +
    "ORDER BY Chambre.idChambre";
    
    private static final String sqlAfficherChambre = 
    "select Chambre.*, Commodite.* " +
    "from Chambre " +
    "left join ChambreCommodite on Chambre.idChambre = ChambreCommodite.idChambre " +
    "left join Commodite on ChambreCommodite.idCommodite = Commodite.idCommodite " +
    "where Chambre.idChambre = ? " +
    "group by Chambre.idChambre, Commodite.idCommodite";

    
    private Connexion cx;
    private final PreparedStatement stmtCheckChambre;
    private final PreparedStatement stmtAjouterChambre;
    private final PreparedStatement stmtSupprimerChambre;
    private final PreparedStatement stmtCheckChambreResFuture;
    private final PreparedStatement stmtCheckChambreReservation;
    private final PreparedStatement stmtAfficherChambresLibres;
    private final PreparedStatement stmtAfficherChambre;

    public TableChambre(Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckChambre = cx.getConnection().prepareStatement(sqlCheckChambre);
            this.stmtAjouterChambre = cx.getConnection().prepareStatement(sqlAjouterChambre);
            this.stmtSupprimerChambre = cx.getConnection().prepareStatement(sqlSupprimerChambre);
            this.stmtCheckChambreResFuture = cx.getConnection().prepareStatement(sqlCheckChambreResFuture);
            this.stmtCheckChambreReservation = cx.getConnection().prepareStatement(sqlCheckChambreReservation);
            this.stmtAfficherChambresLibres = cx.getConnection().prepareStatement(sqlAfficherChambresLibres);
            this.stmtAfficherChambre = cx.getConnection().prepareStatement(sqlAfficherChambre);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            throw new SQLException("Erreur prepareStatement dans TableChambre");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Verification si quitchambre existe (true si existe, false sinon)
     * 
     * @param idChambre
     * @return chambreExiste
     * @throws SQLException
     */
    public boolean checkChambre(int idChambre) throws SQLException {
        try {
            stmtCheckChambre.setInt(1, idChambre);
            ResultSet rs = stmtCheckChambre.executeQuery();
            boolean chambreExiste = rs.next();
            rs.close();
            return chambreExiste;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambre dans TableChambre");
        }
    }

    public boolean checkChambreResFuture(int idChambre) throws SQLException {
        try {
            // update ps avec idchambre et date du query
            Date maintenant = new Date(System.currentTimeMillis());
            stmtCheckChambreResFuture.setInt(1, idChambre);
            stmtCheckChambreResFuture.setDate(2, maintenant);

            ResultSet rs = stmtCheckChambreResFuture.executeQuery();
            boolean chambreReserveeFuture = rs.next();
            rs.close();
            return chambreReserveeFuture;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambreResFuture dans TableChambre");
        }
    }

    public boolean checkChambreReservation(int idChambre) throws SQLException {
        try {
            // update ps avec idchambre
            stmtCheckChambreReservation.setInt(1, idChambre);

            ResultSet rs = stmtCheckChambreReservation.executeQuery();
            boolean chambreReservee = rs.next();
            rs.close();
            return chambreReservee;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur checkChambreReservation dans TableChambre");
        }
    }

    /**
     * Operation ajout chambre a table
     * 
     * @param chambre
     * @return nbChambreAj
     * @throws SQLException
     */
    public int ajouterChambre(TupleChambre chambre) throws SQLException {
        try {
            // Modif ps avec info chambre
            stmtAjouterChambre.setInt(1, chambre.getIdChambre());
            stmtAjouterChambre.setString(2, chambre.getNom());
            stmtAjouterChambre.setString(3, chambre.getTypeLit());
            stmtAjouterChambre.setInt(4, chambre.getPrixBase());
            
            // Ajout chambre
            int nbChambreAj = stmtAjouterChambre.executeUpdate();
            return nbChambreAj;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur ajouterChambre dans TableChambre");
        }
    }

    /**
     * Operation supprimer chambre sur table
     * 
     * @param idChambre
     * @return nbChambreSup
     * @throws SQLException
     */
    public int supprimerChambre(int idChambre) throws SQLException{
        try {
            // Modif ps avec info chambre
            stmtSupprimerChambre.setInt(1, idChambre);
            
            // Supprimer chambre
            int nbChambreSup = stmtSupprimerChambre.executeUpdate();
            return nbChambreSup;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur supprimerChambre dans TableChambre");
        }
    }

    /**
     * Afficher chambres libres non reserver
     * 
     * @param dateDebut
     * @param dateFin
     * @throws SQLException
     */
    public void afficherChambresLibres(Date dateDebut, Date dateFin) throws SQLException {
        try {
            // Modif ps avec info chambre
            stmtAfficherChambresLibres.setDate(1, dateFin);
            stmtAfficherChambresLibres.setDate(2, dateDebut);
            
            // Afficher chambres libres
            ResultSet rs = stmtAfficherChambresLibres.executeQuery();
            if (!rs.next()) {
                System.out.println(
                    "\nAucune chambre libre pour la periode:" +
                    dateDebut.toString() + " - " + dateFin.toString()
                );
            }
            else {
                System.out.println(
                "\nChambres libres pour la periode: " + dateDebut.toString() +
                " - " + dateFin.toString() + ":\n"
                );
                // List<Map<String, String>> chambresLibres = new ArrayList<Map<String, String>>();
                while (rs.next()) {
                    // Ajout dans container map
                    // HashMap<String, String> chambreLibre = new HashMap<String, String>();
                    int idChambre = rs.getInt("idChambre");
                    String nom = rs.getString("nom");
                    String typeLit = rs.getString("typeLit");
                    int prixBase = rs.getInt("prixBase");
                    int surplusPrix = rs.getInt("prixTotalCommodite");
                    int prixTotal = prixBase + surplusPrix;
                    System.out.println(
                        "idChambre: " + idChambre + "\n" +
                        "nom de chambre: " + nom + "\n" +
                        "Type de lit: " + typeLit + "\n" +
                        "Prix total par nuit: " + prixTotal + "\n"
                    );
                    
                    // chambreLibre.put("idChambre", Integer.toString(idChambre));
                    // chambreLibre.put("nom", nom);
                    // chambreLibre.put("typeLit", typeLit);
                    // chambreLibre.put("description", description);
                    // chambreLibre.put("prixTotal", Integer.toString(prixTotal));
                }
                // for (HashMap chambre: chambresLibres) {
                //     System.out.println(
                //         "idChambre: " + chambre.get("idChambre") + "\n" +
                //         "nom: " + chambre.get("nom") + "\n" +
                //         "typeLit: " + chambre.get("typeLit") + "\n" +
                //         "description: " + chambre.get("description") + "\n" +
                //         "prixTotal: " + chambre.get("prixTotal") + "\n"
                //     );
                // }
            }

        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherChambresLibres dans TableChambre");
        }
    }

    /**
     * Afficher info chambre
     * 
     * @param idChambre
     * @throws SQLException
     */
    public void afficherChambre(int idChambre) throws SQLException{
        try {
            // update ps avec idChambre
            stmtAfficherChambre.setInt(1, idChambre);

            // execution query
            ResultSet rs = stmtAfficherChambre.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String typeLit = rs.getString("typeLit");
                int prixBase = rs.getInt("prixBase");

                // Afficher info chambre
                System.out.println(
                    "\nChambre idChambre: " + Integer.toString(idChambre) + "\n" +
                    "nom: " + nom + "\n" +
                    "typeLit: " + typeLit + "\n" +
                    "prixBase: " + prixBase + "\n"
                );

                // Afficher commodites si dispo
                ArrayList<String> commodites = new ArrayList<String>();
                do {
                    String descriptionCommodite = rs.getString("description");
                    String surplusPrixCommodite = Integer.toString(rs.getInt("surplusPrix"));

                    if (descriptionCommodite == null | surplusPrixCommodite == null) {
                        commodites.add("Aucune commodite");
                    } else {
                        commodites.add(descriptionCommodite + " (" + surplusPrixCommodite + "$)");
                        System.out.println(
                            "Commodite: " + descriptionCommodite + " (" + surplusPrixCommodite + "$)"
                        );
                    }
                } while (rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur afficherChambre dans TableChambre");
        } 
    }
}
