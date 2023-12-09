<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SGA: Gestion utilisateurs</title>
    <meta name="author" content="Simon Lalonde">
    <meta name="description"
          content="Page gestion des utilisateurs du systeme de gestion de AubergeInn.">

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
    <h1 class="text-center mb-3">Gestion des utilisateurs de AubergeInn</h1>

    <div class="container">
        <div class="row">
            <%--                Sommaire clients--%>
            <div class="col">
                <h3 class="text-center">Utilisateurs</h3>
                <%
                    Boolean showPassword = (Boolean) session.getAttribute("showPassword");
                %>
                <form action="Utilisateurs" method="get">
                    <% if (showPassword) { %>
                    <input type="hidden" name="showPassword" value="false" />
                    <input type="submit" value="Cacher mots de passe" />
                    <% } else { %>
                    <input type="hidden" name="showPassword" value="true" />
                    <input type="submit" value="Devoiler mots de passe" />
                    <% } %>
                </form>
                <table class="table text-center">
                    <thead class="thead-dark">
                    <tr>
                        <th>ID utilisateur</th>
                        <th>Mot de Passe</th>
                        <th>Privilege (0=admin, 1=utilisateur)</th>
                        <th>Nom</th>
                        <th>Numero Telephone</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        MainManager aubInterro = AubergeInnHelper.getAubergeInnInterro(session);
                        List<TupleUtilisateur> utilisateurs = aubInterro
                                .getManagerUtilisateurs()
                                .getListeUtilisateurs(true);

                        for (TupleUtilisateur utilisateur : utilisateurs) {
                    %>
                    <tr>
                        <td><%=utilisateur.getUtilisateurName()%></td>
                        <%
                            if (showPassword) { %>
                        <td><%=utilisateur.getMotDePasse()%></td>
                        <% } else { %>
                        <td>********</td>
                        <% } %>
                        <td><%=utilisateur.getNiveauAcces()%></td>
                        <td><%=utilisateur.getNom()%></td>
                        <td><%=utilisateur.getTelephone()%></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <div class="col">
                <div class="card mb-3">
                    <div class="card-header"><b>Ajouter un utilisateur</b></div>
<%--                Ajout user form--%>
                    <div class="card-body">
                        <form action="Utilisateurs" method="post">
                            <div class="form-group">
                                <label for="nomUtilisateurAub">Nom d'utilisateur</label>
                                <input class="form-control" type="text" name="utilisateurName" placeholder=nouvelUtilisateur>
                            </div>
                            <div class="form-group">
                                <label for="motDePasseAub">Mot de passe</label>
                                <input class="form-control" type="password" name="motDePasse" value="">
                            </div>
                            <div class="form-group">
                                <label for="accesAub">Niveau d'acces</label>
                                <select class="form-control" name="niveauAcces">
                                    <option value="0">0</option>
                                    <option value="1">1</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="nomAub">Nom ou prenom</label>
                                <input class="form-control" type="text" name="nomPrenomUtil" value="">
                            </div>
                            <div class="form-group">
                                <label for="telephoneAub">Numero de telephone</label>
                                <input class="form-control" type="tel" name="telephone" pattern="[0-9]{10}" placeholder=8195551234>
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="ajouterUtilisateur" value="Ajouter Utilisateur">
                        </form>
                    </div>
                </div>
<%--                Supprimer un user--%>
                <div class="card">
                    <div class="card-header"><b>Supprimer utilisateur</b></div>
                    <div class="card-body">
                        <form action="Utilisateurs" method="post">
                            <div class="form-group">
                                <label for="listeUtilisateurs">Liste des utilisateurs</label>
                                <select class="form-control" name="selectionUtilisateur">
                                    <%
                                        for (TupleUtilisateur u : utilisateurs) { %>
                                    <option value="<%=u.getUtilisateurName()%>"><%=u.getUtilisateurName()%></option>
                                    <% } %>
                                </select>
                            </div>
                            <input class="btn btn-primary" type="SUBMIT" name="supprimerUtilisateur" value="Supprimer utilisateur">
                        </form>
                    </div>
                </div>
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <br>
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
