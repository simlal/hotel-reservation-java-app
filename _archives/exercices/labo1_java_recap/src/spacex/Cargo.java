package spacex;

public class Cargo {
    private double masse;

    public Cargo(double masse) {
        this.masse = masse;
    }

    public String dessiner() {
        String cargo = "  /\\  \n" + " /__\\  \n";
        return cargo;
    }

    public double getMasse() {
        return masse;
    }
    
}
