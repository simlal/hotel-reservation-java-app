package AubServlet;

import AubergeInn.IFT287Exception;

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

//        si on est admin on accede a la page de gestion utilisateurs
        RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionUtilisateurs.jsp");
        dispatcher.forward(request, response);
    }

}
