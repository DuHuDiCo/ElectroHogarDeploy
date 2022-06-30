package Web;

import Datos.DaoCartera;
import Datos.DaoFiles;
import Datos.DaoObligaciones;
import Dominio.Archivo;
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

    private final String rutaFiles = "/opt/glassfish/glassfish/domains/domain1/applications/ROOT/archivos/txt/";
    private final File uploads = new File(rutaFiles);
    private final String[] extens = {".txt"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "listarFiles":
                {
                    try {
                        this.listarFiles(req, resp);
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
        String delimitante = "|";
        String ruta = "/opt/glassfish/glassfish/domains/domain1/applications/ROOT/archivos/txt/" + nombre;

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
    
     

}
