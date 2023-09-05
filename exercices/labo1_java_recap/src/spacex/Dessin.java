package spacex;

public class Dessin {
    public static void main (String[] args) {
        Dessin fusee = new Dessin();
        
        String resultat = fusee.dessinerCargo();
        resultat += fusee.dessinerCiterne(5);
        resultat += fusee.dessinerMoteur();
        System.out.println(resultat);
    }

    private String dessinerCargo() {
        String cargo = "  /\\  \n" + " /__\\  \n";
        return cargo;
    }

    private String dessinerCiterne(int nombreCiternes) {
        String citerne = "";
        for (int i = 0; i < nombreCiternes; i++) {
            citerne += " |  | \n";
        }
        citerne += "/____\\\n";

        return citerne;
    }

    private String dessinerMoteur() {
        String moteur = " /WW\\ ";
        return moteur;
    }
}
