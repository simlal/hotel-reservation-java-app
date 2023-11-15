package com.ift287lalonde;

import javax.print.Doc;

// import com.ift287lalonde.TupleChambre;
// import com.ift287lalonde.TupleCommodite;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

// import com.ift287lalonde.Connexion;

public class AccesCommodite {
    
    private Connexion cx;
    private MongoCollection<Document> commoditesCollection;

    public AccesCommodite(Connexion cx) {
        this.cx = cx;
        this.commoditesCollection = cx.getDatabase().getCollection("commodites");
    }

    /**
     * Retourne la connexion
     * 
     * @return Connexion
     */
    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Retourne la collection
     * 
     * @return commoditeCollection
     */
    public MongoCollection<Document> getCommoditesCollection() {
        return commoditesCollection;
    }

    /**
     * Verifie si commodite existe
     * 
     * @param idCommodite
     * @return true si la commodite existe, false sinon
     */
    public boolean commoditeExiste(int idCommodite) {
        stmtCheckCommodite.setParameter("idCommodite", idCommodite);
        return !stmtCheckCommodite.getResultList().isEmpty();
    }

    /**
     * Cherche la commodite avec l'id fourni
     * 
     * @param idCommodite
     * @return TupleCommodite
     */
    public TupleCommodite getCommodite(int idCommodite) {
        if (commoditeExiste(idCommodite)) {
            stmtCheckCommodite.setParameter("idCommodite", idCommodite);
            return stmtCheckCommodite.getSingleResult();
        } else {
            return null;
        }
    }

    /**
     * Ajoute une commodite dans la db
     * 
     * @param commodite
     */
    public void ajouterCommodite(TupleCommodite commodite) {
            cx.getConnection().persist(commodite);
    }

    /**
     * Supprime une commodite de la db
     *
     * @param idCommodite 
     */
    public void supprimerCommodite(int idCommodite) {
        TupleCommodite commodite = getCommodite(idCommodite);

        if (commodite == null) {
            throw new IllegalArgumentException("Commodite inexistante :" + idCommodite);
        }
        cx.getConnection().remove(commodite);
    }

    /**
     * Inclusion d'une commodite dans une chambre
     * 
     * @param chambre
     * @param commodite
     */
    public void inclureCommodite(TupleChambre chambre, TupleCommodite commodite) {
        chambre.ajouterCommodite(commodite);
    }

    /**
     * Exclusion d'une commodite dans une chambre
     * 
     * @param chambre
     * @param commodite
     */
    public void enleverCommodite(TupleChambre chambre, TupleCommodite commodite) {
        chambre.supprimerCommodite(commodite);
    }
}
