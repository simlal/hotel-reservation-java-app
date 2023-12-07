<%@ page import="AubergeInn.*,AubServlet.*,javax.sql.*"
         contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
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
    <h1 class="text-center">Système de gestion de AubergeInn</h1>
<%--Statistiques generales--%>
    <h2 class="text-center"> Vue d'ensemble</h2>
    <div class="container">
        <h3>Info utilisateurs logiciel</h3>
        <table class="table-dark">
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
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h3>Clients</h3>
                    <table class="table">
                        <thead class="thead-dark">
                            <tr>
                                <th>Parametre</th>
                                <th>valeur</th>
                            </tr>
                        </thead>
<%--                        <% List<TupleClient> clients = AubergeInnHelper--%>
<%--                                .getAubergeInnInterro(session)--%>
<%--                                .getManagerClient();--%>

<%--                        %>--%>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-4">
                    <table class="table">
<%--                        TODO TABLE CHAMBRECOMM--%>
                    </table>
                </div>
                <div class="col-md-4">
                    <table class="table">
                        <%--                        TODO TABLE RESERV--%>
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
