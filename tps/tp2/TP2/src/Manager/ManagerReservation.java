package Manager;

import java.sql.SQLException;

import AubergeInn.Connexion;
import Tables.TableChambre;
import Tables.TableClient;
import Tables.TableReservation;
import Tuples.TupleReservation;

public class ManagerReservation {
    
    private Connexion cx;

    private final TableClient tableClient;
    private final TableChambre tableChambre;
    private final TableReservation tableReservation;

    public ManagerReservation(
        TableClient tableClient,
        TableChambre tableChambre,
        TableReservation tableReservation
        ) {
        this.cx = tableReservation.getConnexion();
        this.tableClient = tableClient;
        this.tableChambre = tableChambre;
        this.tableReservation = tableReservation;
        }

    public Connexion getConnexion() {
        return cx;
    }

    public void faireReservation(
        TupleReservation reservation
        ) throws SQLException {
            try {
                // Validation si client existe
                if (!tableClient.checkClient(reservation.getIdClient())) {
                    throw new SQLException(
                        "Impossible faire reservation avec idClient=" + reservation.getIdClient() + ": client n'existe pas dans db."
                    );
                // Validation chambre existe
                } else if (!tableChambre.checkChambre(reservation.getIdChambre())) {
                    throw new SQLException(
                        "Impossible faire reservation avec idChambre=" + reservation.getIdChambre() + ": chambre n'existe pas dans db."
                    );
                } else {
                    // Reservation de chambre si libre
                    if (!tableReservation.checkChambreReserve(
                        reservation.getIdChambre(),
                        reservation.getDateDebut(),
                        reservation.getDateFin()
                    )) {
                        tableReservation.reserver(reservation);
                        cx.commit();
                    } else {
                        throw new SQLException(
                            "Impossible faire reservation avec idChambre=" + reservation.getIdChambre() + ": chambre est deja reservee."
                        );
                    }
                }
        } catch (SQLException se) {
            cx.rollback();
            se.printStackTrace();
            throw new SQLException("Erreur faireReservation dans ManagerReservation");
        }
    }
}
