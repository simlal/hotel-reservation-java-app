package Tuples;

public class TupleChambreCommodite {
    
    private int idChambre;
    private int idCommodite;
    private int nombre;

    public TupleChambreCommodite(
        int idChambre,
        int idCommodite
    ) {
        this.idChambre = idChambre;
        this.idCommodite = idCommodite;
    }
    
    public int getIdChambre() {
        return idChambre;
    }
    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public int getIdCommodite() {
        return idCommodite;
    }
    public void setIdCommidte(int idCommodite) {
        this.idCommodite = idCommodite;
    }

    public int getNombre() {
        return nombre;
    }
    public void setNombre(int nombre) throws IllegalArgumentException {
        if (nombre < 0) {
            throw new IllegalArgumentException("Nombre de commodite doit etre >= 0");
        }
        this.nombre = nombre;
    }
}
