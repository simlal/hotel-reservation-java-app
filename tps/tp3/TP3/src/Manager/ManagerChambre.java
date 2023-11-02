package Manager;

import java.util.Date;

import AubergeInn.Connexion;
import AubergeInn.IFT287Exception;
import Acces.AccesChambre;
import Tuples.TupleChambre;


public class ManagerChambre {
    
    private Connexion cx;
    private final AccesChambre accesChambre;
    
    public ManagerChambre(AccesChambre accesChambre) {
        this.cx = accesChambre.getConnexion();
        this.accesChambre = accesChambre;
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
     * Ajout chambre si n'existe pas dans db
     * 
     * @param chambre
     * @throws IFT287Exception
     */
    public void ajouterChambre(TupleChambre chambre) throws IFT287Exception {
        try {
            cx.demarreTransaction();
            accesChambre.ajouterChambre(chambre);

            // Check si chambre n'existe pas avant ajout
            if (accesChambre.chambreExiste((int)chambre.getId())) {
                throw new IFT287Exception(
                    "Impossible ajouter chambre avec idChambre=" + chambre.getId() + ": existe deja dans db."
                );
            }
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur ajouterChambre dans ManagerChambre");
        }
    }
    
    /**
     * Supprimer chambre si n'existe pas dans db ou si pas de reservation en cours
     * et pas de reservation dans le futur
     * 
     * @param idChambre
     * @throws IFT287Exception
     */
    public void supprimerChambre(int idChambre) throws IFT287Exception {
        try {
            cx.demarreTransaction();
            
            // Check si chambre existe et maj db
            if (!accesChambre.chambreExiste(idChambre)) {
                throw new IFT287Exception(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
                );
            }
            if (accesChambre.checkChambreReservationEnCours(idChambre)) {
                throw new IFT287Exception(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre a une reservation en cours."
                );
            }
            if (accesChambre.checkChambreReservationFuture(idChambre)) {
                throw new IFT287Exception(
                    "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre est reservee dans le futur."
                );
            }
           
            accesChambre.supprimerChambre(idChambre);
            cx.commit();

        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur supprimerChambre dans ManagerChambre");
        }
    }

    /**
     * Afficher chambres libres + prix total incluant les commodites
     * 
     * @param dateDebut
     * @param dateFin
     * @throws IFT287Exception
     */
    public void afficherChambresLibres(
        Date dateDebut, 
        Date dateFin
    ) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            // Validation entree date correcte
            if (dateFin.compareTo(dateDebut) <= 0) {
                throw new IFT287Exception(
                    "Impossible afficher ChambresLibres avec dateDebut=" + 
                    dateDebut.toString() + " et dateFin=" + dateFin.toString() +
                    ": dateFin doit etre plus grand que dateDebut."
                );
            }
            accesChambre.afficherChambresLibres(dateDebut, dateFin);
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur afficherChambresLibres dans ManagerChambre");
        }
    }

    /**
     * Afficher les informations dùne chambre
     * 
     * @param idChambre
     * @throws IFT287Exception
     */
    public void afficherChambre(int idChambre) throws IFT287Exception {
        try {
            cx.demarreTransaction();

            // Verifie si chambre existe
            if (!accesChambre.chambreExiste(idChambre)) {
                throw new IFT287Exception(
                    "Impossible afficher info chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
                );
            }
            accesChambre.afficherChambre(idChambre);
            cx.commit();
        } catch (IFT287Exception e) {
            cx.rollback();
            e.printStackTrace();
            throw new IFT287Exception("Erreur afficherInfoChambre dans ManagerChambre");
        }
    }
}
