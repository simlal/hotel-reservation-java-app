package tp1;

public class To {
    private int id;
    /**
     * Constructeur pour attribut implied id
     * 
     * @param id
     */
    public To(int id) {
        this.id = id;
    }

    // Get ou set l'attribut id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Representation simplifie instance to
    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append("To(")
            .append("id:").append(getId())
            .append(")");
        return toStringBuilder.toString();
    }
}
