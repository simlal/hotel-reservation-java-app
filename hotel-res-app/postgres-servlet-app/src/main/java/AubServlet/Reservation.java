package AubServlet;

import AubergeInn.IFT287Exception;
import AubergeInn.MainManager;
import AubergeInn.TupleReservation;

import java.sql.Date;
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

public class Reservation extends HttpServlet {
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
//            Afficher les chambres libres
            if (request.getParameter("afficherChambresLibres") != null) {
                String dateDebutS = request.getParameter("dateDebut");
                String dateFinS = request.getParameter("dateFin");
                if(dateDebutS == null || dateDebutS.equals(""))
                    throw new IFT287Exception("Vous devez entrer une date de debut.");
                if(dateFinS == null || dateFinS.equals(""))
                    throw new IFT287Exception("Vous devez entrer une date de fin");

                Date dateDebut = AubergeInnHelper.convertirDate(dateDebutS, "date de debut");
                Date dateFin = AubergeInnHelper.convertirDate(dateFinS, "date de fin");

                Date maintenant = new Date(System.currentTimeMillis());
                if (dateDebut.compareTo(maintenant) < 0) {
                    throw new IFT287Exception("la Date de debut doit etre plus tard qu'aujourd'hui");
                }

                if (dateFin.compareTo(dateDebut) <= 0) {
                    throw new IFT287Exception("la Date de debut doit etre plus grande que celle de la fin");
                }

//                Set les dates et dispatch vers la page pour faire la reservation
                request.setAttribute("dateDebutCL", dateDebut);
                request.setAttribute("dateFinCL", dateFin);
                System.out.println(dateDebut);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/faireReservation.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("reserver") != null) {
                String dateDebutResS = request.getParameter("dateDebutRes");
                String dateFinResS = request.getParameter("dateFinRes");
                String idClientS = request.getParameter("selectionClientRes");
                String idChambreS = request.getParameter("selectionChambreRes");

                if(dateDebutResS == null || dateDebutResS.equals(""))
                    throw new IFT287Exception("Vous devez entrer une date de debut.");
                if(dateFinResS == null || dateFinResS.equals(""))
                    throw new IFT287Exception("Vous devez entrer une date de fin");
                if(idClientS == null || idClientS.equals(""))
                    throw new IFT287Exception("Vous devez choisir un client");
                if(idChambreS == null || idChambreS.equals(""))
                    throw new IFT287Exception("Vous devez choisir une chambre");

                Date dateDebutRes = AubergeInnHelper.convertirDate(dateDebutResS, "dateDebut reserv");
                Date dateFinRes = AubergeInnHelper.convertirDate(dateFinResS, "dateFin reserv");
                int idClient = AubergeInnHelper.ConvertirInt(idClientS, "id du client");
                int idChambre = AubergeInnHelper.ConvertirInt(idChambreS, "id de la chambre");

//                validation des dates
                Date maintenant = new Date(System.currentTimeMillis());
                if (dateDebutRes.compareTo(maintenant) < 0) {
                    throw new IFT287Exception("la Date de debut doit etre plus tard qu'aujourd'hui");
                }
                if (dateFinRes.compareTo(dateDebutRes) <= 0) {
                    throw new IFT287Exception("la Date de debut doit etre plus grande que celle de la fin");
                }
//                validation existe client et chambre
                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                if (!aubInterro.getTableClient().checkClient(idClient)) {
                    throw new SQLException(
                            "Impossible supprimer client avec idClient=" + idClient + ": n'existe pas dans db."
                    );
                }
                if (!aubInterro.getTableChambre().checkChambre(idChambre)) {
                    throw new IFT287Exception("Chambre avec id=" + idChambre + " n'existe pas");
                }

//                Check si la chambre peu etre reserve
                if (aubInterro.getTableReservation().checkChambreReserve(idChambre, dateDebutRes, dateFinRes)) {
                    throw new IFT287Exception(
                            "Impossible faire reservation avec idChambre=" + idChambre + ": chambre est deja reservee."
                    );
                }

                TupleReservation reservation =  new TupleReservation(dateDebutRes, dateFinRes, idClient, idChambre);
                MainManager aubergeInnUpdate = AubergeInnHelper.getAubergeInnUpdate(session);
                synchronized (aubergeInnUpdate) {
                    aubergeInnUpdate.getManagerReservation().faireReservation(reservation);
                }

//                dispatch vers chambresLibres pour voir reservation
                RequestDispatcher dispatcher = request.getRequestDispatcher("/choixChambresLibres.jsp");
                dispatcher.forward(request, response);

            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/choixChambresLibres.jsp");
                dispatcher.forward(request, response);
            }
        } catch (IFT287Exception e) {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/choixChambresLibres.jsp");
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
                // Dispatch vers page choix de
                RequestDispatcher dispatcher = request.getRequestDispatcher("/choixChambresLibres.jsp");
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
