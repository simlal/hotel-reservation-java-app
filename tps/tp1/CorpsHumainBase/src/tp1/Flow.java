package tp1;

import java.util.ArrayList;

public class Flow {
    
    private int id;
    private String name;
    private ArrayList <Connectible> connectiblesList;
    private ArrayList <Connection> connectionsList;

    /**
     * Constructeur pour element Flow incluant enfants liste de connectibles et connections
     * 
     * @param id
     * @param name
     * @param connectiblesList
     * @param connectionsList
     */
    public Flow(
        int id, 
        String name, 
        ArrayList <Connectible> connectiblesList, 
        ArrayList <Connection> connectionsList
    ) {
        setId(id);
        setName(name);
        setConnectiblesList(connectiblesList);
        setConnectionsList(connectionsList);
    }

    /**
     * Get l'attribut id
     * 
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * Set l'attribut id
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Get l'attribut name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Set l'attribut name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get l'attribut connectiblesList
     * 
     * @return connectiblesList
     */
    public ArrayList<Connectible> getConnectiblesList() {
        return connectiblesList;
    }
    /**
     * Set l'attribut connectiblesList
     * 
     * @param connectiblesList
     */
    public void setConnectiblesList(ArrayList<Connectible> connectiblesList) {
        this.connectiblesList = connectiblesList;
    }
    /**
     * Get l'attribut connectionsList
     * 
     * @return connectionsList
     */
    public ArrayList<Connection> getConnectionsList() {
        return connectionsList;
    }
    /**
     * Set l'attribut connectionsList
     * 
     * @param connectionsList
     */
    public void setConnectionsList(ArrayList<Connection> connectionsList) {
        this.connectionsList = connectionsList;
    }

    // Representation simplifie d'une instance de Flow
    @Override
    public String toString() {
        StringBuilder flowStringBuilder = new StringBuilder();
        flowStringBuilder.append("Flow(")
            .append("id:").append(getId())
            .append(", name:").append(getName())
            .append(", connectiblesListSize:").append(getConnectiblesList().size())
            .append(", connectionsListSize:").append(getConnectionsList().size())
            .append(")");
        return flowStringBuilder.toString();
    }
}
