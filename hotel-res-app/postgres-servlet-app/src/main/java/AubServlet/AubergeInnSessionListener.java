package AubServlet;

import javax.servlet.http.*;
import AubergeInn.MainManager;
import java.sql.*;
public class AubergeInnSessionListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent se) {
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session détruite pour l'utilisateur " + se.getSession().getAttribute("userID"));

        MainManager AubergeInnInterrogation = (MainManager) se.getSession().getAttribute("AubergeInnInterrogation");
        if (AubergeInnInterrogation != null) {
            try {
                System.out.println("Fermeture de la connexion d'interrogation...");
                AubergeInnInterrogation.fermer();
            } catch (SQLException e) {
                System.out.println("Impossible de fermer la connexion");
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun gestionnaire d'interrogation n'avait encore été créé.");
        }

        MainManager AubergeInnUpdate = (MainManager) se.getSession().getAttribute("AubergeInnUpdate");
        if (AubergeInnUpdate != null) {
            try {
                System.out.println("Fermeture de la connexion de mise à jour...");
                AubergeInnUpdate.fermer();
            } catch (SQLException e) {
                System.out.println("Impossible de fermer la connexion");
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun gestionnaire de mise à jour n'avait encore été créé.");
        }
    }
}