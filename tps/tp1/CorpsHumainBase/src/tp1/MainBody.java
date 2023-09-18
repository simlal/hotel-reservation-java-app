package tp1;

import java.util.ArrayList;

public class MainBody {
    private String bodyName;
    private int id;
    private ArrayList<SystemEle> systemsList;
    private ArrayList <Organ> organsList;

    /**
     * Constructeur attributs de element seulement + listes vides
     * 
     * @param bodyName
     * @param id
     */
    public MainBody(String bodyName, int id) {
        setBodyName(bodyName);
        setId(id);
        this.systemsList = new ArrayList <SystemEle>();
        this.organsList = new ArrayList <Organ>();
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
     * Get l'attribut systemsList
     * 
     * @return
     */
    public ArrayList <SystemEle> getSystemsList() {
        return systemsList;
    }

    /**
     * Get l'attribut organsList
     * 
     * @return
     */
    public ArrayList <Organ> getOrgansList() {
        return organsList;
    }


    /**
     * Ajout element SystemEle a systemsList
     * 
     * @param systemEle
     */
    public void addSystemEle(SystemEle systemEle) {
        systemsList.add(systemEle);
    }

    /**
     * Ajout element Organ a organsList
     * 
     * @param organ
     */
    public void addOrgan(Organ organ) {
        organsList.add(organ);
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
