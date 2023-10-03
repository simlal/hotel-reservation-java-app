package Tuples;

public class TupleCommodite {
    
    private int idCommodite;
    private String description;
    private double surplusPrix;

    public TupleCommodite(
        int idCommodite,
        String description,
        double surplusPrix
    ) {
        this.idCommodite = idCommodite;
        this.description = description;
        this.surplusPrix = surplusPrix;
    }

    public int getIdCommodite() {
        return idCommodite;
    }
    public void setIdCommidte(int idCommodite) {
        this.idCommodite = idCommodite;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getSurplusPrix() {
        return surplusPrix;
    }
    public void setSurplusPrix(double surplusPrix) throws IllegalArgumentException {
        if (surplusPrix <= 0) {
            throw new IllegalArgumentException("Surplus de prix doit etre positif");
        }
        this.surplusPrix = surplusPrix;
    }
}
