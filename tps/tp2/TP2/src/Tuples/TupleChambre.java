package Tuples;

public class TupleChambre {
    
    private int idChambre;
    private String nom;
    private String type;
    private double prixBase;

    public TupleChambre  (
        int idChambre,
        String nom,
        String type,
        double prixBase
    ) {
        this.idChambre = idChambre;
        this.nom = nom;
        this.type = type;
        this.prixBase = prixBase;
    }

    public int getIdChambre() {
        return idChambre;
    }
    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public double getPrixBase() {
        return prixBase;
    }
    public void setPrixBase(double prixBase) throws IllegalArgumentException {
        if (prixBase <= 0) {
            throw new IllegalArgumentException("Prix de base doit etre positif");
        }
        this.prixBase = prixBase;
    }
    
}
