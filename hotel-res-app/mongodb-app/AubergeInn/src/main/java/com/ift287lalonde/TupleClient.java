package com.ift287lalonde;

import java.util.List;
import java.util.ArrayList;
import org.bson.Document;

public class TupleClient {

    private int idClient;
    private String prenom;
    private String nom;
    private int age;
    private List<Integer> reservationsId;

    public TupleClient(Document doc) {
        setId(doc.getInteger("idClient"));
        setPrenom(doc.getString("prenom"));
        setNom(doc.getString("nom"));
        reservationsId = doc.getList("reservationsId", Integer.class);
    }
    
    public TupleClient(int idClient, String prenom, String nom, int age) {
        setId(idClient);
        setPrenom(prenom);
        setNom(nom);
        setAge(age);

        // Reservations assoc a client
        reservationsId = new ArrayList<Integer>();
    }

    // getters setters pour client
    public int getId() {
        return idClient;
    }
    public void setId(int idClient) {
        this.idClient = idClient;
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
    public List<Integer> getReservationsId() {
        return reservationsId;
    }

    public void ajouterReservation(int idReservation) {
        reservationsId.add(idReservation);
    }

    public void supprimerReservation(int idReservation) {
        reservationsId.remove(idReservation);
    }

    // creer document pour operation db
    public Document toDocument() {
        Document doc = new Document();
        doc.append("idClient", idClient)
            .append("prenom", prenom)
            .append("nom", nom)
            .append("age", age)
            .append("reservationsId", reservationsId);
        return doc;
    }

}

