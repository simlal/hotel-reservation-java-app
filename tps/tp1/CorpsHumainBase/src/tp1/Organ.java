package tp1;

public class Organ {
    private String name;
    private int id;
    private int systemId;

    public Organ(String name, int id, int systemId) {
        setName(name);
        setId(id);
        setSystemId(systemId);
    }

    /**
     * Get l'attribut name
     * 
     * @return name
     */
    public String getName() {
        return this.name;
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
     * Get l'attribut id
     * 
     * @return id
     */
    public int getId() {
        return this.id;
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
     * Get l'attribut systemId
     * 
     * @return systemId
     */
    public int getSystemId() {
        return this.systemId;
    }

    /**
     * Set l'attribut systemId
     * 
     * @param systemId
     */
    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    // Representation simplifie d'une instance de Organ
    @Override
    public String toString() {
        StringBuilder organStringBuilder = new StringBuilder();
        organStringBuilder.append("Organ(")
            .append("name:").append(getName())
            .append(", id:").append(getId())
            .append(", systemId:").append(getSystemId())
            .append(")");
        return organStringBuilder.toString();
    }
}
