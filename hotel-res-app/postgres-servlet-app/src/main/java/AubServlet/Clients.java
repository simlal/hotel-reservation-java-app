package AubServlet;

import AubergeInn.IFT287Exception;
import AubergeInn.MainManager;
import AubergeInn.TupleClient;
import AubergeInn.TupleReservation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Clients extends HttpServlet {

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
//            Ajouter un client
            if (request.getParameter("ajouterClient") != null) {
                System.out.printf("Servet Clients : POST - Ajouter un client");
                // lecture des paramètres du formulaire
                String prenomClient = request.getParameter("prenomClient");
                String nomClient = request.getParameter("nomClient");
                String ageClientS = request.getParameter("ageClient");


                if (prenomClient == null || prenomClient.equals(""))
                    throw new IFT287Exception("Vous devez entrer le prenom du client!");
                if (nomClient == null || nomClient.equals(""))
                    throw new IFT287Exception("Vous devez entrer le nom du client!");
                if (ageClientS == null || ageClientS.equals(""))
                    throw new IFT287Exception("Vous devez l'age du client!");
                int ageClient = AubergeInnHelper.ConvertirInt(ageClientS, "age");


                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerClient().ajouterClient(prenomClient, nomClient, ageClient);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionClients.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("supprimerClient") != null) {
                System.out.printf("Servet Clients : POST - Supprimer un client");
                String idClientSupS = request.getParameter("selectionClientSup");
                int idClientSup = AubergeInnHelper.ConvertirInt(idClientSupS, "idClient a supprimer");

                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);

                if (!aubInterro.getTableClient().checkClient(idClientSup)) {
                    throw new SQLException(
                            "Impossible supprimer client avec idClient=" + idClientSup + ": n'existe pas dans db."
                    );
                }

                if (aubInterro.getTableClient().checkClientReservationEnCours(idClientSup)) {
                    throw new IFT287Exception(
                            "Impossible supprimer client avec idClient=" + idClientSup + ": a des reservations en cours."
                    );
                }

                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerClient().supprimerClient(idClientSup);
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionClients.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("afficherClient") != null) {
                System.out.printf("Servlet Clients : POST - afficher un client");

                String idClientAffichS = request.getParameter("selectionAfficherClient");
                int idClientAffichage = AubergeInnHelper.ConvertirInt(idClientAffichS, "idClient a afficher");

                MainManager aubergeInnInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubergeInnInterro.getTableClient().checkClient(idClientAffichage)) {
                    throw new IFT287Exception("Client avec id=" + idClientAffichage + " n'existe pas.");
                }
//              Get le client et ses reservations

                TupleClient clientInfo = aubergeInnInterro.getManagerClient().getClient(idClientAffichage);
                List<TupleReservation> reservationsDeClient = aubergeInnInterro
                        .getManagerReservation().
                        getReservationsDeClient(clientInfo.getIdClient());

//              Passer en parametres le client et ses reservations a la requete
                request.setAttribute("clientInfo", clientInfo);
                request.setAttribute("reservationsDeClient", reservationsDeClient);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/afficherClient.jsp");
                dispatcher.forward(request, response);

//          Si on fait un post request (avec postman par exemple) sans action disponible sur le .jsp
//                Ou on click sur le boutton de retours
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionClients.jsp");
                dispatcher.forward(request, response);
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionClients.jsp");
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
                // Dispatch vers page gestion client
                RequestDispatcher dispatcher = request.getRequestDispatcher("/gestionClients.jsp");
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
