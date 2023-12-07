package AubServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet qui gère l'inscription d'un utilisateur au système de gestion de
 * AubergeInn
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 11 novembre 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 * </pre>
 */

public class Inscription extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Inscription : POST");
        if (!AubergeInnHelper.peutProcederLogin(getServletContext(), request, response))
        {
            // Le dispatch vers le login se fait dans AubergeInnHelper.peutProceder
            return;
        }

        System.out.println("Servlet Inscription : POST dispatch vers creerCompte.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/creerCompte.jsp");
        dispatcher.forward(request, response);
    }

    // Dans les formulaires, on utilise la méthode POST
    // donc, si le servlet est appelé avec la méthode GET
    // s'est qu'on a écrit l'adresse directement dans la barre d'adresse.
    // On procède si on est connecté correctement, sinon, on retourne au login
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Inscription : GET");
        // Si on a déjà entré les informations de connexion valide

        if (AubergeInnHelper.peutProceder(getServletContext(), request, response))
        {
            System.out.println("Servlet Inscription : GET dispatch vers creerCompte.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/creerCompte.jsp");
            dispatcher.forward(request, response);
        }
    }

} // class