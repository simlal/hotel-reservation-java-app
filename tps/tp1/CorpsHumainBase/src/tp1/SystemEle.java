package tp1;

import java.util.ArrayList;
import java.util.concurrent.Flow;

public class SystemEle {
    
    private int id;
    private String name;
    private int type;
    private ArrayList <Flow> flowsList;

    /**
     * Constructeur pour element System avec incluant enfants flowList
     * 
     * @param id
     * @param name
     * @param type
     * @param flowsList
     */
    public SystemEle (
        int id, 
        String name, 
        int type,
        ArrayList <Flow> flowsList) {
        setId(id);
        setName(name);
        setType(type);
        setFlowsList(flowsList);
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
     * Sets l'attribut id
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
     * Sets l'attribut name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get l'attribut type
     * 
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets l'attribut type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Get l'attribut flowsList
     * 
     * @return flowsList
     */
    public ArrayList <Flow> getFlowsList() {
        return flowsList;
    }

    /**
     * Sets l'attribut flowsList
     */
    public void setFlowsList(ArrayList <Flow> flowsList) {
        this.flowsList = flowsList;
    }


    // Representation simplifie d'une instance de SystemEle
    @Override
    public String toString() {
        StringBuilder systemEleStringBuilder = new StringBuilder();
        systemEleStringBuilder.append("SystemEle(")
            .append("id:").append(getId())
            .append(", name:").append(getName())
            .append(", type:").append(getType())
            .append(", flowsListSize:").append(getFlowsList().size())
            .append(")");
        return systemEleStringBuilder.toString();
    }
}
