package Tables;

import AubergeInn.Connexion;
import Tuples.TupleReservation;

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
    
    private final PreparedStatement stmtCheckChambreReserve;
    private final PreparedStatement stmtFaireReservation;

    public TableReservation (Connexion cx) throws Exception {
        this.cx = cx;
        try {
            this.stmtCheckChambreReserve = cx.getConnection().prepareStatement(sqlCheckChambreReserve);
            this.stmtFaireReservation = cx.getConnection().prepareStatement(sqlFaireReservation);
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
            Date datefin
        ) throws SQLException {
            try {
                // modif ps avec info reservation
                stmtCheckChambreReserve.setInt(1, idChambre);
                stmtCheckChambreReserve.setDate(2, dateDebut);
                stmtCheckChambreReserve.setDate(3, datefin);

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
}