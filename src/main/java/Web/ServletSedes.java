package Web;

import Datos.DaoSedes;
import Datos.DaoUsuarios;
import Dominio.Sedes;
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

@WebServlet(urlPatterns = {"/ServletSedes"})

public class ServletSedes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarSede": {
                    try {
                        this.listarSede(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "obtenerSedeById": {
                    try {
                        this.obtenerSedeById(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "eliminarSede": {
                    try {
                        this.eliminarSede(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "obtenerSede": {
                    try {
                        this.obtenerSede(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                    Logger.getLogger(ServletSedes.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                break;

                default:

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "guardarSede": {
                    try {
                        this.guardarSede(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "actualizarSede": {
                    try {
                        this.actualizarSede(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                default:

            }
        }

    }

    private void listarSede(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {

        List<Sedes> Sedes = new DaoSedes().listarSedes();
        Gson gson = new Gson();
        String json = gson.toJson(Sedes);
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        out.print(json);
        out.flush();
    }

    private void accionDefaul(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    private void guardarSede(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String sede = req.getParameter("sede");
        String municipio = req.getParameter("municipio");
        String telefono = req.getParameter("telefono");
        String dato = req.getParameter("dato_personalizado");
        String dato_personalizado = null;
        if (dato == null) {
            dato_personalizado = "NA";
        }

        Sedes sedes = new Sedes(sede, municipio, telefono, dato_personalizado);

        int guardarSede = new DaoSedes().guardarSedes(sedes);

        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        out.print(guardarSede);
        out.flush();
    }

    private void obtenerSedeById(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idSede = Integer.parseInt(req.getParameter("idSede"));

        Sedes sede = new DaoSedes().obtenerSedeById(idSede);

        Gson gson = new Gson();
        String json = gson.toJson(sede);
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        out.print(json);
        out.flush();
    }

    private void actualizarSede(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idSede = Integer.parseInt(req.getParameter("idSede"));
        String nombre_sede = req.getParameter("nombre_sede");
        String municipio = req.getParameter("municipio");
        String telefono = req.getParameter("telefono");
        String datoPerso = req.getParameter("datoPer");

        Sedes sede = new Sedes(idSede, nombre_sede, municipio, telefono, datoPerso);

        int actualizarSede = new DaoSedes().actualizarSede(sede);

        PrintWriter out = resp.getWriter();

        resp.setContentType("text/plain");
        out.print(actualizarSede);
        out.flush();
    }

    private void eliminarSede(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idSede = Integer.parseInt(req.getParameter("idSede"));

        int eliminarSede = new DaoSedes().eliminarSede(idSede);

        PrintWriter out = resp.getWriter();

        resp.setContentType("text/plain");
        out.print(eliminarSede);
        out.flush();
    }

    private void obtenerSede(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);

        String sede = new DaoSedes().obtenerSedeByIdUsuario(id_usuario);
        PrintWriter out = resp.getWriter();

        resp.setContentType("text/plain");
        out.print(sede);
        out.flush();

    }

}
