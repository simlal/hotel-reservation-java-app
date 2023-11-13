package Tuples;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.GeneratedValue;

@Entity
public class TupleCommodite {
    
    @Id
    @GeneratedValue
    private long id;

    private String description;
    private int surplusPrix;

    @ManyToMany(mappedBy = "commodites")
    private List<TupleChambre> chambres;

    public TupleCommodite(
        String description,
        int surplusPrix
    ) {
        setDescription(description);
        setSurplusPrix(surplusPrix);
        this.chambres = new ArrayList<>();
    }

    // Getters et setters attr commodite base
    public long getId() {
        return id;
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
    public List<TupleChambre> getChambres() {
        return chambres;
    }

    public void inclureCommoditeChambre(TupleChambre chambre) {
        chambres.add(chambre);
    }

    public void enleverCommoditeChambre(TupleChambre chambre) {
        chambres.remove(chambre);
    }

}
