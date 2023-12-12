<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Gestion ChambresCommodites</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page gestion des chambres et commodites du systeme de gestion de AubergeInn.">

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
    <h1 class="text-center mb-3">SGA: Gestion des Chambres et Commodites</h1>
<%--Chambres--%>
    <div class="container mb-3">
        <div class="row text-center">
            <div class="col text-center">
                <h2 class="text-center">Gestion des chambres</h2>
            </div>
        </div>
        <div class="row">
            <%--                Sommaire clients--%>
            <div class="col">
                <h3 class="text-center">Toutes les chambres</h3>
                <table class="table text-center">
                    <thead class="thead-dark">
                    <tr>
                        <th>ID chambre</th>
                        <th>Nom</th>
                        <th>Type de lit</th>
                        <th>Prix de base</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                        List<TupleChambre> chambres = aubInterro
                                .getManagerChambre()
                                .getListChambres();

                        for (TupleChambre chambre : chambres) {
                    %>
                    <tr>
                        <td><%=chambre.getIdChambre()%></td>
                        <td><%=chambre.getNom()%></td>
                        <td><%=chambre.getTypeLit()%></td>
                        <td><%=chambre.getPrixBase()%></td>

                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <div class="col">
                <div class="card mb-3">
                    <div class="card-header"><b>Ajouter une chambre</b></div>
                    <%--                Ajout client form--%>
                    <div class="card-body">
                        <form action="ChambresCommodites" method="post">
                            <div class="form-group">
                                <label for="prenomClient">Nom</label>
                                <input class="form-control" type="text" name="nomChambre" value="">
                            </div>
                            <div class="form-group">
                                <label for="typeLitChambre">Type de Lit</label>
                                <input class="form-control" type="text" name="typeLitChambre" value="">
                            </div>
                            <div class="form-group">
                                <label for="prixBaseChambre">Prix de Base</label>
                                <input class="form-control" type="number" name="prixBaseChambre" value="" pattern="[0-9]">
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="ajouterChambre" value="Ajouter">
                        </form>
                    </div>
                </div>
                <%--                Supprimer un user--%>
            </div>
                <div class="col">
                <div class="card">
                    <div class="card-header"><b>Supprimer une chambre </b>(sans reservation(s) associee(s))</div>
                    <div class="card-body">
                        <form action="ChambresCommodites" method="post">
                            <div class="form-group">
                                <label for="listeChambres">Liste des chambres sans reservations</label>
                                <select class="form-control" name="selectionChambreSup">
                                    <%
                                        List<TupleChambre> chambresOkDelete = new ArrayList<>();

                                        for (TupleChambre chambre : chambres) {
                                            boolean chambreSansResEnCours = !aubInterro.getTableChambre().checkChambreResEnCours(chambre.getIdChambre());
                                            boolean chambreSansResFuture = !aubInterro.getTableChambre().checkChambreResFuture(chambre.getIdChambre());
                                            if (chambreSansResEnCours && chambreSansResFuture) {
                                                chambresOkDelete.add(chambre);
                                            }
                                        }
                                        for (TupleChambre chambre : chambresOkDelete) {
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
                            <input class="btn btn-primary" type="SUBMIT" name="supprimerChambre" value="Supprimer">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%--    Commodites--%>
    <div class="container mb-3">
        <div class="row">
            <div class="col">
                <h2 class="text-center">Gestion des commodites</h2>
            </div>
        </div>
        <div class="row">
            <%--                Sommaire clients--%>
            <div class="col">
                <h3 class="text-center">Toutes les commodites</h3>
                <table class="table text-center">
                    <thead class="thead-dark">
                    <tr>
                        <th>ID Commodite</th>
                        <th>Description</th>
                        <th>Prix de base</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<TupleCommodite> commodites = aubInterro
                                .getManagerCommodite()
                                .getListCommodites();

                        for (TupleCommodite commodite : commodites) {
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
            <div class="col">
                <div class="card mb-3">
                    <div class="card-header"><b>Ajouter une commodite</b></div>
                    <%--                Ajout client form--%>
                    <div class="card-body">
                        <form action="ChambresCommodites" method="post">
                            <div class="form-group">
                                <label for="descriptionCommodite">Description</label>
                                <input class="form-control" type="text" name="descriptionCommodite" value="">
                            </div>
                            <div class="form-group">
                                <label for="surplusPrixCommodite">Surplus de prix</label>
                                <input class="form-control" type="number" name="surplusPrixCommodite" value="" pattern="[0-9]">
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="ajouterCommodite" value="Ajouter">
                        </form>
                    </div>
                </div>
                <%--                Supprimer un user--%>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-header"><b>Supprimer une commodite </b>(si pas associee a chambre)</div>
                    <div class="card-body">
                        <form action="Clients" method="post">
                            <div class="form-group">
                                <label for="listeCommodites">Liste des commodites non associees</label>
                                <select class="form-control" name="selectionCommoditeSup">
                                    <%
                                        List<TupleCommodite> commoditesOkDelete = new ArrayList<>();

                                        for (TupleCommodite commodite : commodites) {
                                            List<TupleChambre> chambresDeCommodite = aubInterro
                                                    .getManagerChambreCommodite()
                                                    .getChambresDeCommodite(commodite);
                                            if (chambresDeCommodite.size() == 0) {
                                                commoditesOkDelete.add(commodite);
                                            }
                                        }
                                        for (TupleCommodite commodite : commoditesOkDelete) {
                                    %>
                                    <option value="<%=commodite.getIdCommodite()%>">
                                        (<%=commodite.getIdCommodite()%>)
                                        <%=commodite.getDescription()%> /
                                        <%=commodite.getSurplusPrix()%>
                                    </option>
                                    <% } %>
                                </select>
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="supprimerCommodite" value="Supprimer">
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="container mb-3">
        <div class="col">
            <div class="card text-center">
                <div class="card-header"><b>Informations detaillees d'une chambre</b></div>
                <div class="card-body">
                    <form action="ChambresCommodites" method="post">
                        <div class="form-group">
                            <label for="listeChambres2">Choisir la chambre d'interet</label>
                            <select class="form-control" name="selectionAfficherChambre">
                                <%
                                    for (TupleChambre chambre : chambres) { %>
                                <option value="<%=chambre.getIdChambre()%>">
                                    (<%=chambre.getIdChambre()%>)
                                    <%=chambre.getNom()%> /
                                    <%=chambre.getTypeLit()%> /
                                    <%=chambre.getPrixBase()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <input class="btn btn-primary" type="SUBMIT" name="afficherChambre" value="Afficher">
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <h3></h3>
        <div class="col">
            <div class="card text-center">
                <div class="card-header"><b>Inclure une commodite a une chambre</b></div>
                <div class="card-body">
                    <form action="ChambresCommodites" method="post">
                        <div class="form-group">
                            <label for="listeChambres2">Choisir une chambre</label>
                            <select class="form-control" name="selInclureChambre">
                                <%
                                    for (TupleChambre chambre : chambres) { %>
                                <option value="<%=chambre.getIdChambre()%>">
                                    (<%=chambre.getIdChambre()%>)
                                    <%=chambre.getNom()%> /
                                    <%=chambre.getTypeLit()%> /
                                    <%=chambre.getPrixBase()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="listeCommodite2">Choisir une commodite</label>
                            <select class="form-control" name="selInclureCommodite">
                                <%
                                    for (TupleCommodite commodite : commodites) { %>
                                <option value="<%=commodite.getIdCommodite()%>">
                                    (<%=commodite.getIdCommodite()%>)
                                    <%=commodite.getDescription()%> /
                                    <%=commodite.getSurplusPrix()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <input class="btn btn-primary" type="SUBMIT" name="inclureChambreCommodite" value="Inclure">
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <h3></h3>
        <div class="col">
            <div class="card text-center">
                <div class="card-header"><b>Enlever une commodite a une chambre</b></div>
                <div class="card-body">
                    <form action="ChambresCommodites" method="post">
                        <div class="form-group">
                            <label for="listeChambres2">Choisir une chambre</label>
                            <select class="form-control" name="selEnleverChambre">
                                <%
                                    for (TupleChambre chambre : chambres) { %>
                                <option value="<%=chambre.getIdChambre()%>">
                                    (<%=chambre.getIdChambre()%>)
                                    <%=chambre.getNom()%> /
                                    <%=chambre.getTypeLit()%> /
                                    <%=chambre.getPrixBase()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="listeCommodite2">Choisir une commodite</label>
                            <select class="form-control" name="selEnleverCommodite">
                                <%
                                    for (TupleCommodite commodite : commodites) { %>
                                <option value="<%=commodite.getIdCommodite()%>">
                                    (<%=commodite.getIdCommodite()%>)
                                    <%=commodite.getDescription()%> /
                                    <%=commodite.getSurplusPrix()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <input class="btn btn-primary" type="SUBMIT" name="enleverChambreCommodite" value="Enlever">
                    </form>
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
