package Tuples;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class TupleChambre {
    
    @Id
    @GeneratedValue
    private long id;

    private String nom;
    private String typeLit;
    private int prixBase;

    @OneToMany(mappedBy = "chambre")
    private List<TupleReservation> reservations;

    @ManyToMany
    private List<TupleCommodite> commodites;

    public TupleChambre  (
        String nom,
        String typeLit,
        int prixBase
    ) {
        // Chambre attrs
        setNom(nom);
        setTypeLit(typeLit);
        setPrixBase(prixBase);

        // Relations reservation et commodites
        this.reservations = new ArrayList<>();
        this.commodites = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeLit() {
        return typeLit;
    }
    public void setTypeLit(String typeLit) {
        this.typeLit = typeLit;
    }

    public int getPrixBase() {
        return prixBase;
    }
    public void setPrixBase(int prixBase) throws IllegalArgumentException {
        if (prixBase <= 0) {
            throw new IllegalArgumentException("Prix de base doit etre positif");
        }
        this.prixBase = prixBase;
    }

    // getters et setters pour relations reservation et commodites
    public List<TupleReservation> getReservations() {
        return reservations;
    }
    public void ajouterReservation(TupleReservation reservation) {
        reservations.add(reservation);
    }
    public void supprimerReservation(TupleReservation reservation) {
        reservations.remove(reservation);
    }


    public List<TupleCommodite> getCommodites() {
        return commodites;
    }
    public void ajouterCommodite(TupleCommodite commodite) {
        commodites.add(commodite);
    }
    public void supprimerCommodite(TupleCommodite commodite) {
        commodites.remove(commodite);
    }
    
}
