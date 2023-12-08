<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <%
                if (session.getAttribute("admin") != null)    // Lien vers operations utilisateurs
                {
            %>
            <li><a class="nav-item nav-link" href="Utilisateurs">Gestion Utilisateurs</a></li>

            <%
                }
            %>
            <li><a class="nav-item nav-link" href="#">Gestion Clients</a></li>
            <li><a class="nav-item nav-link" href="#">Gestion Chambres + Commodites</a></li>
            <li><a class="nav-item nav-link" href="#">Faire une reservation</a></li>
        </ul>
    </div>
    <%String statut = null;
     if (session.getAttribute("admin") != null) {
        statut = "Admin";
     } else {
        statut = "Utilisateur";
     }
     String utilisateurName = session.getAttribute("userID").toString();
     %>
    <div class="navbar-collapse collapse justify-content-end">
        <ul class="nav navbar-nav navbar-right">
            <li><div class="nav-item nav-link">Privileges: <%=statut%></div></li>
            <li><a class="nav-item nav-link" href="Logout">Deconnexion (<%=utilisateurName%>)</a></li>
        </ul>
    </div>
</nav>