package AubergeInn;

public class TupleChambre {
    
    private int idChambre;
    private String nom;
    private String typeLit;
    private int prixBase;

    public TupleChambre  (
        int idChambre,
        String nom,
        String typeLit,
        int prixBase
    ) {
        this.idChambre = idChambre;
        this.nom = nom;
        this.typeLit = typeLit;
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
    
}
