
package Dominio;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class seguridad extends HttpServlet{
    
    public static  String usuer = null;
    
    public void validacionSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = req.getSession(true);
        
        usuer = (String) session.getAttribute("usuario");
        

    }
    
}
