
package Web;

import Datos.DaoObligaciones;
import Dominio.Obligaciones;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ServletControladorObligaciones"})
public class ServletControladorObligaciones extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
         String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "listarObligaciones":
                {
                    try {
                        this.listarObligaciones(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorObligaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;

                    
                default:    
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
         String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
            }
        }
    }

    private void listarObligaciones(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Obligaciones> obligaciones = new DaoObligaciones().listarObligaciones();
        
        Gson gson = new Gson();

        String json = gson.toJson(obligaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }
    
}
