package com.ift287lalonde;

import java.util.List;

public class ManagerCommodite {
    
    private AccesChambre accesChambre;
    private AccesCommodite accesCommodite;

    public ManagerCommodite(
    AccesChambre accesChambre, 
    AccesCommodite accesCommodite
    ) throws IFT287Exception {
        if (accesChambre.getConnexion() != accesCommodite.getConnexion()) {
            throw new IFT287Exception(
                "Les instances de AccesChambre et AccesCommodite n'utilisent pas la meme connexion au serveur."
            );
        }
        this.accesChambre = accesChambre;
        this.accesCommodite = accesCommodite;
    }

    /**
     * Ajout commodite si n'existe pas dans db
     * 
     * @param commodite
     * @throws IFT287Exception
     */
    public void ajouterCommodite(TupleCommodite commodite) throws IFT287Exception {
        // Check si commodite n'existe pas
        if (accesCommodite.commoditeExiste(commodite.getId())) {
            throw new IFT287Exception(
                "Impossible ajouter commodite avec idCommodite=" + commodite.getId() + ": existe deja dans db."
                );
            }
        accesCommodite.ajouterCommodite(commodite);
    }

    /**
     * Supprimer commodite si existe dans db
     * 
     * @param idCommodite
     * @throws IFT287Exception
     */
    public void supprimerCommodite(int idCommodite) throws IFT287Exception {
        // Check si commodite existe
        if (!accesCommodite.commoditeExiste(idCommodite)) {
            throw new IFT287Exception(
                "Impossible supprimer commodite avec idCommodite=" + idCommodite + ": n'existe pas dans db."
            );
        }
        // Chercher commodite
        TupleCommodite commodite = accesCommodite.getCommodite(idCommodite);

        // Supprimer la reference de la commodite dans les chambres
        List <TupleChambre> chambres = accesChambre.getChambresCommodite(commodite);
        for (TupleChambre chambre : chambres) {
            accesChambre.enleverCommodite(chambre, commodite);
        }

        // Supprimer la commodite
        accesCommodite.supprimerCommodite(idCommodite);
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
        int idCommodite
    ) throws IFT287Exception {
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
        List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(idChambre);
        for (TupleCommodite commodite : commodites) {
            if (commodite.getId() == idCommodite) {
                throw new IFT287Exception(
                    "Impossible inclure commodite :\n" +
                    "Commodite avec idCommodite=" + idCommodite +
                    "existe deja dans la chambre idChambre=" + idChambre
                );
            }
        }
        // inclure la commodite a la chambre
        if (!accesCommodite.inclureCommodite(chambre, nouvelleCommodite)) {
            throw new IFT287Exception(
                "La commodite " + idCommodite + "n'a pas ete " +
                "incluse dans la chambre " + idChambre +
                "dans le document commodite."
            );
        };

        // Faire la mise a jours dans le document chambre
        if (!accesChambre.inclureCommodite(chambre, nouvelleCommodite)) {
            throw new IFT287Exception(
                "La commodite " + idCommodite + "n'a pas ete " +
                "incluse dans la chambre " + idChambre +
                "dans le document chambre."
            );
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
        int idCommodite
    ) throws IFT287Exception {
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
        List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(idChambre);
        for (TupleCommodite commodite : commodites) {
            if (commodite.getId() == idCommodite) {
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
        // enlever la chambre a la commodite
        if (!accesCommodite.enleverCommodite(chambre, nouvelleCommodite)) {
            throw new IFT287Exception(
                "La commodite " + idCommodite + "n'a pas ete " +
                "enlevee de la chambre " + idChambre +
                "dans le document commodite."
            );
        };

        // Enlever la commodite de la chambre
        if (!accesChambre.enleverCommodite(chambre, nouvelleCommodite)) {
            throw new IFT287Exception(
                "La commodite " + idCommodite + "n'a pas ete " +
                "enlevee de la chambre " + idChambre +
                "dans le document chambre."
            );
        }
    }
}
