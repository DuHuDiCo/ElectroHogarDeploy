package Web;

import Datos.DaoAdmin;
import Datos.DaoEstados;
import Dominio.Estados;
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

@WebServlet(urlPatterns = {"/ServletControladorEstados"})
public class ServletControladorEstados extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "cargarEstados":
                {
                    try {
                        this.cargarEstados(req, resp);
                    } catch (ClassNotFoundException ex) {
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
}
