package AubergeInn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerReservation {
    
    private Connexion cx;

    private final TableClient tableClient;
    private final TableChambre tableChambre;
    private final TableReservation tableReservation;

    public ManagerReservation(
        TableClient tableClient,
        TableChambre tableChambre,
        TableReservation tableReservation
        ) throws IFT287Exception {
        this.cx = tableReservation.getConnexion();
        if (
                tableClient.getConnexion() != tableChambre.getConnexion() ||
                        tableClient.getConnexion() != tableReservation.getConnexion() ||
                        tableChambre.getConnexion() != tableReservation.getConnexion()
        ) {
            throw new IFT287Exception(
                    "Les instances de TableClient, TableChambre et/ou TableReservation n'utilisent pas la meme connexion au serveur."
            );
        }
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
                } else if (reservation.getDateFin().compareTo(reservation.getDateDebut()) <= 0){
                    throw new SQLException(
                        "Impossible faire reservation avec dateDebut=" + 
                        reservation.getDateDebut().toString() + " et dateFin=" + 
                        reservation.getDateFin().toString() +
                        ": dateFin doit etre plus grand que dateDebut."
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

    public List<TupleReservation> getListReservations() throws SQLException {
        List<TupleReservation> reservations = new ArrayList<>();
        try {
            reservations = tableReservation.getListReservations();
            cx.commit();
            return reservations;
        } catch (SQLException e) {
            cx.rollback();
            e.printStackTrace();
            throw new SQLException("Erreur getListReservations dans ManagerReservation");
        }
    }
}
