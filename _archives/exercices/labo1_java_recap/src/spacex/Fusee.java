package spacex;

import java.lang.Math;

public class Fusee {
    private Cargo cargo;
    private Citerne citerne;
    private Moteur moteur;
    
    public static final double gravite = 9.8;
    public static final double vitesseOrbite = 9700;
    
    public Fusee(double masseCargo) {
        this.cargo = new Cargo(masseCargo);
        this.citerne = new Citerne(this.calculerMasseCombustible());
        this.moteur = new Moteur();
    }
    
    public static void main (String[] args) {
        Fusee fusee = new Fusee(0);
        System.out.println(fusee.dessiner());
    }

    private String dessiner() {
        String res;
        res = this.cargo.dessiner();
        res += this.citerne.dessiner();
        res += this.moteur.dessiner();
        
        return res;
    }

    private double calculerMasseSeche() {
        double masseSeche = this.cargo.getMasse() + Moteur.MASSE;
        return masseSeche;
    }

    public double calculerMasseCombustible() {
        double mf = this.calculerMasseSeche();
        double m0 = mf / Math.exp(-Fusee.vitesseOrbite / (Moteur.ISP * Fusee.gravite));
        double masseCombustible = m0 - mf;
        return masseCombustible;
    }
}
