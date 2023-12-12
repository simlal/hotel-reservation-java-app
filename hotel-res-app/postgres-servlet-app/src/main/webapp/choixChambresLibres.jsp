<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Afficher les chambres libres</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page pour le choix des dates pour afficher les chambres libres.">

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
    <h1 class="text-center mb-3">SGA: Choix pour affichage chambres libres</h1>
    <div class="row">
        <div class="col">
            <h2 class="text-center">Historique des reservations</h2>
            <%
                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                List<TupleReservation> reservations = aubInterro
                        .getManagerReservation().getListReservations();
                if (reservations.size() == 0) { %>
            <div><b>Aucune reservation dans l'historique</b></div>
            <% } else { %>
            <table class="table text-center">
                <thead class="thead-dark">
                <tr>
                    <th>Date de debut</th>
                    <th>Date de Fin</th>
                    <th>Prenom, Nom du client</th>
                    <th>Nom de la chambre</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (TupleReservation reservation : reservations) {
                        TupleClient client = aubInterro.getManagerClient().getClient(reservation.getIdClient());
                        TupleChambre chambre = aubInterro.getManagerChambre().getChambre(reservation.getIdChambre());
                %>
                <tr>
                    <td><%=reservation.getDateDebut()%></td>
                    <td><%=reservation.getDateFin()%></td>
                    <td><%=client.getPrenom()%>, <%=client.getNom()%></td>
                    <td><%=chambre.getNom()%></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>
        </div>
<%--        Formulaire choisir dates chambres libres--%>
        <div class="col">
            <div class="row">
                <h2 class="text-center">Affichage des chambres libres</h2>
            </div>
            <div class="row">
                <div class="card mb-3">
                    <div class="card-header"><b>Choisir les date pour une eventuelle reservation</b></div>
                    <div class="card-body">

                    <form action="Reservation" method="POST">
                        <div class="form-group">
                            <label for="dateDebut">Date Debut</label>
                            <input type="date" class="form-control" name="dateDebut">
                        </div>
                        <div class="form-group">
                            <label for="dateFin">Date Fin:</label>
                            <input type="date" class="form-control" name="dateFin">
                        </div>
                        <button type="submit" class="btn btn-primary text-center" name="afficherChambresLibres">Afficher</button>
                    </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
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