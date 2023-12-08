<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
    <title>IFT287 - Système de gestion de AubergeInn</title>
    <meta name="author" content="Vincent Ducharme">
    <meta name="description"
          content="Page d'accueil du système de gestion de AubergeInn.">

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
          crossorigin="anonymous">

</head>
<body>
<jsp:include page="/navigation.jsp" />
<div class="container">
    <h1 class="text-center mb-3">Gestion de AubergeInn: Vue d'ensemble</h1>
<%--Statistiques generales--%>
    <%
        if (session.getAttribute("admin") != null) {
    %>
    <div class="container mb-5">
        <h3 class="text-center">Info utilisateurs logiciel</h3>
        <div class="d-flex justify-content-center">
        <table class="table" style="width: 50%;">
            <thead>
            <tr>
                <th>Type de Privilege</th>
                <th>Total</th>
            </tr>
            </thead>
            <%
                List<TupleUtilisateur> utilisateurs = AubergeInnHelper
                        .getAubergeInnInterro(session)
                        .getManagerUtilisateurs()
                        .getListeUtilisateurs(true);
                Integer nbAdmins = 0;
                Integer nbReguliers = 0;
                for (TupleUtilisateur utilisateur : utilisateurs) {
                    if (utilisateur.getNiveauAcces() == 0) {
                        nbAdmins += 1;
                    } else {
                        nbReguliers += 1;
                    }
                }
            %>
            <tbody>
                <tr>
                    <td>Administrateur</td>
                    <td><%=nbAdmins%></td>
                </tr>
                <tr>
                    <td>Regulier</td>
                    <td><%=nbReguliers%></td>
                </tr>
            </tbody>
        </table>
        </div>
    </div>
    <% } %>
        <div class="container">
            <div class="row">
<%--                Sommaire clients--%>
                <div class="col-md-4">
                    <h3 class="text-center">Clients</h3>
                    <table class="table text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>Parametre</th>
                                <th>Valeur</th>
                            </tr>
                        </thead>
                        <%
                            MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                            List<TupleClient> clients = aubInterro
                                .getManagerClient()
                                .getListClients();
                            int nbTotalClients = clients.size();
                            double ageMoyenClients = 0;
                            for (TupleClient client : clients) {
                                ageMoyenClients += client.getAge();
                            }
                            ageMoyenClients = Math.round(ageMoyenClients/nbTotalClients);
                        %>
                        <tbody>
                        <tr>
                            <td>Nb total de clients</td>
                            <td><%=nbTotalClients%></td>
                        </tr>
                        <tr>
                            <td>Age moyen</td>
                            <td><%=ageMoyenClients%></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
<%--    Sommaire chambres/commodite--%>
                <%
                    List<TupleChambre> chambres = aubInterro.getManagerChambre().getListChambres();
                    Set<String> typePossibles = new HashSet<String>();
                    double prixBaseMoyen = 0;
                    for (TupleChambre chambre : chambres) {
                        typePossibles.add(chambre.getTypeLit());
                        prixBaseMoyen += chambre.getPrixBase();
                    }
                    prixBaseMoyen = Math.round(prixBaseMoyen/chambres.size());
                %>
                <div class="col-md-4">
                    <h3 class="text-center">Chambres</h3>
                    <table class="table text-center">
                        <thead class="thead-dark">
                        <tr>
                            <th>Parametre</th>
                            <th>Valeur</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Nb de chambres total</td>
                            <td><%chambres.size();%></td>
                        </tr>
                        <tr>
                            <td>Types de lit possible</td>
                            <td><%=typePossibles%></td>
                        </tr>
                        <tr>
                            <td>Prix de base moyen</td>
                            <td><%=prixBaseMoyen%></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
<%--    Sommaire reservations--%>
                <div class="col-md-4">
                    <h3 class="text-center">Reservations</h3>
                    <table class="table text-center">
                        <thead class="thead-dark">
                        <tr>
                            <th>Parametre</th>
                            <th>Valeur</th>
                        </tr>
                        </thead>
                        <%
                            List<TupleReservation> reservations = aubInterro.getManagerReservation().getListReservations();

                            Date maintenant = new Date(System.currentTimeMillis());
                            List<TupleChambre> chambresLibres = aubInterro.getManagerChambre().getListChambresLibres(maintenant, maintenant);
                        %>
                        <tbody>
                        <tr>
                            <td>Nb de reservations (total historique)</td>
                            <td><%=reservations.size()%></td>
                        </tr>
                        <tr>
                            <td>Nb de chambres libres ajourd'hui</td>
                            <td><%=chambresLibres.size()%></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    <br>
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/messageErreur.jsp" />
    <br>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>
</body>
</html>
