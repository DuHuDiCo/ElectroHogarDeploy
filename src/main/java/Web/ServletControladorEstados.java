package Web;

import Datos.DaoAdmin;
import Datos.DaoEstados;
import Dominio.Estados;
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

@WebServlet(urlPatterns = {"/ServletControladorEstados"})
public class ServletControladorEstados extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "cargarEstados": {
                    try {
                        this.cargarEstados(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorEstados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "obtenerEstadoById": {
                    try {
                        this.obtenerEstadoById(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorEstados.class.getName()).log(Level.SEVERE, null, ex);
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
                case "actualizarEstado": {
                    try {
                        this.actualizarEstado(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorEstados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
        }
    }

    private void cargarEstados(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Estados> estados = new DaoEstados().listarEstados();

        Gson gson = new Gson();

        String json = gson.toJson(estados);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void obtenerEstadoById(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idEstado = Integer.parseInt(req.getParameter("idEstado"));
        Estados estado = new DaoEstados().obtenerEstadoById(idEstado);
        Gson gson = new Gson();

        String json = gson.toJson(estado);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void actualizarEstado(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idEstado = Integer.parseInt(req.getParameter("idEstado"));
        String estado = req.getParameter("nombre_estado");
        
        Estados est = new Estados(idEstado, estado);
        
        int actualizarEstado = new DaoEstados().actualizarEstados(est);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(actualizarEstado);
        out.flush();
    }
}
