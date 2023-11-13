package tp1;

import java.util.ArrayList;

public class Flow {
    
    private int id;
    private String name;
    private ArrayList <Connectible> connectiblesList;
    private ArrayList <Connection> connectionsList;

    /**
     * Constructeur attributs de element seulement + listes vides
     * 
     * @param id
     * @param name
     */
    public Flow(int id, String name) {
        setId(id);
        setName(name);
        this.connectiblesList = new ArrayList <Connectible>();
        this.connectionsList = new ArrayList <Connection>();
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
     * Get l'attribut connectionsList
     * 
     * @return connectionsList
     */
    public ArrayList<Connection> getConnectionsList() {
        return connectionsList;
    }
    
    /**
     * Ajout element Connectible a connectiblesList
     * 
     * @param connectible
     */
    public void addConnectible(Connectible connectible) {
        connectiblesList.add(connectible);
    }

    /**
     * Ajout element connection a connectionsList
     * 
     * @param connection
     */
    public void addConnection(Connection connection) {
        connectionsList.add(connection);
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
