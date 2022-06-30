package Web;

import Datos.Dao;
import Datos.DaoRoles;
import Datos.DaoUsuarios;
import Dominio.Rol;
import Dominio.Usuario;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/ServletControlador"})
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "cerrarSesion": {
                    try {
                        this.cerrarSesion(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "sesion": {
                    try {
                        this.sesion(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "traerDatosPerfil": {
                    try {
                        this.obtenerDatosPerfil(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                default:
                    System.out.println("ENTRAAA");
                     {
                        try {
                            this.accionDefaul(req, resp);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

            }
        } else {
            try {
                this.accionDefaul(req, resp);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "iniciarSesion": {
                    try {
                        this.iniciarSesion(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "obtenerNombreUsuario": {
                    try {
                        this.obtenerNombreUsuario(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "actualizarPerfil": {
                    try {
                        this.actualizarPerfil(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                default:

            }
        }
    }

    private void sesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = req.getSession(true);
        String usu = (String) session.getAttribute("usuario");

       
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(usu);
        out.flush();

    }

    private void iniciarSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        if (email != null && pass != null) {

            HttpSession session = req.getSession();

            //obtenemos los datos de la base datos
            Usuario user = new Dao().iniciarSesion(email);

            if (user.getEmail().equals(email) && user.getPassword().equals(pass)) {
                // contraseña y correo correctos, inicio de sesion y se envian los datos de estado de conexion y la ultima sesion
                String conexion = "Conectado";

                int ConexionIniciada = new Dao().datosConexion(conexion, email);

                if (ConexionIniciada == 1) {
                    //recuperamos la sesion
                    session.setAttribute("usuario", email);
                    crearCookie(req, resp);
                    Gson gson = new Gson();

                    Rol rolJson = new Rol(user.getNombre_rol());

                    String json = gson.toJson(rolJson);
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();

                    out.print(json);
                    out.flush();
                }

            } else {

                Gson gson = new Gson();

                Rol role = new Rol("null");

                String json = gson.toJson(role);
                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();

                out.print(json);
                out.flush();

            }
        }
    }

    private void cerrarSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = req.getSession();

        String usuario = (String) session.getAttribute("usuario");

        if (usuario != null) {

            String conex = "Desconectado";
            int actualizacionConexion = new Dao().datosConexion(conex, usuario);
            session.removeAttribute("usuario");
            String usu = (String) session.getAttribute("usuario");
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(usu);
            out.flush();
        }

    }

    private void crearCookie(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
        Cookie[] cookiees = req.getCookies();

        boolean nuevoUsuario = true;

        if (cookiees != null) {
            for (Cookie c : cookiees) {
                if (c.getName().equals("visitanteRecurrente") && c.getValue().equals("si")) {
                    nuevoUsuario = false;
                    break;
                }
            }
        }

        if (nuevoUsuario) {
            Cookie vistanteCookie = new Cookie("visitanteRecurrente", "si");
            resp.addCookie(vistanteCookie);
        }
    }

    private void accionDefaul(HttpServletRequest req, HttpServletResponse resp) throws IOException, ClassNotFoundException {

        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");

        if (email != null) {
            String rol = new DaoRoles().obtenerRolUsuario(email);

            switch (rol) {
                case "Super Administrador":
                    resp.sendRedirect("inicioSuperAdmin.html");
                    break;
                case "Administrador":
                    resp.sendRedirect("inicioAdmin.html");
                    break;
                case "Cartera":
                    resp.sendRedirect("inicioCartera.html");
                    break;
                case "Contabilidad":
                    resp.sendRedirect("inicioContabilidad.html");
                    break;
                case "Caja":
                    resp.sendRedirect("inicioCaja.html");
                    break;
            }

        } else {
            resp.sendRedirect("login.html");
        }

    }

    private void obtenerNombreUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");

        String nombre = new Dao().obtenerNombreUsuario(email);

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(nombre);
        out.flush();
    }

    private void obtenerDatosPerfil(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);

        String email = (String) session.getAttribute("usuario");

        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);

        Usuario user = new DaoUsuarios().datosPerfil(id_usuario);
        System.out.println(user.getNombre());

        Gson gson = new Gson();

        String json = gson.toJson(user);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void actualizarPerfil(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        String nombre = req.getParameter("nombre");
        String correo = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        String password = req.getParameter("password");
        int actualizarPerfil = 0;

        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setEmail(correo);
        user.setTelefono(telefono);
        user.setPassword(password);
        user.setIdUsuario(id_usuario);

        if (password == null) {
            actualizarPerfil = new DaoUsuarios().actualizarPerfil(user);
        } else {
            actualizarPerfil = new DaoUsuarios().actualizarPerfilCompleto(user);
        }

        session.removeAttribute("usuario");

        session.setAttribute("usuario", user.getEmail());

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(actualizarPerfil);
        out.flush();

    }

}
