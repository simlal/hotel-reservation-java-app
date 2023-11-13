package Tuples;

public class TupleClient {

    private int idClient;
    private String prenom;
    private String nom;
    private int age;
    
    public TupleClient(
        int idClient,
        String prenom,
        String nom,
        int age
    ) {
        setIdClient(idClient);
        setPrenom(prenom);
        setNom(nom);
        setAge(age);
    }

    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) throws IllegalArgumentException {
        if (age <= 0) {
            throw new IllegalArgumentException("Age doit etre positif");
        }
        this.age = age;
    }

    

    @Override
    public String toString() {
        return "TupleClient [idClient=" + idClient + ", prenom=" + prenom + ", nom=" + nom + ", age=" + age + "]";
    }
}
