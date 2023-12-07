package AubServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Classe pour logout système de gestion de bibliothèque
 * <P>
 * Système de gestion de AubergeInn &copy; 2004 Marc Frappier, Université de
 * Sherbrooke
 */

public class Logout extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // invalider la session pour libérer les ressources associées à la
        // session
        request.getSession().invalidate();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/tp5/Login");
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}