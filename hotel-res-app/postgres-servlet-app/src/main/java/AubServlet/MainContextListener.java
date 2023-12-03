package AubServlet;

import javax.servlet.*;
import java.util.*;

/**
 * Classe pour gestion des sessions
 * <P>
 * Système de gestion de bibliothèque &copy; 2004 Marc Frappier, Université de
 * Sherbrooke
 */

public class MainContextListener implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent sce)
    {
        System.out.println("Contexte du hotel WEB démarré : " + sce.getServletContext().getServletContextName());
        System.out.println("Voici les paramètres du contexte tels que définis dans web.xml");
        Enumeration<String> initParams = sce.getServletContext().getInitParameterNames();
        while (initParams.hasMoreElements())
        {
            String name = (String) initParams.nextElement();
            System.out.println(name + ":" + sce.getServletContext().getInitParameter(name));
        }
    }

    public void contextDestroyed(ServletContextEvent sce)
    {
        System.out.println("Le contexte de l'application MainManager vient d'être détruit.");
    }
}
