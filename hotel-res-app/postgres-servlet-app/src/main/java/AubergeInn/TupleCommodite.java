package AubergeInn;

public class TupleCommodite {
    
    private int idCommodite;
    private String description;
    private int surplusPrix;

    public TupleCommodite(
        int idCommodite,
        String description,
        int surplusPrix
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

    public int getSurplusPrix() {
        return surplusPrix;
    }
    public void setSurplusPrix(int surplusPrix) throws IllegalArgumentException {
        if (surplusPrix <= 0) {
            throw new IllegalArgumentException("Surplus de prix doit etre positif");
        }
        this.surplusPrix = surplusPrix;
    }
}
