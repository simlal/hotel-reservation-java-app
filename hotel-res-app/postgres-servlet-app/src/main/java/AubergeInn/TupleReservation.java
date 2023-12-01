package AubergeInn;

import java.sql.Date;

public class TupleReservation {
    
    // private int idReservation;
    private Date dateDebut;
    private Date dateFin;
    private int idClient;
    private int idChambre;

    public TupleReservation(
        Date dateDebut,
        Date dateFin,
        int idClient,
        int idChambre
    ) {
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        setIdClient(idClient);
        setIdChambre(idChambre);
    }

    // public int getIdReservation() {
    //     return idReservation;
    // }
    // public void setIdReservation(int idReservation) {
    //     this.idReservation = idReservation;
    // }

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

    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdChambre() {
        return idChambre;
    }
    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }
}
