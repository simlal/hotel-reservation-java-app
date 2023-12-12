<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
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
    <h1 class="text-center mb-3">SGA: Resever une chambre</h1>
    <div class="row">
        <div class="col">
            <h2 class="text-center">Chambres Libres</h2>
            <div><b>Periode choisie:</b></div>
            <ul>
                <%
                    Date dateDebutCL = (Date) request.getAttribute("dateDebutCL");
                    Date dateFinCL = (Date) request.getAttribute("dateFinCL");
                %>
                <li>Date de debut: <%=dateDebutCL%></li>
                <li>Date de fin: <%=dateFinCL%></li>
            </ul>
            <%
                MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                List<TupleChambre> chambresLibres = aubInterro
                        .getManagerChambre()
                        .getListChambresLibres(dateDebutCL, dateFinCL);
                if (chambresLibres.size() == 0) { %>
            <div><b>Toutes les chambres sont libres pour cette periode</b></div>
            <% } else { %>
            <table class="table text-center">
                <thead class="thead-dark">
                <tr>
                    <th>ID Chambre</th>
                    <th>Nom</th>
                    <th>Type de Lit</th>
                    <th>Commodites incluses</th>
                    <th>Prix Total ($)</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (TupleChambre chambre : chambresLibres) {
                        List<String> commoditesIncluses = new ArrayList<>();
                        int prixSurplusTotal = 0;
                        List<TupleCommodite> commoditesDeChambre = aubInterro
                                .getManagerChambreCommodite()
                                .getCommoditesDeChambre(chambre);
                        for (TupleCommodite commodite : commoditesDeChambre) {
                            commoditesIncluses.add(commodite.getDescription());
                            prixSurplusTotal += commodite.getSurplusPrix();
                        }
                        int prixTotal = chambre.getPrixBase() + prixSurplusTotal;
                %>
                <tr>
                    <td><%=chambre.getIdChambre()%></td>
                    <td><%=chambre.getNom()%></td>
                    <td><%=chambre.getTypeLit()%></td>
                    <td><%=commoditesIncluses%></td>
                    <td><%=prixTotal%></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>
            <form action="Reservation" method="GET">
                <button type="submit" class="btn btn-primary" name="reserer">Choisir d'autres dates</button>
            </form>
        </div>
        <%--        Formulaire choisir dates chambres libres--%>
        <div class="col">
            <div class="row">
                <h2 class="text-center">Faire une reservation</h2>
            </div>
            <div class="row">
                <div class="card mb-3">
                    <div class="card-header"><b>Choisir les parametres de la reservation</b></div>
                    <div class="card-body">

                        <form action="Reservation" method="POST">
                            <div class="form-group">
                                <label for="dateDebut">Date Debut</label>
                                <input type="date" class="form-control" name="dateDebutRes" value="<%=dateDebutCL%>">
                            </div>
                            <div class="form-group">
                                <label for="dateFin">Date Fin:</label>
                                <input type="date" class="form-control" name="dateFinRes" value="<%=dateFinCL%>">
                            </div>
                            <div class="form-group">
                                <label for="listeClients">Choisir un client</label>
                                <select class="form-control" name="selectionClientRes">
                                    <%
                                        List<TupleClient> clients = aubInterro.getManagerClient().getListClients();
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
                            <div class="form-group">
                                <label for="listeChambres">Choisir une chambre</label>
                                <select class="form-control" name="selectionChambreRes">
                                    <%
                                        List<TupleChambre> chambres = aubInterro.getManagerChambre().getListChambres();
                                        for (TupleChambre chambre : chambres) {
                                    %>
                                    <option value="<%=chambre.getIdChambre()%>">
                                        (<%=chambre.getIdChambre()%>)
                                        <%=chambre.getNom()%> /
                                        <%=chambre.getTypeLit()%> /
                                        <%=chambre.getPrixBase()%>
                                    </option>
                                    <% } %>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary" name="reserver">Reserver</button>
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