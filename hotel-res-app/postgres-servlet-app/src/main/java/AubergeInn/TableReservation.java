package AubergeInn;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableReservation {

    private Connexion cx;
    
    private final static String sqlCheckChambreReserve = 
    "select * from Reservation where idChambre = ? and dateDebut < ? and dateFin > ?";
    private final static String sqlFaireReservation = 
    "insert into Reservation (dateDebut, dateFin, idClient, idChambre) "+
    "values (?,?,?,?)";
    private final static String sqlSupprimerReservationChambre = 
    "delete from Reservation where idChambre = ?";
    
    private final PreparedStatement stmtCheckChambreReserve;
    private final PreparedStatement stmtFaireReservation;
    private final PreparedStatement stmtSupprimerReservationChambre;

    public TableReservation (Connexion cx) throws Exception {
        this.cx = cx;
        try {
            this.stmtCheckChambreReserve = cx.getConnection().prepareStatement(sqlCheckChambreReserve);
            this.stmtFaireReservation = cx.getConnection().prepareStatement(sqlFaireReservation);
            this.stmtSupprimerReservationChambre = cx.getConnection().prepareStatement(sqlSupprimerReservationChambre);
        } catch (Exception se) {
            se.printStackTrace();
            throw new Exception("Erreur prepareStatement dans TableReservation");
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    /**
     * @param idChambre
     * @param dateDebut
     * @param datefin
     * @return chambreReservee
     * @throws SQLException
     */
    public boolean checkChambreReserve(
            int idChambre,
            Date dateDebut,
            Date dateFin
        ) throws SQLException {
            try {
                // modif ps avec info reservation
                stmtCheckChambreReserve.setInt(1, idChambre);
                stmtCheckChambreReserve.setDate(2, dateFin);
                stmtCheckChambreReserve.setDate(3, dateDebut);

                // Verif si chambre est reservee
                ResultSet rs = stmtCheckChambreReserve.executeQuery();
                boolean chambreReservee = rs.next();
                return chambreReservee;
            } catch (SQLException se) {
                se.printStackTrace();
                throw new SQLException("Erreur checkChambreReserve dans TableReservation");
            }
        }

    /**
     * Operation faire reservation dans une chambre
     * 
     * @param reservation
     * @throws Exception
     */
    public int reserver(
        TupleReservation reservation
        ) throws SQLException {
            try {

                // Modif du ps avec info reservation
                stmtFaireReservation.setDate(1, reservation.getDateDebut());
                stmtFaireReservation.setDate(2, reservation.getDateFin());
                stmtFaireReservation.setInt(3, reservation.getIdClient());
                stmtFaireReservation.setInt(4, reservation.getIdChambre());

                // Ajout de la reservation
                int nbReservation = stmtFaireReservation.executeUpdate();
                return nbReservation;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur reserver dans TableReservation");
        }
    }

    /**
     * Operation supprimer reservation dans une chambre
     * 
     * @param idChambre
     * @throws Exception
     */
    public int supprimerReservationChambre(
        int idChambre
        ) throws SQLException {
            try {

                // Modif du ps avec info reservation
                stmtSupprimerReservationChambre.setInt(1, idChambre);

                // Suppression de la reservation
                int nbReservation = stmtSupprimerReservationChambre.executeUpdate();
                return nbReservation;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur supprimerReservationChambre dans TableReservation");
        }
    }
}
