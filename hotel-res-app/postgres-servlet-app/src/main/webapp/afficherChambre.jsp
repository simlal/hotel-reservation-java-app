<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Affichage d'une chambre</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page d'affichage des details d'une chambre">

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
        TupleChambre chambre = (TupleChambre) request.getAttribute("chambreInfo");
        List<TupleCommodite> commoditesDeChambre = (List<TupleCommodite>) request.getAttribute("commoditesDeChambre");
    %>
    <h1 class="text-center mb-1">Informations et reservation(s) de <%=chambre.getNom()%></h1>
    <div class="row mb-3">
        <h3 class="text-center"></h3>
        <table class="table text-center" style="width: 50%; margin: auto;">
            <thead class="thead-dark">
            <tr>
                <th>idChambre</th>
                <th>Nom</th>
                <th>Type de Lit</th>
                <th>Prix de Base</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><%=chambre.getIdChambre()%></td>
                <td><%=chambre.getNom()%></td>
                <td><%=chambre.getTypeLit()%></td>
                <td><%=chambre.getPrixBase()%></td>
            </tr>
            </tbody>
        </table>
    </div>
    <%
        if (commoditesDeChambre.size() == 0) { %>
    <div class="text-center">
        <h3 class="text-center">Historique de reservations</h3>
        <b>Cette chambre ne contient aucune commodite</b>
    </div>
    <% } else { %>
    <div>
        <h3 class="text-center">Commodite(s) incluse(s)</h3>
        <table class="table text-center" style="width: 50%; margin: auto;">
            <thead class="thead-dark">
            <tr>
                <th>ID commodite</th>
                <th>Description</th>
                <th>Surplus de Prix</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (TupleCommodite commodite : commoditesDeChambre) {
                    %>
            <tr>
                <td><%=commodite.getIdCommodite()%></td>
                <td><%=commodite.getDescription()%></td>
                <td><%=commodite.getSurplusPrix()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
    <form class="text-center" action="ChambresCommodites" method="GET">
        <input class="btn btn-primary" type="SUBMIT" name="retourChambresCommodites" value="Retour vers page ChambresCommodites">
    </form>
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
