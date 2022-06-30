
package Web;

import Datos.DaoConsignaciones;
import Datos.DaoObservacion;
import Datos.DaoUsuarios;
import Dominio.Observaciones;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = {"/ServletObservaciones"})
public class ServletObservaciones extends HttpServlet{
    
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "obtenerObservaciones":
                {
                    try {
                        this.obtenerObservaciones(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletObservaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;

                    
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "nuevaObservacion":
                {
                    try {
                        this.nuevaObservacion(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletObservaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;
                case "guardarObservacion":
                {
                    try {
                        this.guardarObservacion(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletObservaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;


            }
        }
    }

    private void obtenerObservaciones(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));
        List<Observaciones> observaciones = new DaoObservacion().obtenerObservaciones(id_consignacion);
        
        Gson gson = new Gson();

        String json = gson.toJson(observaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void nuevaObservacion(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession sesion = req.getSession(true);
        String email = (String)sesion.getAttribute("usuario");
        String observacion = req.getParameter("observacion");
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        Observaciones obs = new Observaciones(observacion, id_usuario, id_consignacion);
        
        int guardarObservacion = new DaoObservacion().guardarObservacion(obs);
        
        int actualizarObservacionConsignacion = new DaoConsignaciones().actualizarObservacionConsignacion(guardarObservacion, id_consignacion);
        
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(actualizarObservacionConsignacion);
        out.flush();
        
        
    }

    private void guardarObservacion(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        String observacion = req.getParameter("observacion");
        HttpSession sesion = req.getSession(true);
        String email = (String) sesion.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        int id_consignacion = new DaoConsignaciones().obtenerIdConsignacion();
        Observaciones obs = new Observaciones(observacion, id_usuario, id_consignacion);
        
        int id_observacion = new DaoObservacion().guardarObservacion(obs);
        
        int actualizarObservacionEnConsignacion = new DaoConsignaciones().actualizarObservacionConsignacion(id_observacion, id_consignacion);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(actualizarObservacionEnConsignacion);
        out.flush();
        
    }
    
}
