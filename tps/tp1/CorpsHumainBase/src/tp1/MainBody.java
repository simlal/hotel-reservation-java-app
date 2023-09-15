package tp1;

import java.util.ArrayList;

public class MainBody {
    private String bodyName;
    private int id;
    private ArrayList<SystemEle> systemsList;
    private ArrayList <Organ> organsList;

    /**
     * Constructeur element racine et enfants systemsList + organsList 
     * 
     * @param bodyName
     * @param id
     * @param systemsList
     * @param organsList
     */
    public MainBody(
        String bodyName,
        int id,
        ArrayList <SystemEle> systemsList,
        ArrayList <Organ> organsList
    ) {
        setBodyName(bodyName);
        setId(id);
        setSystemsList(systemsList);
        setOrgansList(organsList);
    }

    /**
     * Get l'attribut bodyName
     * 
     * @return bodyName
     */
    public String getBodyName() {
        return bodyName;
    }

    /**
     * Sets l'attribut bodyName
     */
    public void setBodyName(String bodyName) {
        this.bodyName = bodyName;
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
     * Get l'attribut systemCont
     * 
     * @return
     */
    public ArrayList <SystemEle> getSystemsList() {
        return systemsList;
    }

    /**
     * Sets l'attribut systemCont
     * 
     * @param systemCont
     */
    public void setSystemsList(ArrayList <SystemEle> systemsList) {
        this.systemsList = systemsList;
    }

    /**
     * Get l'attribut Organs
     * 
     * @return
     */
    public ArrayList <Organ> getOrgansList() {
        return organsList;
    }

    /**
     * Sets l'attribut organs
     * 
     * @param organs
     */
    public void setOrgansList(ArrayList <Organ> organsList) {
        this.organsList = organsList;
    }

    // Representation simplifie d'une instance de MainBody
    @Override
    public String toString() {
        StringBuilder mainBodyStringBuilder = new StringBuilder();
        mainBodyStringBuilder.append("MainBody(")
            .append("bodyName:").append(getBodyName())
            .append(", id:").append(getId())
            .append(", systemsListSize:").append(getSystemsList().size())
            .append(", organsListSize:").append(getOrgansList().size())
            .append(")");
        return mainBodyStringBuilder.toString();
    }
}
