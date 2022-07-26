package Web;

import Datos.Dao;
import Datos.DaoUsuarios;
import Funciones.FuncionesGenerales;
import Dominio.Usuario;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ServletUsuarios"})

public class ServletUsuarios extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarUsuarios": {
                    try {
                        this.listarUsuarios(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "desactivarUsuario": {
                    try {
                        this.desactivarUsuario(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "activarUsuario": {
                    try {
                        this.activarUsuario(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "obtenerUsuarioById": {
                    try {
                        this.obtenerUsuarioById(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "usuarioByCedula": {
                    try {
                        this.usuarioByCedula(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "usuarioByNombre": {
                    try {
                        this.usuarioByNombre(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
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
                case "registrarUsuario": {
                    try {
                        this.registrarUsuario(req, resp);
                    } catch (ClassNotFoundException | MessagingException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
                case "actualizarUsuario": {
                    try {
                        this.actualizarUsuario(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

                default:

            }
        }
    }

    private void registrarUsuario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException, MessagingException {
        //recuperamos la informacion desde el cliente
        String nombre = req.getParameter("nombre");
        String Identificacion = req.getParameter("Identificacion");
        String TipoDoc = req.getParameter("TipoDocumento");
        String Email = req.getParameter("Email");
        String telefono = req.getParameter("telefono");
        int Rol = Integer.parseInt(req.getParameter("Rol"));
        int Sede = Integer.parseInt(req.getParameter("Sede"));
        String password = req.getParameter("password");
        //llamamos una funcion creada en el archivo Funciones Generales 
        //para obtener la fecha de creaccion
        Date fecha_creacion = FuncionesGenerales.obtenerFechaServer("yyyy-MM-dd");
        int status = 1;

        //creamos el objeto
        Usuario user = new Usuario(nombre, TipoDoc, Identificacion, Email, password, telefono, fecha_creacion, status, Rol, Sede);
        //guardamos en la base de datos
        int registrar = new Dao().crearUsuario(user);
        
        String asunto = "Datos de Inicio de Sesion Electro Hogar";
        String mensaje = "Hola Mundo Desde Electro Hogar";
        
        //Funciones.FuncionesGenerales.enviarCorreo(asunto, mensaje, Email);
        //devolvemos la respuesta
        Gson gson = new Gson();
        String json = gson.toJson(registrar);
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    private void accionDefaul(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    private void listarUsuarios(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        List<Usuario> usuarios = new DaoUsuarios().listarUsuarios();

        Gson gson = new Gson();
        String json = gson.toJson(usuarios);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

    }

    private void desactivarUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        int desactivarUsuario = new DaoUsuarios().desactivarUsuario(idUsuario);

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print(desactivarUsuario);
        out.flush();
    }

    private void activarUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        int desactivarUsuario = new DaoUsuarios().activarUsuario(idUsuario);

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print(desactivarUsuario);
        out.flush();
    }

    private void obtenerUsuarioById(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));

        Usuario user = new DaoUsuarios().obtenerUsuarioById(idUsuario);

        Gson gson = new Gson();
        String json = gson.toJson(user);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    private void actualizarUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        String documento = req.getParameter("documento");
        String telefono = req.getParameter("telefono");
        int sede = Integer.parseInt(req.getParameter("sede"));
        int rol = Integer.parseInt(req.getParameter("rol"));

        Usuario user = new Usuario();
        user.setIdUsuario(idUsuario);
        user.setNombre(nombre);
        user.setEmail(email);
        user.setN_documento(documento);
        user.setTelefonoUser(telefono);
        user.setId_sede(sede);
        user.setId_rol(rol);

        int actualizarUsuario = new DaoUsuarios().actualizarUsuario(user);
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print(actualizarUsuario);
        out.flush();
    }

    private void usuarioByCedula(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException, SQLException {
        String dato = req.getParameter("Dato");

        Usuario userCedula = new DaoUsuarios().obtenerUsuarioByCedula(dato);
        Gson gson = new Gson();
        String json = gson.toJson(userCedula);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

    }
    
    private void usuarioByNombre(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException, SQLException {
        String dato = req.getParameter("Dato");

        Usuario userCedula = new DaoUsuarios().obtenerUsuarioByNombre(dato);
        Gson gson = new Gson();
        String json = gson.toJson(userCedula);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

    }

}
