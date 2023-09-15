package tp1;

public class Connectible {
    
    // Booleens pour certains attributs implied dans instance de connectible
    private String name;
    private int id;
    
    private boolean volumeAvailable;
    private double volume;
    private boolean lengthAvailable;
    private float length;    // Seulement 2 valeur avec 1 decimales reste int
    private boolean startRadiusAvailable;
    private double startRadius;
    private boolean endRadiusAvailable;
    private double endRadius;

    /**
     * Constructeur avec attr required et defaut a false pour tous implied
     * 
     * @param name
     * @param id
     */
    public Connectible(
        String name, 
        int id
    ) {
        setName(name);
        setId(id);
        this.volumeAvailable = false;
        this.lengthAvailable = false;
        this.startRadiusAvailable = false;
        this.endRadiusAvailable = false;
    }

    // Get ou set l'attribut required name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Get ou set l'attribut required id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour boolean statut attribut implied seulement
    // Getter et setter pour l'attribut implied lui-meme
    public boolean isVolumeAvailable() {
        return volumeAvailable;
    }

    public double getVolume() {
        if (!volumeAvailable) {
            throw new IllegalStateException("No volume attribute available.");
        }
        return volume;
    }
    public void setVolume(double volume) {
        this.volume = volume;
        this.volumeAvailable = true;
    }

    public boolean isLengthAvailable() {
        return lengthAvailable;
    }

    public float getLength() {
        if (!lengthAvailable) {
            throw new IllegalStateException("No length attribute available.");
        }
        return length;
    }
    public void setLength(float length) {
        this.length = length;
        this.lengthAvailable = true;
    }

    public boolean isStartRadiusAvailable() {
        return startRadiusAvailable;
    }

    public double getStartRadius() {
        if (!startRadiusAvailable) {
            throw new IllegalStateException("No startRadius attribute available.");
        }
        return startRadius;
    }
    public void setStartRadius(double startRadius) {
        this.startRadius = startRadius;
        this.startRadiusAvailable = true;
    }

    public boolean isEndRadiusAvailable() {
        return endRadiusAvailable;
    }

    public double getEndRadius() {
        if (!endRadiusAvailable) {
            throw new IllegalStateException("No endRadius attribute available.");
        }
        return endRadius;
    }
    public void setEndRadius(double endRadius) {
        this.endRadius = endRadius;
    }

    // Representation simplifie instance connectible
    @Override
    public String toString() {
        StringBuilder connectibleStringBuilder = new StringBuilder();
        connectibleStringBuilder.append("Connectible(")
            .append("id:").append(getId())
            .append(", name:").append(getName())
            .append(", volumeAvailable:").append(isVolumeAvailable())
            .append(", lengthAvailable:").append(isLengthAvailable())
            .append(", startRadiusAvailable:").append(isStartRadiusAvailable())
            .append(", endRadiusAvailable:").append(isEndRadiusAvailable())
            .append(")");
        return connectibleStringBuilder.toString();
    }
}
