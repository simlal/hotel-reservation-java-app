package AubergeInn;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableReservation {

    private Connexion cx;
    
    private final static String sqlCheckChambreReserve = 
    "select * from Reservation where idChambre = ? and dateDebut < ? and dateFin > ?";
    private final static String sqlFaireReservation = 
    "insert into Reservation (dateDebut, dateFin, idClient, idChambre) "+
    "values (?,?,?,?)";
    private final static String sqlSupprimerReservationChambre = 
    "delete from Reservation where idChambre = ?";
    private final static String sqlGetListReservations = "select * from Reservation";
    private final static String sqlGetReservationsClient ="select * from Reservation where idClient = ?";
    
    private final PreparedStatement stmtCheckChambreReserve;
    private final PreparedStatement stmtFaireReservation;
    private final PreparedStatement stmtSupprimerReservationChambre;
    private final PreparedStatement stmtGetListReservations;
    private final PreparedStatement stmtGetReservationsClient;

    public TableReservation (Connexion cx) throws SQLException {
        this.cx = cx;
        try {
            this.stmtCheckChambreReserve = cx.getConnection().prepareStatement(sqlCheckChambreReserve);
            this.stmtFaireReservation = cx.getConnection().prepareStatement(sqlFaireReservation);
            this.stmtSupprimerReservationChambre = cx.getConnection().prepareStatement(sqlSupprimerReservationChambre);
            this.stmtGetListReservations = cx.getConnection().prepareStatement(sqlGetListReservations);
            this.stmtGetReservationsClient = cx.getConnection().prepareStatement(sqlGetReservationsClient);

        } catch (SQLException se) {
            se.printStackTrace();
            throw new SQLException("Erreur prepareStatement dans TableReservation");
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
                rs.close();
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

    public List<TupleReservation> getListReservations() throws SQLException {
        List<TupleReservation> reservations = new ArrayList<>();
        try {
            ResultSet rs = stmtGetListReservations.executeQuery();
            while (rs.next()) {
                Date dateDebut = rs.getDate(1);
                Date dateFin = rs.getDate(2);
                int idClient = rs.getInt(3);
                int idChambre = rs.getInt(4);

                TupleReservation reservation = new TupleReservation(dateDebut, dateFin, idClient, idChambre);
                reservations.add(reservation);
            }
            rs.close();
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getListReservations dans TableReservation");
        }
    }
    public List<TupleReservation> getReservationsClient(int idClient) throws SQLException {
        List<TupleReservation> reservationsDeClient = new ArrayList<>();
        try {
            stmtGetReservationsClient.setInt(1, idClient);
            ResultSet rs = stmtGetReservationsClient.executeQuery();

            while (rs.next()) {
                Date dateDebut = rs.getDate(1);
                Date dateFin = rs.getDate(2);
                int idClientRs = rs.getInt(3);
                int idChambre = rs.getInt(4);

                TupleReservation reservation = new TupleReservation(dateDebut, dateFin, idClientRs, idChambre);

                reservationsDeClient.add(reservation);
            }
            rs.close();
            return reservationsDeClient;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getReservationsDeClient dans TableReservation");
        }
    }
}
