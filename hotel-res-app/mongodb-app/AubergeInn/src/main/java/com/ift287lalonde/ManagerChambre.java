package com.ift287lalonde;

import java.util.Date;
import java.util.List;

public class ManagerChambre {
    
    private final AccesChambre accesChambre;
    private final AccesReservation accesReservation;
    private final AccesCommodite accesCommodite;
    
    public ManagerChambre(
        AccesChambre accesChambre,
        AccesReservation accesReservation,
        AccesCommodite accesCommodite
    ) throws IFT287Exception {
        if (accesChambre.getConnexion() != accesReservation.getConnexion() || accesChambre.getConnexion() != accesCommodite.getConnexion()) {
            throw new IFT287Exception(
                "Les instances de AccesChambre, AccesReservation et AccesCommodite n'utilisent pas la meme connexion au serveur."
            );
        }
        this.accesChambre = accesChambre;
        this.accesReservation = accesReservation;
        this.accesCommodite = accesCommodite;
    }

    /**
     * Ajout chambre si n'existe pas dans db
     * 
     * @param chambre
     * @throws IFT287Exception
     */
    public void ajouterChambre(TupleChambre chambre) throws IFT287Exception {
        // Check si chambre n'existe pas avant ajout
        if (accesChambre.chambreExiste((int)chambre.getId())) {
            throw new IFT287Exception(
                "Impossible ajouter chambre avec idChambre=" + chambre.getId() + ": existe deja dans db."
            );
        }
        // Ajouter chambre
        accesChambre.ajouterChambre(chambre);
    }
    
    /**
     * Supprimer chambre si n'existe pas dans db ou si pas de reservation en cours
     * et pas de reservation dans le futur
     * 
     * @param idChambre
     * @throws IFT287Exception
     */
    public void supprimerChambre(int idChambre) throws IFT287Exception {
        // Check si chambre existe et maj db
        if (!accesChambre.chambreExiste(idChambre)) {
            throw new IFT287Exception(
                "Impossible supprimer chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
            );
        }
        if (accesReservation.checkChambreReservee(idChambre, "enCours")) {
            throw new IFT287Exception(
                "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre a une reservation en cours."
            );
        }
        if (accesReservation.checkChambreReservee(idChambre, "futur")) {
            throw new IFT287Exception(
                "Impossible supprimer chambre avec idChambre=" + idChambre + ": chambre est reservee dans le futur."
            );
        }
        
        // Chercher la chambre
        TupleChambre chambre = accesChambre.getChambre(idChambre);

        // Supprimer la reference de la chambre dans les commodites
        List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(idChambre);
        for (TupleCommodite commodite : commodites) {
            accesCommodite.enleverCommodite(chambre, commodite);
        }

        // Supprimer chambre
        accesChambre.supprimerChambre(chambre.getId());

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
        // Validation entree date correcte
        if (dateFin.compareTo(dateDebut) <= 0) {
            throw new IFT287Exception(
                "Impossible afficher ChambresLibres avec dateDebut=" + 
                dateDebut.toString() + " et dateFin=" + dateFin.toString() +
                ": dateFin doit etre plus grand que dateDebut."
            );
        }
        // Chercher la liste de toute les chambres
        List<TupleChambre> chambres = accesChambre.listerChambres();
        // Verifier si la chambre est reservee pour periode
        for (TupleChambre chambre : chambres) {
            boolean chambreLibre = accesReservation.checkChambreLibre(
                chambre.getId(), 
                dateDebut, 
                dateFin
            );
            if (chambreLibre) {
                // Calculer le prix total de la chambre
                int prixTotal = chambre.getPrixBase();
                List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(chambre.getId());
                for (TupleCommodite commodite : commodites) {
                    prixTotal += commodite.getSurplusPrix();
                }
                // Afficher les informations de la chambre
                System.out.println(
                    "\nChambre: " + chambre.getNom() +
                    "\n\tidChambre: " + chambre.getId() +
                    "\n\tType de lit: " + chambre.getTypeLit() +
                    "\n\tPrix de base: " + chambre.getPrixBase() + "$" +
                    "\n\tPrix total: " + prixTotal + "$"
                );
            }
        }
}

    /**
     * Afficher les informations dùne chambre
     * 
     * @param idChambre
     * @throws IFT287Exception
     */
    public void afficherChambre(int idChambre) throws IFT287Exception {
        // Verifie si chambre existe
        if (!accesChambre.chambreExiste(idChambre)) {
            throw new IFT287Exception(
                "Impossible afficher info chambre avec idChambre=" + idChambre + ": n'existe pas dans db."
            );
        }
        // Chercher la chambre + prix de base
        TupleChambre chambre = accesChambre.getChambre(idChambre);
        int prixTotal = chambre.getPrixBase();

        // Chercher commodites associees a chambre
        List<TupleCommodite> commodites = accesCommodite.getCommoditesChambre(chambre.getId());

        // Representation en string des infos
        String infoCommodites = "";
        for (TupleCommodite commodite : commodites) {
            infoCommodites += commodite.getDescription() + "=";
            infoCommodites += commodite.getSurplusPrix() + "$" + ", ";
            prixTotal += commodite.getSurplusPrix();
        }
        // Enlever derniere virgule
        if (infoCommodites.endsWith(", ")) {
            infoCommodites = infoCommodites.substring(0, infoCommodites.length() - 2);
        }
        // Pour les chambres sans commodites
        if (infoCommodites.isEmpty()) {
            infoCommodites = "Aucune commodite";
        }

        // Afficher informations chambre
        System.out.println(
                "\nChambre: " + chambre.getNom() +
                        "\n\tidChambre: " + chambre.getId() +
                        "\n\tType de lit: " + chambre.getTypeLit() +
                        "\n\tPrix de base: " + chambre.getPrixBase() + "$" +
                        "\n\tCommodites: " + infoCommodites +
                        "\n\tPrix total: " + prixTotal + "$");
    }
}
