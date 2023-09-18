package tp1;

import java.util.ArrayList;

public class Connection {
    private int id;
    private ArrayList <To> tosList;

    /**
     * Constructeur attributs de element seulement + listes vides
     * 
     * @param id
     */
    public Connection(int id) {
        setId(id);
        this.tosList = new ArrayList <To>();
    }

    // Get ou set l'attribut id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Get ou set l'attribut tosList
    public ArrayList<To> getTosList() {
        return tosList;
    }
    
    /**
     * Ajout un element To a la tosList
     * @param to
     */
    public void addTo(To to) {
        tosList.add(to);
    }

    // Representation simplifie instance connection
    @Override
    public String toString() {
        StringBuilder connectionStringBuilder = new StringBuilder();
        connectionStringBuilder.append("Connection(")
            .append("id:").append(getId())
            .append(", tosListSize:").append(getTosList().size())
            .append(")");
        return connectionStringBuilder.toString();
    }
    
}
