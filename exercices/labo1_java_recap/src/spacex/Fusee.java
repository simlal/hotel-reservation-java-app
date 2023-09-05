package spacex;

public class Fusee {
    private Cargo cargo;
    private Citerne citerne;
    private Moteur moteur;
    private int hauteur;
    
    public Fusee(int hauteur) {
        this.cargo = new Cargo();
        this.citerne = new Citerne();
        this.moteur = new Moteur();
        this.hauteur = hauteur; 
    }
    
    public static void main (String[] args) {
        Fusee fusee = new Fusee(3);
        System.out.println(fusee.dessiner());
    }

    private String dessiner() {
        String res;
        res = this.cargo.dessiner();
        res += this.citerne.dessiner(this.hauteur);
        res += this.moteur.dessiner();
        
        return res;
    }
}
