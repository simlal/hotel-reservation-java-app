package tp1;

import java.util.ArrayList;

public class Connection {
    private int id;
    private ArrayList <To> tosList;

    /**
     * Constructeur pour attributs required id et tosList
     * 
     * @param id
     * @param tosList
     */
    public Connection(int id, ArrayList <To> tosList) {
        setId(id);
        setTosList(tosList);
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
    public void setTosList(ArrayList<To> tosList) {
        this.tosList = tosList;
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
