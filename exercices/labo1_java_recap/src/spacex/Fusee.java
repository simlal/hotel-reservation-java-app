package spacex;

import java.lang.Math;

public class Fusee {
    private Cargo cargo;
    private Citerne citerne;
    private Moteur moteur;
    private int hauteur;
    
    public static final double gravite = 9.8;
    public static final double vitesseOrbite = 9700;
    
    public Fusee(int hauteur, double masseCargo) {
        this.cargo = new Cargo(masseCargo);
        this.citerne = new Citerne();
        this.moteur = new Moteur();
        this.hauteur = hauteur; 
    }
    
    public static void main (String[] args) {
        Fusee fusee = new Fusee(0, 1000);
        System.out.println(fusee.calculerMasseCombustible());
    }

    private String dessiner() {
        String res;
        res = this.cargo.dessiner();
        res += this.citerne.dessiner(this.hauteur);
        res += this.moteur.dessiner();
        
        return res;
    }

    private double calculerMasseSeche() {
        double masseSeche = this.cargo.getMasse() + Moteur.MASSE;
        return masseSeche;
    }

    private double calculerMasseCombustible() {
        double mf = this.calculerMasseSeche();
        double m0 = mf / Math.exp(-Fusee.vitesseOrbite / (Moteur.ISP * Fusee.gravite));
        double masseCombustibe = m0 - mf;
        return masseCombustibe;
    }
}
