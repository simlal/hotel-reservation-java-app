package AubServlet;

import AubergeInn.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ChambresCommodites extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        valide login
        HttpSession session = request.getSession();
        Integer etat = (Integer) session.getAttribute("etat");
        if (etat == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
        try {
//            Ajouter une chambre
            if (request.getParameter("ajouterChambre") != null) {
                System.out.printf("Servet ChambresCommodites : POST - Ajouter une chambre");
                // lecture des paramètres du formulaire
                String nomChambre = request.getParameter("nomChambre");
                String typeLitChambre = request.getParameter("typeLitChambre");
                String prixBaseChambreS = request.getParameter("prixBaseChambre");


                if (nomChambre == null || nomChambre.equals(""))
                    throw new IFT287Exception("Vous devez entrer le nom de la chambre!");
                if (typeLitChambre == null || typeLitChambre.equals(""))
                    throw new IFT287Exception("Vous devez entrer le type de Lit!");
                if (prixBaseChambreS == null || prixBaseChambreS.equals(""))
                    throw new IFT287Exception("Vous devez le prix de base!");
                int prixBaseChambre = AubergeInnHelper.ConvertirInt(prixBaseChambreS, "prixBaseChambre");

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerChambre().ajouterChambre(nomChambre, typeLitChambre, prixBaseChambre);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);
            } else if (request.getParameter("supprimerChambre") != null) {
                System.out.printf("Servet ChambresCommodites : POST - Supprimer une chambre");
                String idChambreSupS = request.getParameter("selectionChambreSup");
                int idChambreSup = AubergeInnHelper.ConvertirInt(idChambreSupS, "idChambre a supprimer");

                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubInterro.getTableChambre().checkChambre(idChambreSup)) {
                    throw new IFT287Exception("Chambre avec id=" + idChambreSup + " n'existe pas");
                }

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerChambre().supprimerChambre(idChambreSup);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);
            } else if (request.getParameter("ajouterCommodite") != null) {
                System.out.printf("Servet ChambresCommodites : POST - Ajouter une commodite");
                // lecture des paramètres du formulaire
                String description = request.getParameter("descriptionCommodite");
                String surplusPrixS = request.getParameter("surplusPrixCommodite");


                if (description == null || description.equals(""))
                    throw new IFT287Exception("Vous devez entrer la description de la commodite!");
                if (surplusPrixS == null || surplusPrixS.equals(""))
                    throw new IFT287Exception("Vous devez entrer le prix de surplus!");
                int surplusPrix = AubergeInnHelper.ConvertirInt(surplusPrixS, "surplusPrix");

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerCommodite().ajouterCommodite(description, surplusPrix);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("supprimerCommodite") != null) {
                System.out.printf("Servet ChambresCommodites : POST - Supprimer une Commodite");
                String selectionCommoditeSupS = request.getParameter("selectionCommoditeSup");
                int selectionCommoditeSup = AubergeInnHelper.ConvertirInt(selectionCommoditeSupS, "idCommodite a supprimer");

                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubInterro.getTableCommodite().checkCommodite(selectionCommoditeSup)) {
                    throw new IFT287Exception("Commodite avec id=" + selectionCommoditeSup + " n'existe pas");
                }

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerChambre().supprimerChambre(selectionCommoditeSup);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);
            } else if (request.getParameter("afficherChambre") != null) {
                System.out.printf("Servlet ChambresCommodites : POST - afficher une chambre");

                String idChambreAffichS = request.getParameter("selectionAfficherChambre");
                int idChambreAffich = AubergeInnHelper.ConvertirInt(idChambreAffichS, "idChambre a afficher");

                MainManager aubergeInnInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubergeInnInterro.getTableChambre().checkChambre(idChambreAffich)) {
                    throw new IFT287Exception("Chambre avec id=" + idChambreAffich + " n'existe pas.");
                }
//              Get la chambre et ses commodites
                TupleChambre chambreAffich = aubergeInnInterro.getManagerChambre().getChambre(idChambreAffich);
                List<TupleCommodite> commoditesDeChambre = aubergeInnInterro.getManagerChambreCommodite().getCommoditesDeChambre(chambreAffich);

