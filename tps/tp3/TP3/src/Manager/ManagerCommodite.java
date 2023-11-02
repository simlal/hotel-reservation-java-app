package Manager;

import java.util.List;

import Tuples.TupleChambre;
import Tuples.TupleCommodite;
import Acces.AccesCommodite;
import Acces.AccesChambre;
import AubergeInn.Connexion;
import AubergeInn.IFT287Exception;

public class ManagerCommodite {
    
    private Connexion cx;
    private AccesChambre accesChambre;
    private AccesCommodite accesCommodite;

    public ManagerCommodite(AccesChambre accesChambre, AccesCommodite accesCommodite) {
        this.cx = accesCommodite.getConnexion();
        this.accesChambre = accesChambre;
        this.accesCommodite = accesCommodite;
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
     * Ajout commodite si n'existe pas dans db
     * 
     * @param commodite
     * @throws IFT287Exception
     */
    public void ajouterCommodite(TupleCommodite commodite) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            accesCommodite.ajouterCommodite(commodite);
            // Check si commodite n'existe pas
            if (accesCommodite.commoditeExiste((int)commodite.getId())) {
                throw new IFT287Exception(
                    "Impossible ajouter commodite avec idCommodite=" + commodite.getId() + ": existe deja dans db."
                );
            }
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur ajouterCommodite dans ManagerCommodite");
        }
    }

    /**
     * Supprimer commodite si existe dans db
     * 
     * @param idCommodite
     * @throws IFT287Exception
     */
    public void supprimerCommodite(int idCommodite) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            // Check si commodite existe
            if (!accesCommodite.commoditeExiste(idCommodite)) {
                throw new IFT287Exception(
                    "Impossible supprimer commodite avec idCommodite=" + idCommodite + ": n'existe pas dans db."
                );
            }
            accesCommodite.supprimerCommodite(idCommodite);
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur supprimerCommodite dans ManagerCommodite");
        }
    }

    /**
     * Inclusion d'une commodite a une chambre si chambre et commodite existent
     * et si la commodite n'est pas deja incluse dans la chambre
     * 
     * @param idChambre
     * @param idCommodite
     * @throws IFT287Exception
     */
    public void inclureCommodite(
        int idChambre, 
        int idCommodite) throws IFT287Exception {
        try {
            cx.demarreTransaction();
            // Verification si chambre existe
            if (!accesChambre.chambreExiste(idChambre)) {
                throw new IFT287Exception(
                    "Impossible inclure commodite car chambre " + 
                    "idChambre=" + idChambre +" n'existe pas."
                );
            }

            // Verification si commodite existe
            if (!accesCommodite.commoditeExiste(idCommodite)) {
                throw new IFT287Exception(
                    "Impossible inclure commodite car commodite " + 
                    "idCommodite=" + idCommodite + " n'existe pas."
                );
            }

            // Chercher la chambre et commodite dans db
            TupleChambre chambre = accesChambre.getChambre(idChambre);
            TupleCommodite nouvelleCommodite = accesCommodite.getCommodite(idCommodite);

            // Verif si une commodite existe deja dans la chambre
            List<TupleCommodite> commodites = chambre.getCommodites();
            for (TupleCommodite commodite : commodites) {
                if ((int)commodite.getId() == idCommodite) {
                    throw new IFT287Exception(
                        "Impossible inclure commodite :\n" +
                        "Commodite avec idCommodite=" + idCommodite +
                        "existe deja dans la chambre idChambre=" + idChambre
                    );
                }
            }
            // inclure la commodite a la chambre
            accesCommodite.inclureCommodite(chambre, nouvelleCommodite);
            cx.commit();

        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur inclureCommodite dans ManagerCommodite.");
        }
    }

    /**
     * Enleve une commodite a une chambre si chambre et commodite existent
     * et si commodite est incluse dans la chambre
     * 
     * @param idChambre
     * @param idCommodite
     * @throws IFT287Exception
     */
    public void enleverCommodite(
        int idChambre, 
        int idCommodite) throws IFT287Exception {
        try {
            cx.demarreTransaction();
            
            // Verification si chambre existe
            if (!accesChambre.chambreExiste(idChambre)) {
                throw new IFT287Exception(
                    "Impossible inclure commodite car chambre " + 
                    "idChambre=" + idChambre +" n'existe pas."
                );
            }

            // Verification si commodite existe
            if (!accesCommodite.commoditeExiste(idCommodite)) {
                throw new IFT287Exception(
                    "Impossible inclure commodite car commodite " + 
                    "idCommodite=" + idCommodite + " n'existe pas."
                );
            }

            // Chercher la chambre et commodite dans db
            TupleChambre chambre = accesChambre.getChambre(idChambre);
            TupleCommodite nouvelleCommodite = accesCommodite.getCommodite(idCommodite);

            // Verif si la commodite existe dans la chambre d'interet
            boolean commoditeIncluse = false;
            List<TupleCommodite> commodites = chambre.getCommodites();
            for (TupleCommodite commodite : commodites) {
                if ((int)commodite.getId() == idCommodite) {
                    commoditeIncluse = true;
                    break;
                }
            }
            if (!commoditeIncluse) {
                throw new IFT287Exception(
                    "Impossible enlever commodite :\n" +
                    "Commodite avec idCommodite=" + idCommodite +
                    "n'existe pas dans la chambre idChambre=" + idChambre
                );
            }
            // inclure la commodite a la chambre
            accesCommodite.enleverCommodite(chambre, nouvelleCommodite);
            cx.commit();

        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur inclureCommodite dans ManagerCommodite.");
        }
    }
}
