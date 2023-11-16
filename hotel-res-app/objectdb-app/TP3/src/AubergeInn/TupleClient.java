package AubergeInn;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
public class TupleClient {
    @Id
    @GeneratedValue
    private long id;

    private String prenom;
    private String nom;
    private int age;

    @OneToMany(mappedBy = "client")
    private List<TupleReservation> reservations;

    public TupleClient(String prenom, String nom, int age) {
        setPrenom(prenom);
        setNom(nom);
        setAge(age);

        // Reservations assoc a client
        reservations = new ArrayList<TupleReservation>();
    }

    // getters setters pour client
    public long getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) throws IllegalArgumentException {
        if (age <= 0) {
            throw new IllegalArgumentException("Age doit etre positif");
        }
        this.age = age;
    }

    // Lien a une ou plusieurs reservations
    public List<TupleReservation> getReservations() {
        return reservations;
    }

    public void ajouterReservation(TupleReservation reservation) {
        reservations.add(reservation);
    }

    public void supprimerReservation(TupleReservation reservation) {
        reservations.remove(reservation);
    }

}

