package Tuples;

import java.sql.Date;

public class TupleReservation {
    
    private int idReservation;
    private Date dateDebut;
    private Date dateFin;

    public TupleReservation(
        int idReservation,
        Date dateDebut,
        Date dateFin
    ) {
        setIdReservation(idReservation);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }

    public int getIdReservation() {
        return idReservation;
    }
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
