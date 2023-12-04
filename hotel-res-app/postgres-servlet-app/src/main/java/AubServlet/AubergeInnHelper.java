package AubServlet;
import AubergeInn.*;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AubergeInnHelper {
    public static boolean infoBDValide(ServletContext c)
    {
        return c.getAttribute("serveur") != null;
    }

    public static boolean peutProceder(ServletContext c, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (infoBDValide(c))
        {
            HttpSession session = request.getSession(false);
            if (AubergeInnHelper.estConnecte(session))
            {
                return true;
            }
            DispatchToLogin(request, response);
            return false;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }

    public static boolean peutProcederLogin(ServletContext c, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(infoBDValide(c))
        {
            HttpSession session = request.getSession(false);
            if (session != null)
            {
                session.invalidate();
            }
            return true;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }

    public static void DispatchToLoginServlet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AubergeInnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Login");
        dispatcher.forward(request, response);
    }

    public static void DispatchToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AubergeInnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }

    public static void DispatchToBDConnect(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AubergeInnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    public static boolean estConnecte(HttpSession session)
    {
        if(session == null)
            return false;
        return session.getAttribute("etat") != null;
    }

    public static boolean gestionnairesCrees(HttpSession session)
    {
        if(session == null)
            return false;
        return session.getAttribute("AubergeInnInterrogation") != null;
    }

    public static void creerGestionnaire(ServletContext c, HttpSession s) throws SQLException, IFT287Exception, Exception
    {
        String serveur = (String) c.getAttribute("serveur");
        String bd = (String) c.getAttribute("bd");
        String userIdBD = (String) c.getAttribute("user");
        String pass = (String) c.getAttribute("pass");

        MainManager AubergeInnInterrogation = new MainManager(serveur, bd, userIdBD, pass);
        AubergeInnInterrogation.getConnexion().setIsolationReadCommited();
        s.setAttribute("AubergeInnInterrogation", AubergeInnInterrogation);
        MainManager AubergeInnUpdate = new MainManager(serveur, bd, userIdBD, pass);
        s.setAttribute("AubergeInnUpdate", AubergeInnUpdate);
    }

    public static MainManager getAubergeInnInterro(HttpSession session)
    {
        return (MainManager) session.getAttribute("AubergeInnInterrogation");
    }

    public static MainManager getAubergeInnUpdate(HttpSession session)
    {
        return (MainManager) session.getAttribute("AubergeInnUpdate");
    }


    public static int ConvertirInt(String v, String nom) throws IFT287Exception
    {
        try
        {
            return Integer.parseInt(v);
        }
        catch(Exception e)
        {
            throw new IFT287Exception(nom + " ne doit être composé que de chiffre.");
        }
    }

    public static long ConvertirLong(String v, String nom) throws IFT287Exception
    {
        try
        {
            return Long.parseLong(v);
        }
        catch(Exception e)
        {
            throw new IFT287Exception(nom + " ne doit être composé que de chiffre.");
        }
    }
}
