package spacex;

public class Citerne {

    public static final double DENSOXYGENE = 1.141 * 1000;
    public static final double DENSKERO = 1.020 * 1000;
    public static final double DIAMETRE = Moteur.DIAMETRE;

    private double hauteur;
    private double masseCombustible;

    public Citerne(double masseCombustible) {
        this.masseCombustible = masseCombustible;
        this.hauteur = determinerHauteurCiterne();
    }
    
    public String dessiner() {
        String citerne = "";
        for (int i = 0; i < this.hauteur; i++) {
            citerne += " |  | \n";
        }
        citerne += "/____\\\n";

        return citerne;
    }
    
    private double calculerMasseKero() {
        // Calcul masse kerosene
        double mk = this.masseCombustible / (1 + 2.56);
        return mk;
    }

    private double calculerMasseOxy(double masseKero) {
        double mlox = this.masseCombustible - masseKero;
        return mlox;
    }

    private double calculerVolLiquideCiterne() {
        // Get masses Oxy + Kero
        double masseKero = this.calculerMasseKero();
        double masseOxy = this.calculerMasseOxy(masseKero);

        // Calcul volumes oxy + kero (v = m / p)
        double volumeOxy = masseOxy / Citerne.DENSOXYGENE;
        double volumeKero = masseKero / Citerne.DENSKERO;
        double volumeTotal = volumeOxy + volumeKero;
        return volumeTotal;
    }

    private double determinerHauteurCiterne() {
        double rayon = Citerne.DIAMETRE / 2;
        double hauteur = this.calculerVolLiquideCiterne() / (Math.PI * Math.pow(rayon, 2));
        return hauteur;
    }
    
}