//              Passer en parametres le client et ses reservations a la requete
                request.setAttribute("chambreInfo", chambreAffich);
                request.setAttribute("commoditesDeChambre", commoditesDeChambre);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/afficherChambre.jsp");
                dispatcher.forward(request, response);
            } else if (request.getParameter("inclureChambreCommodite") != null) {
                System.out.printf("Servlet ChambresCommodites : POST - inclure une commodite a une chambre");
                String idChambreIncS = request.getParameter("selInclureChambre");
                String idCommoditeIncS = request.getParameter("selInclureCommodite");

                int idChambreInc = AubergeInnHelper.ConvertirInt(idChambreIncS, "idChambreInc");
                int idCommoditeInc = AubergeInnHelper.ConvertirInt(idCommoditeIncS, "idCommInc");

                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubInterro.getTableChambre().checkChambre(idChambreInc)) {
                    throw new IFT287Exception("Chambre avec id=" + idChambreInc + " n'existe pas");
                }
                if (!aubInterro.getTableCommodite().checkCommodite(idCommoditeInc)) {
                    throw new IFT287Exception("Commodite avec id=" + idCommoditeInc + " n'existe pas");
                }
                TupleChambre chambreInc = aubInterro.getManagerChambre().getChambre(idChambreInc);
                TupleCommodite commoditeInc = aubInterro.getManagerCommodite().getCommodite(idCommoditeInc);
                TupleChambreCommodite chambreCommodite = new TupleChambreCommodite(idChambreInc, idCommoditeInc);

                if (aubInterro.getTableChambreCommodite().chambreCommoditeExiste(chambreCommodite)) {
                    throw new IFT287Exception(
                            "Impossible inclure la commodite '" + commoditeInc.getDescription() +
                            "' dans la chambre '" + chambreInc.getNom() + "'. " +
                            "La relation existe deja."
                    );
                }

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerChambreCommodite().inclureChambreCommodite(chambreCommodite);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("enleverChambreCommodite") != null) {
                System.out.printf("Servlet ChambresCommodites : POST - enlever une commodite a une chambre");
                String idChambreEnlS = request.getParameter("selEnleverChambre");
                String idCommoditeEnlS = request.getParameter("selEnleverCommodite");

                int idChambreEnl = AubergeInnHelper.ConvertirInt(idChambreEnlS, "idChambreInc");
                int idCommoditeEnl = AubergeInnHelper.ConvertirInt(idCommoditeEnlS, "idCommInc");

                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubInterro.getTableChambre().checkChambre(idChambreEnl)) {
                    throw new IFT287Exception("Chambre avec id=" + idChambreEnl + " n'existe pas");
                }
                if (!aubInterro.getTableCommodite().checkCommodite(idCommoditeEnl)) {
                    throw new IFT287Exception("Commodite avec id=" + idCommoditeEnl + " n'existe pas");
                }
                TupleChambreCommodite chambreCommodite = new TupleChambreCommodite(idChambreEnl, idCommoditeEnl);
                TupleCommodite commoditeEnl = aubInterro.getManagerCommodite().getCommodite(idCommoditeEnl);
                TupleChambre chambreEnl = aubInterro.getManagerChambre().getChambre(idChambreEnl);

                if (!aubInterro.getTableChambreCommodite().chambreCommoditeExiste(chambreCommodite)) {
                    throw new IFT287Exception(
                            "Impossible d'enlever la commodite '" + commoditeEnl.getDescription() +
                                    "' dans la chambre '" + chambreEnl.getNom() + "'. " +
                                    "La relation n'existe pas."
                    );
                }
                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerChambreCommodite().enleverChambreCommodite(chambreCommodite);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);

            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);

            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            Integer etat = (Integer) session.getAttribute("etat");
//            Validation login
            if (etat == null) {
                throw new IFT287Exception("Utilisateur non connecte");
            } else {
                // Dispatch vers page gestion CHambresCommodites
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionChambresCommodites.jsp");
                dispatcher.forward(request, response);
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            e.printStackTrace();
        }
    }
}
