package com.ift287lalonde;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TupleReservation {
    
    @Id
    @GeneratedValue
    private long id;

    // Attributs reservation
    private Date dateDebut;
    private Date dateFin;

    // relations reservation avec client et chambre
    @ManyToOne
    private TupleClient client;

    @ManyToOne
    private TupleChambre chambre;


    public TupleReservation(
        TupleClient client,
        TupleChambre chambre,
        Date dateDebut,
        Date dateFin
    ) {
        setClient(client);
        setChambre(chambre);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }

    // Getter et setters pour reservation
    public long getId() {
        return id;
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
    public TupleClient getClient() {
        return client;
    }

    public void setClient(TupleClient client) {
        this.client = client;
    }
    
    public TupleChambre getChambre() {
        return chambre;
    }
    public void setChambre(TupleChambre chambre) {
        this.chambre = chambre;
    }
}
