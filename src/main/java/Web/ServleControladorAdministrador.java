package Web;

import Datos.DaoAdmin;
import Dominio.Estados;
import Dominio.Plataforma;
import Dominio.TipoPago;
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

@WebServlet(urlPatterns = {"/ServletControladorAdministrador"})
public class ServleControladorAdministrador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarEstados": {
                    try {
                        this.listarEstados(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "llenarTipoPago":
                {
                    try {
                        this.llenarTipoPago(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;
                case "listarBancos":
                {
                    try {
                        this.listarBancos(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
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
                case "crearEstado": {
                    try {
                        this.crearEstados(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "crearTipoPago":
                {
                    try {
                        this.crearTipoPago(req,resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;
                case "crearBanco":
                {
                    try {
                        this.crearBanco(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServleControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;



            }
        }
    }

    private void crearEstados(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ClassNotFoundException {
        String estado = req.getParameter("estado");

        int finish = new DaoAdmin().crearEstados(estado);
        String respuesta = Integer.toString(finish);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(respuesta);
        out.flush();
    }

    private void listarEstados(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Estados> estados = new DaoAdmin().listarEstados();

        Gson gson = new Gson();

        String json = gson.toJson(estados);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void crearTipoPago(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String tipo_pago = req.getParameter("tipo_pago");
        
        int valid = new DaoAdmin().crearTipoPago(tipo_pago);
        
        String respuesta = Integer.toString(valid);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(respuesta);
        out.flush();
        
    }

    private void llenarTipoPago(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<TipoPago> tipoPago = new DaoAdmin().listarTipoPago();

        Gson gson = new Gson();

        String json = gson.toJson(tipoPago);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void crearBanco(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String banco = req.getParameter("banco");
        int tipo_pago = Integer.parseInt(req.getParameter("tipo_pago"));
        
        int respu = new DaoAdmin().crearBanco(banco, tipo_pago);
        
        String respuesta = Integer.toString(respu);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(respuesta);
        out.flush();
    }

    private void listarBancos(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Plataforma> bancos = new DaoAdmin().listarBancos();

        Gson gson = new Gson();

        String json = gson.toJson(bancos);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

}
