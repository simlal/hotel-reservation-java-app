package com.ift287lalonde;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class TupleCommodite {
    private int idCommodite;
    private String description;
    private int surplusPrix;

    private List<Integer> chambresId;

    public TupleCommodite(Document d) {
        setId(d.getInteger("idCommodite"));
        setDescription(d.getString("description"));
        setSurplusPrix(d.getInteger("surplusPrix"));
        this.chambresId = d.getList("chambresId", Integer.class);
    }

    public TupleCommodite(
        int idCommodite,
        String description,
        int surplusPrix
    ) {
        setId(idCommodite);
        setDescription(description);
        setSurplusPrix(surplusPrix);
        this.chambresId = new ArrayList<Integer>();
    }

    // Getters et setters attr commodite base
    public int getId() {
        return idCommodite;
    }
    public void setId(int idCommodite) {
        this.idCommodite = idCommodite;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getSurplusPrix() {
        return surplusPrix;
    }
    public void setSurplusPrix(int surplusPrix) throws IllegalArgumentException {
        if (surplusPrix <= 0) {
            throw new IllegalArgumentException("Surplus de prix doit etre positif");
        }
        this.surplusPrix = surplusPrix;
    }

    // Relation avec chambres
    public List<Integer> getChambresId() {
        return chambresId;
    }

    // Conversion pour document nongodb
    public Document toDocument() {
        Document doc = new Document();
        doc.append("idCommodite", idCommodite)
            .append("description", description)
            .append("surplusPrix", surplusPrix)
            .append("chambresId", chambresId);
        return doc;
    }

}
