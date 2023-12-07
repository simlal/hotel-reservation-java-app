package AubServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Index extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Index : GET");
        // Si on a déjà entré les informations de connexion valide
        if (AubergeInnHelper.infoBDValide(getServletContext()))
        {
            AubergeInnHelper.DispatchToLogin(request, response);
        }
        else
        {
            AubergeInnHelper.DispatchToBDConnect(request, response);
        }
    }
}
