package com.ift287lalonde;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class TupleChambre {
    private int idChambre;
    private String nom;
    private String typeLit;
    private int prixBase;

    private List<ObjectId> reservationsId;
    private List<Integer> commoditesId;

    public TupleChambre (Document doc) {
        // Chambre attrs
        setId(doc.getInteger("idChambre"));
        setNom(doc.getString("nom"));
        setTypeLit(doc.getString("typeLit"));
        setPrixBase(doc.getInteger("prixBase"));

        // Relations reservation et commodites
        this.reservationsId = doc.getList("reservationsId", ObjectId.class);
        this.commoditesId = doc.getList("commoditesId", Integer.class);
    }
    public TupleChambre (
        int idChambre,
        String nom,
        String typeLit,
        int prixBase
    ) {
        // Chambre attrs
        setId(idChambre);
        setNom(nom);
        setTypeLit(typeLit);
        setPrixBase(prixBase);

        // Relations reservation et commodites
        this.reservationsId = new ArrayList<>();
        this.commoditesId = new ArrayList<>();
    }

    public int getId() {
        return idChambre;
    }
    public void setId(int idChambre) {
        this.idChambre = idChambre;
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

    // getters pour relations reservation et commodites
    public List<ObjectId> getReservationsId() {
        return reservationsId;
    }
    public void ajouterReservation(ObjectId idReservation) {
        reservationsId.add(idReservation);
    }
    public void supprimerReservation(ObjectId idReservation) {
        reservationsId.remove(idReservation);
    }

    public List<Integer> getCommoditesId() {
        return commoditesId;
    }
    public void ajouterCommodite(int idCommodite) {
        commoditesId.add(idCommodite);
    }
    public void supprimerCommodite(int idCommodite) {
        commoditesId.remove(idCommodite);
    }

    // Conversion en document mongodb
    public Document toDocument() {
        Document doc = new Document();
        doc.append("idChambre", idChambre)
            .append("nom", nom)
            .append("typeLit", typeLit)
            .append("prixBase", prixBase)
            .append("reservationsId", reservationsId)
            .append("commoditesId", commoditesId);
        return doc;
    }
}
