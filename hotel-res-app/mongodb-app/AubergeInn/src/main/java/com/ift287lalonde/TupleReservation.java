package com.ift287lalonde;

import java.util.Date;
import org.bson.Document;

public class TupleReservation {
    
    // Attributs reservation
    private int idReservation;
    private Date dateDebut;
    private Date dateFin;

    // relations reservation avec client et chambre
    private int idClient;
    private int idChambre;

    public TupleReservation(Document doc) {
        setId(doc.getInteger("idReservation"));
        setClient(doc.getInteger("idClient"));
        setChambre(doc.getInteger("idChambre"));
        setDateDebut(doc.getDate("dateDebut"));
        setDateFin(doc.getDate("dateFin"));
    }

    public TupleReservation(
        int idReservation,
        int idClient,
        int idChambre,
        Date dateDebut,
        Date dateFin
    ) {
        setId(idReservation);
        setClient(idClient);
        setChambre(idChambre);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }

    // Getter et setters pour reservation
    public int getId() {
        return idReservation;
    }
    public void setId(int idReservation) {
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

    // Liens avec un client et une chambre
    public int getClient() {
        return idClient;
    }

    public void setClient(int idClient) {
        this.idClient = idClient;
    }
    
    public int getChambre() {
        return idChambre;
    }
    public void setChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    // Conversion pour document mongodb
    public Document toDocument() {
        Document doc = new Document();
        doc.append("idReservation", idReservation)
            .append("idClient", idClient)
            .append("idChambre", idChambre)
            .append("dateDebut", dateDebut)
            .append("dateFin", dateFin);
        return doc;
    }
}
