package Web;

import Datos.DaoCartera;
import Datos.DaoConsignaciones2;
import Datos.DaoFiles;
import Datos.DaoObligaciones;
import Datos.DaoRoles;
import Datos.DaoUsuarios;
import Dominio.Archivo;
import Dominio.Consignacion;
import Dominio.Obligaciones;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
@WebServlet(urlPatterns = {"/ServletControladorFiles"})
public class ServletControladorFiles extends HttpServlet {

    private final String rutaFiles = "/var/lib/tomcat9/webapps/ElectroHogar-1.0/archivos/txt/";
    private final File uploads = new File(rutaFiles);
    private final String[] extens = {".txt"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarFiles": {
                    try {
                        this.listarFiles(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "obtenerRutaImagen": {
                    try {
                        this.obtenerRutaImagen(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "generarReporte": {
                    try {
                        this.generarReporte(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "traerReportesByIdUsuario": {
                    try {
                        this.traerReportesByIdUsuario(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "traerReportesByFecha": {
                    try {
                        this.traerReportesByFecha(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "traerReportesAdminByFecha": {
                    try {
                        this.traerReportesAdminByFecha(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "traerReportesAdmin": {
                    try {
                        this.traerReportesAdmin(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "descargarReporte": {
                    try {
                        this.descargarReporte(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "descargarReporteAdmin": {
                    try {
                        this.descargarReporteAdmin(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "traerReportesAdminByUsuario": {
                    try {
                        this.traerReportesAdminByUsuario(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                default:
                    this.accionDefaul(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "guardartxt": {
                    try {
                        this.guardarTxt(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorFiles.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
    }

    private void guardarTxt(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ClassNotFoundException, SQLException {
        Part part = req.getPart("file");
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");

        if (part == null) {
            System.out.println("no ha seleccionado un archivo");
            return;
        }

        if (isExtension(part.getSubmittedFileName(), extens)) {
            String name = crearNombreArchivo();
            String photo = saveFile(part, uploads, name);
            Date fecha = obtenerFechaServer();

            int id_usuario = new DaoCartera().obtenerIdUsuario(email);

            Archivo file = new Archivo(name, photo, fecha, id_usuario);
            int save = new DaoFiles().guardarArchivoTxt(file);

            //leemos el archivo y guardamos en base datos
            int leerArchivo = leerTxt(name);

            String respuesta = Integer.toString(leerArchivo);
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(respuesta);
            out.flush();
        }
    }

    private String saveFile(Part part, File pathUploads, String name) {
        String rutaAbsoluta = "";

        try {

            Path path = Paths.get(part.getSubmittedFileName());
            String fileName = name;
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

    private int leerTxt(String nombre) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        int obtenerIdTxt = new DaoFiles().obtenerIdFileTxt(nombre);
        String linea = "";
        String delimitante = "\\|";
        String ruta = "/var/lib/tomcat9/webapps/ElectroHogar-1.0/archivos/txt/" + nombre;

        Obligaciones obligacion = null;
        int guardarObliga = 0;

        try {
            FileReader fileReader = new FileReader(ruta);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((linea = reader.readLine()) != null) {
                String[] campo = linea.split(delimitante);
                String nombreTitular = campo[0];
                String tipoDoc = campo[1];
                String documento = campo[2];

                String telefono = campo[3];
                String email = campo[4];
                String direccion = campo[5];
                String clasi_cliente = campo[6];
                String codigo_cliente = campo[7];
                String valorCuota = campo[8];
                float valor_cuota = Float.valueOf(valorCuota);
                String saldoCapital = campo[9];
                float saldo_capital = Float.valueOf(saldoCapital);
                String fecha = campo[10];
                Date fecha_obligacion = Funciones.FuncionesGenerales.fechaSQL(fecha, "yyyy-MM-dd");
                String saldoMora = campo[11];
                float saldo_mora = Float.valueOf(saldoMora);
                String diasMora = campo[12];
                int dias_mora = Integer.parseInt(diasMora);
                String dato_perso = campo[13];
                String idSede = campo[14];
                int id_sede = Integer.parseInt(idSede);

                obligacion = new Obligaciones(nombreTitular, tipoDoc, documento, telefono, email, direccion, clasi_cliente, codigo_cliente, valor_cuota, saldo_capital, fecha_obligacion, saldo_mora, dias_mora, id_sede, obtenerIdTxt);
                guardarObliga = new DaoObligaciones().guardarObligaciones(obligacion);

            }

        } catch (IOException | ClassNotFoundException | NumberFormatException | SQLException e) {
            System.out.println(e);
        }
        return guardarObliga;
    }

    private String crearNombreArchivo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();

        java.util.Date datObj = calendar.getTime();
        String fecha = sdf.format(datObj);

        String nombreArc = "txtDatos-" + fecha + ".txt";
        return nombreArc;

    }

    private Date obtenerFechaServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        java.util.Date datObj = calendar.getTime();
        String formattedDate = sdf.format(datObj);
        java.sql.Date dateformated = fechaSQL(formattedDate);

        return dateformated;
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

    private void listarFiles(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Archivo> listaFiles = new DaoFiles().listarFiles();

        Gson gson = new Gson();

        String json = gson.toJson(listaFiles);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void accionDefaul(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("usuario") != null) {

//            resp.sendRedirect("inicio.html");
        } else {
            resp.sendRedirect("login.html");
        }
    }

    private void obtenerRutaImagen(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, ClassNotFoundException, SQLException, IOException {
        int idConsignacion = Integer.parseInt(req.getParameter("idConsignacion"));

        int idFileImagen = new DaoFiles().obtenerIdFileImg(idConsignacion);

        String nombreArchivo = new DaoFiles().obtenerNombreFile(idFileImagen);
        String ruta = "archivos/img/" + nombreArchivo;

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(ruta);
        out.flush();

    }

    private void generarReporte(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException, SQLException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        List<Consignacion> consigTempCartera = new DaoConsignaciones2().listarConsignacionesTempoCartera(id_usuario);
        if (consigTempCartera.size() > 0) {

            String generarPdf = Funciones.FuncionesGenerales.generarPdf(consigTempCartera, email);
            if (generarPdf != null || !"".equals(generarPdf)) {
                String nombreArchivo = Funciones.FuncionesGenerales.nombreArchivo(generarPdf);
                System.out.println(nombreArchivo);

                Archivo file = new Archivo(nombreArchivo, generarPdf);
                file.setId_usuario(id_usuario);

                int guardarArchivo = new DaoFiles().guardarArchivoReportes(file);

                int elimnarTemporalCartera = new DaoConsignaciones2().eliminarTemporalCartera(id_usuario);
                resp.setContentType("text/plain");

                PrintWriter out = resp.getWriter();

                out.print(elimnarTemporalCartera);
                out.flush();
            } else {
                String error = "Error";
                resp.setContentType("text/plain");

                PrintWriter out = resp.getWriter();

                out.print(error);
                out.flush();
            }

        } else {
            String error = "null";
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(error);
            out.flush();
        }

    }

    private void traerReportesByIdUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        List<Archivo> reportes = new DaoFiles().listarFilesByIdUsuario(id_usuario);

        Gson gson = new Gson();

        String json = gson.toJson(reportes);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void traerReportesByFecha(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        String fecha = req.getParameter("fecha");

        String fecha1 = fecha + " 00:00:00";
        String fecha2 = fecha + " 23:59:59";

        HttpSession session = req.getSession(true);
        String mail = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(mail);
        List<Archivo> reportes = new DaoFiles().listarFilesByFecha(id_usuario, fecha1, fecha2);

        Gson gson = new Gson();

        String json = gson.toJson(reportes);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void descargarReporte(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idFile = Integer.parseInt(req.getParameter("idFile"));
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        String rol = new DaoRoles().obtenerRolUsuario(email);
        String ruta = null;
        String nombreReporte = new DaoFiles().obtenerNombreReporte(idFile);
        switch (rol) {
            case "Administrador":
                ruta = "archivos/reportes/Admin/" + nombreReporte;
                break;
            case "Cartera":
                ruta = "archivos/reportes/Cartera/" + nombreReporte;
                break;
            case "Contabilidad":
                ruta = "archivos/reportes/contabilidad/" + nombreReporte;
                break;
            case "Caja":
                ruta = "archivos/reportes/Caja/" + nombreReporte;
                break;
        }

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(ruta);
        out.flush();
    }

    private void traerReportesAdmin(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Archivo> reportes = new DaoFiles().listarReportesAdmin();
        Gson gson = new Gson();

        String json = gson.toJson(reportes);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void descargarReporteAdmin(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idFile = Integer.parseInt(req.getParameter("idFile"));
        String ruta = null;
        Archivo reporte = new DaoFiles().obtenerNombreUsuarioArchivo(idFile);

        String rol = new DaoRoles().obtenerRolUsuario(reporte.getEmail());
        switch (rol) {
            case "Administrador":
                ruta = "archivos/reportes/Admin/" + reporte.getNombre_archivo();
                break;
            case "Cartera":
                ruta = "archivos/reportes/Cartera/" + reporte.getNombre_archivo();
                break;
            case "Contabilidad":
                ruta = "archivos/reportes/Contabilidad/" + reporte.getNombre_archivo();
                break;
            case "Caja":
                ruta = "archivos/reportes/Caja/" + reporte.getNombre_archivo();
                break;
        }

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(ruta);
        out.flush();

    }

    private void traerReportesAdminByFecha(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        String fecha = req.getParameter("fecha");

        String fecha1 = fecha + " 00:00:00";
        String fecha2 = fecha + " 23:59:59";

        HttpSession session = req.getSession(true);
        String mail = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(mail);
        List<Archivo> reportes = new DaoFiles().listarFilesAdminByFecha(id_usuario, fecha1, fecha2);

        Gson gson = new Gson();

        String json = gson.toJson(reportes);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void traerReportesAdminByUsuario(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        String fecha = req.getParameter("fecha");
        List<Archivo> reportes = null;
        if (fecha == null) {
            reportes = new DaoFiles().listarFilesByIdUsuario(idUsuario);
        } else {
            String fecha1 = fecha + " 00:00:00";
            String fecha2 = fecha + " 23:59:59";
             reportes = new DaoFiles().listarFilesByFecha(idUsuario, fecha1, fecha2);
        }
         Gson gson = new Gson();

        String json = gson.toJson(reportes);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
        
    }

}
