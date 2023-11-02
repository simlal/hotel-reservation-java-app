package Acces;

import Tuples.TupleChambre;
import Tuples.TupleCommodite;

import javax.persistence.TypedQuery;

import AubergeInn.Connexion;

public class AccesCommodite {
    
    private Connexion cx;

    private static final String queryCheckCommodite = 
        "select commodite from TupleCommodite commodite where commodite.id = :idCommodite";

    private TypedQuery<TupleCommodite> stmtCheckCommodite;

    public AccesCommodite(Connexion cx) {
        this.cx = cx;

        this.stmtCheckCommodite = cx.getConnection().createQuery(queryCheckCommodite, TupleCommodite.class);
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
