package spacex;

public class Citerne {

    public String dessiner(int hauteur) {
        String citerne = "";
        for (int i = 0; i < hauteur; i++) {
            citerne += " |  | \n";
        }
        citerne += "/____\\\n";

        return citerne;
    }
    
}
