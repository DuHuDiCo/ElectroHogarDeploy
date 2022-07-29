package Web;

import Datos.DaoCartera;
import Datos.DaoObligaciones;
import Datos.DaoConsignaciones2;

import Dominio.Actualizacion;
import Dominio.Consignacion;
import Dominio.Archivo;
import Dominio.Plataforma;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(urlPatterns = {"/ServletControladorCartera"})
public class ServletControladorCartera extends HttpServlet {

    private final String rutaFiles = "/var/lib/tomcat9/webapps/ROOT/img/recibos";
    private final File uploads = new File(rutaFiles);
    private final String[] extens = {".ico", ".png", ".jpg", ".jpeg"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "llenarBanco": {
                    try {
                        this.llenarBanco(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorCartera.class.getName()).log(Level.SEVERE, null, ex);
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
                case "guardarConsignacion": {
                    try {
                        this.guardarConsignacion(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorCartera.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            }
        }
    }

    private void guardarConsignacion(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ClassNotFoundException, SQLException {
        String num_recibo = req.getParameter("num_recibo");
        float valor = Float.valueOf(req.getParameter("valor"));
        String fecha = req.getParameter("fecha_pago");
        Date fecha_pago = fechaSQL(fecha);
        Date fecha_creacion = obtenerFechaServer();
        String idObligacion = req.getParameter("obligacion");
        int id_obligacion = 0;
        if (idObligacion != null) {
            id_obligacion = Integer.parseInt(idObligacion);
        }
        int plataforma = Integer.parseInt(req.getParameter("plataforma"));
        String nuevoCliente = req.getParameter("nuevoCliente");
        String idSede = req.getParameter("sltSedeCon");
        int id_sede = 0;
        if (!"Sede:".equals(idSede)) {
            id_sede = Integer.parseInt(idSede);
        }
        String cedulaCliente = req.getParameter("cedulaCliente");

        Part part = req.getPart("file");
        int id_estado = obtenerIdEstado("Pendiente");
        //obtenemos el ID del usuario
        HttpSession session = req.getSession(true);
        String emaiUser = (String) session.getAttribute("usuario");
        int id_usuario = new DaoCartera().obtenerIdUsuario(emaiUser);

        if (nuevoCliente == null) {
        }

        //obtener nombre del archivo
        if (part == null) {
            System.out.println("no ha seleccionado un archivo");
            return;
        }
        //validamos si los 3 datos estan nulos, de no ser asi quiere decir que el cliente no exisita y habia que crearlo
        if (nuevoCliente != null && id_sede != 0 && cedulaCliente != null) {
            //guardamos la obligacion con los datos requeridos en el formulario de la obligacion
            int guardarObligacion = new DaoObligaciones().guardarObligacionPorCliente(nuevoCliente, id_sede, cedulaCliente);

            //obtenemos el id de la obligacion creada
            int id_obligacionCreada = new DaoObligaciones().obtenerIdObligacionCreada(cedulaCliente);

            //guardamos la consignacion con el nuevo cliente guardado
            int idConsignacion = guardar_Consignacion(part, fecha_creacion, id_usuario, id_estado, num_recibo, fecha_pago, valor, plataforma, id_obligacionCreada);
            int guardarConsignacionCareteraTemporal = new DaoConsignaciones2().obtenerConsignacionById(idConsignacion, id_usuario);
            

            String respuesta = Integer.toString(idConsignacion);
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(respuesta);
            out.flush();
        } else {
            if (isExtension(part.getSubmittedFileName(), extens)) {

                int SaveConsig = guardar_Consignacion(part, fecha_creacion, id_usuario, id_estado, num_recibo, fecha_pago, valor, plataforma, id_obligacion);
                int guardarConsignacionCareteraTemporal = new DaoConsignaciones2().obtenerConsignacionById(SaveConsig, id_usuario);
                

                resp.setContentType("text/plain");

                PrintWriter out = resp.getWriter();

                out.print(SaveConsig);
                out.flush();

            }
        }

    }

    private int guardar_Consignacion(Part part, Date fecha_creacion, int id_usuario, int id_estado, String num_recibo, Date fecha_pago, float valor, int plataforma, int id_obligacion) throws ClassNotFoundException, SQLException {
        String name = part.getSubmittedFileName();
        String photo = saveFile(part, uploads);

        Archivo file = new Archivo(name, photo, fecha_creacion, id_usuario);
        // guardamos el archivo en la BD
        int save = new DaoCartera().guardarArchivo(file);
        //obtenemos el ID de ese archivo
        int idFile = new DaoCartera().obtenerIdFile(name);
        //Guardamos la primera actualizacion(por defecto:pendiente)
        Actualizacion actu = new Actualizacion(fecha_creacion, id_estado, id_usuario);
        int upd = new DaoCartera().guardarActualizacion(actu);

        //Guardamos la consignacion en la BD
        Consignacion consig = new Consignacion(num_recibo, fecha_creacion, fecha_pago, valor, idFile, upd, id_usuario, plataforma, id_obligacion);
        int idConsignacion = new DaoCartera().guardarConsignacion(consig);

        return idConsignacion;
    }

    private int obtenerIdEstado(String dato) throws ClassNotFoundException, SQLException {

        int estado = new DaoCartera().obtenerIdEstado(dato);
        return estado;

    }

    private String saveFile(Part part, File pathUploads) {
        String rutaAbsoluta = "";

        try {

            Path path = Paths.get(part.getSubmittedFileName());
            String fileName = path.getFileName().toString();
            InputStream input = part.getInputStream();

            if (input != null) {
                File file = new File(pathUploads, fileName);
                rutaAbsoluta = file.getAbsolutePath();
                Files.copy(input, file.toPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rutaAbsoluta;
    }

    private boolean isExtension(String fileName, String[] extensions) {
        for (String et : extensions) {
            if (fileName.toLowerCase().endsWith(et)) {
                return true;
            }
        }
        return false;
    }

    private Date fechaSQL(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date javaDate = null;
        try {
            javaDate = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        return sqlDate;
    }

    private Date obtenerFechaServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        java.util.Date datObj = calendar.getTime();
        String formattedDate = sdf.format(datObj);
        java.sql.Date dateformated = fechaSQL(formattedDate);

        return dateformated;
    }

    private void llenarBanco(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Plataforma> banco = new DaoCartera().listarBanco();

        Gson gson = new Gson();

        String json = gson.toJson(banco);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }
}
