<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Gestion Clients</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page gestion des clients du systeme de gestion de AubergeInn.">

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
    <h1 class="text-center mb-3">SGA: Gestion des clients</h1>

    <div class="container">
        <div class="row">
            <%--                Sommaire clients--%>
            <div class="col">
                <h3 class="text-center">Clients d'AubergeInn</h3>
                <table class="table text-center">
                    <thead class="thead-dark">
                    <tr>
                        <th>idClient</th>
                        <th>Prenom</th>
                        <th>Nom</th>
                        <th>Age</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                        List<TupleClient> clients = aubInterro
                                .getManagerClient()
                                .getListClients();

                        for (TupleClient client : clients) {
                    %>
                    <tr>
                        <td><%=client.getIdClient()%></td>
                        <td><%=client.getPrenom()%></td>
                        <td><%=client.getNom()%></td>
                        <td><%=client.getAge()%></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                <h3 class="text-center">Avec reservation en cours</h3>
                <%
                    List<TupleClient> clientsReservEnCours = aubInterro
                            .getManagerClient()
                            .getListClientsReservEnCours();
                    if (clientsReservEnCours.size() == 0) { %>
                <div><b>Aucun client avec reservation en cours</b></div>
                <% } else { %>
                <table class="table text-center">
                    <thead class="thead-dark">
                    <tr>
                        <th>idClient</th>
                        <th>Prenom</th>
                        <th>Nom</th>
                        <th>Age</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (TupleClient client : clients) { %>
                    <tr>
                        <td><%=client.getIdClient()%></td>
                        <td><%=client.getPrenom()%></td>
                        <td><%=client.getNom()%></td>
                        <td><%=client.getAge()%></td>
                    </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } %>
            </div>
            <div class="col">
                <div class="card mb-3">
                    <div class="card-header"><b>Ajouter un client</b></div>
                    <%--                Ajout client form--%>
                    <div class="card-body">
                        <form action="Clients" method="post">
                            <div class="form-group">
                                <label for="prenomClient">Prenom</label>
                                <input class="form-control" type="text" name="prenomClient" value="">
                            </div>
                            <div class="form-group">
                                <label for="nomClient">Nom</label>
                                <input class="form-control" type="text" name="nomClient" value="">
                            </div>
                            <div class="form-group">
                                <label for="ageClient">Age</label>
                                <input class="form-control" type="text" name="ageClient" value="">
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="ajouterClient" value="Ajouter">
                        </form>
                    </div>
                </div>
                <%--                Supprimer un user--%>
                <div class="card">
                    <div class="card-header"><b>Supprimer un client </b>(sans reservation en cours!)</div>
                    <div class="card-body">
                        <form action="Clients" method="post">
                            <div class="form-group">
                                <label for="listeClients">Liste des clients</label>
                                <select class="form-control" name="selectionClientSup">
                                    <%

                                        for (TupleClient client : clients) {
                                            if (!aubInterro.getTableClient().checkClientReservationEnCours(client.getIdClient())) {
                                    %>
                                    <option value="<%=client.getIdClient()%>">
                                        <%=client.getIdClient()%> /
                                        <%=client.getPrenom()%>
                                        <%=client.getNom()%> /
                                        <%=client.getAge()%> ans
                                    </option>
                                        <% } %>
                                    <% } %>
                                </select>
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="supprimerClient" value="Supprimer">
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="card">
                <div class="card-header"><b>Informations detaillees d'un client </b></div>
                <div class="card-body">
                    <form action="Clients" method="post">
                        <div class="form-group">
                            <label for="listeClient2">Liste des clients</label>
                            <select class="form-control" name="selectionAfficherClient">
                                <%
                                    for (TupleClient client : clients) { %>
                                <option value="<%=client.getIdClient()%>">
                                    <%=client.getIdClient()%> /
                                    <%=client.getPrenom()%>
                                    <%=client.getNom()%> /
                                    <%=client.getAge()%> ans
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <input class="btn btn-primary" type="SUBMIT" name="afficherClient" value="Afficher">
                    </form>
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
