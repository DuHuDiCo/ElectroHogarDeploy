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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/ServletUsuarios"})

public class ServletUsuarios extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarUsuarios":  {
                try {
                    this.listarUsuarios(req, resp);
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
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                break;

                default:

            }
        }
    }

    private void registrarUsuario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ClassNotFoundException {
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

}
