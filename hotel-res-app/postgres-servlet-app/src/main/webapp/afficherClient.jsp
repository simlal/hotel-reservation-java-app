<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Affichage d'un client</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page d'affichage des details d'un client">

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
    <%
        TupleClient client = (TupleClient) request.getAttribute("clientInfo");
        List<TupleReservation> reservationsDeClient = (List<TupleReservation>) request.getAttribute("reservationsDeClient");
    %>
    <h1 class="text-center mb-1">Informations et reservation(s) de <%=client.getPrenom()%> <%=client.getNom()%></h1>
    <div class="row mb-3">
    <h3 class="text-center"></h3>
    <table class="table text-center" style="width: 50%; margin: auto;">
        <thead class="thead-dark">
        <tr>
            <th>idClient</th>
            <th>Prenom</th>
            <th>Nom</th>
            <th>Age</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><%=client.getIdClient()%></td>
            <td><%=client.getPrenom()%></td>
            <td><%=client.getNom()%></td>
            <td><%=client.getAge()%></td>
        </tr>
        </tbody>
    </table>
    </div>
    <%
        if (reservationsDeClient.size() == 0) { %>
    <div class="text-center">
        <h3 class="text-center">Historique de reservations</h3>
        <b>Aucune reservations effectuees par le client!</b>
    </div>
    <% } else { %>
    <div>
        <h3 class="text-center">Historique de reservations</h3>
        <table class="table text-center" style="width: 50%; margin: auto;">
            <thead class="thead-dark">
            <tr>
                <th>Date de Debut</th>
                <th>Date de Fin</th>
                <th>Nom de la Chambre</th>
                <th>Nb de commodites</th>
                <th>Prix total de la reservation</th>
            </tr>
            </thead>
            <tbody>
        <%
            MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
            for (TupleReservation reservation : reservationsDeClient) {
                TupleChambre chambre = aubInterro.getManagerChambre().getChambre(reservation.getIdChambre());
                int prixTotal = chambre.getPrixBase();
                List<TupleCommodite> commodites = aubInterro.getManagerChambreCommodite().getCommoditesDeChambre(chambre);
                for (TupleCommodite commodite : commodites) {
                    prixTotal += commodite.getSurplusPrix();
                } %>
            <tr>
                <td><%=reservation.getDateDebut()%></td>
                <td><%=reservation.getDateFin()%></td>
                <td><%=chambre.getNom()%></td>
                <td><%=commodites.size()%></td>
                <td><%=prixTotal%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
</div>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<br>
<jsp:include page="/messageErreur.jsp" />
<br>

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
