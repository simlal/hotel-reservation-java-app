package AubServlet;

import AubergeInn.IFT287Exception;
import AubergeInn.MainManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utilisateurs extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        valide login
        HttpSession session = request.getSession();
        Integer etat = (Integer) session.getAttribute("etat");
        if (etat == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
//        requiere privilege admin
        try {
            Boolean statut_admin = (Boolean) session.getAttribute("admin");
            if (statut_admin == null || !statut_admin) {
                throw new IFT287Exception("Vous n'avez pas les privileges administrateur");
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/accueil.jsp");
            dispatcher.forward(request, response);
            e.printStackTrace();
        }

        try {
//            Ajouter un utilisateur dans bd
            if (request.getParameter("ajouterUtilisateur") != null) {
                System.out.printf("Servet Utilisateurs : POST - Ajouter un Utilisateur");
                // lecture des paramètres du formulaire de creerCompte.jsp
                String utilisateurName = request.getParameter("utilisateurName");
                String motDePasse = request.getParameter("motDePasse");
                int niveauAcces = AubergeInnHelper.ConvertirInt(request.getParameter("niveauAcces"), "niveau d'acces");
                String telephoneS = request.getParameter("telephone");
                String nomPrenomUtil = request.getParameter("nomPrenomUtil");


                if (utilisateurName == null || utilisateurName.equals(""))
                    throw new IFT287Exception("Vous devez entrer un nom d'utilisateur!");
                if (motDePasse == null || motDePasse.equals(""))
                    throw new IFT287Exception("Vous devez entrer un mot de passe!");
                if (telephoneS == null || telephoneS.equals(""))
                    throw new IFT287Exception("Vous devez entrer un numéro de téléphone!");
                if (nomPrenomUtil == null || nomPrenomUtil.equals(""))
                    throw new IFT287Exception("Vous devez entrer un nom!");

                long telephone = AubergeInnHelper.ConvertirLong(telephoneS, "Le numéro de téléphone");

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerUtilisateurs().inscrire(utilisateurName, motDePasse, niveauAcces, nomPrenomUtil, telephone);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("supprimerUtilisateur") != null) {
                System.out.printf("Servet Utilisateurs : POST - Supprimer un Utilisateur");
                String utilisateurNameSelection = request.getParameter("selectionUtilisateur");

//                empecher supprimer Admin maitre
                System.out.println(utilisateurNameSelection);
                MainManager aubergeInnInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (utilisateurNameSelection.equals("AubergeInnAdmin")) {
                    throw new IFT287Exception("Impossible de supprimer l'admin principal");
                }

                if (!aubergeInnInterro.getManagerUtilisateurs().existe(utilisateurNameSelection)) {
                    throw new IFT287Exception("Utilisateur avec id=" + utilisateurNameSelection + " n'existe pas");
                }
//              empecher supprimer user connecte
                if (session.getAttribute("userID").equals(utilisateurNameSelection)) {
                    throw new IFT287Exception("Impossible de supprimer l'utilisateur qui est connecte.");
                }

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerUtilisateurs().desinscrire(utilisateurNameSelection);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
                dispatcher.forward(request, response);
//          Si on fait un post request (avec postman par exemple) sans action disponible sur le .jsp
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
                dispatcher.forward(request, response);
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
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
        System.out.println("Servlet Utilisateurs : GET");
        HttpSession session = request.getSession();
        //        Acces sans etre login
        try {
            Integer etat = (Integer)session.getAttribute("etat");
            if (etat == null) {
                throw new IFT287Exception("Utilisateur non connecte");
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            e.printStackTrace();
        }

//        Pas acces si n'est pas un admin (pas dans la navbar mais au cas ou url hardcode)
        try {
            Boolean statut_admin = (Boolean) session.getAttribute("admin");
            if (statut_admin == null || !statut_admin) {
                throw new IFT287Exception("Vous n'avez pas les privileges administrateur");
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/accueil.jsp");
            dispatcher.forward(request, response);
            e.printStackTrace();
        }

//      Admin OK va pouvoir proceder vers pages gestions
//        Creer le flag lorsque click par le bouton
        String showPasswordParam = request.getParameter("showPassword");
        if (showPasswordParam != null) {
            Boolean showPassword = Boolean.valueOf(showPasswordParam);
            session.setAttribute("showPassword", showPassword);
        }

        Boolean showPassword = (Boolean) session.getAttribute("showPassword");

//        Creer le flag password si premiere fois qu'on appel le GET
        if (showPassword == null) {
            showPassword = false;
            session.setAttribute("showPassword", showPassword);
        }

//    procede au dispatch vers gestion
        RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
        dispatcher.forward(request, response);
    }

}
