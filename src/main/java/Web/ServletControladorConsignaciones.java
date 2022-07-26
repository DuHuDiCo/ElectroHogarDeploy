package Web;

import Datos.DaoActualizacion;
import Datos.DaoConsignaciones;
import Datos.DaoEstados;
import Datos.DaoFiles;
import Datos.DaoObservacion;
import Datos.DaoUsuarios;
import Dominio.Actualizacion;
import Dominio.Archivo;
import Dominio.Consignacion;
import Dominio.Obligaciones;
import Dominio.Observaciones;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@WebServlet(urlPatterns = {"/ServletControladorConsignaciones"})
public class ServletControladorConsignaciones extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String accion = req.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "listarConsignaciones": {
                    try {
                        this.listarConsignaciones(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "listarConsignacionesByEstado": {
                    try {
                        this.listarConsignacionesByEstado(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "listarConsignacionesByCedula": {
                    try {
                        this.listarConsignacionesCedula(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "listarClienteByCedula": {
                    try {
                        this.listarClientesByCedula(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "listarConsignacionesCaja": {
                    try {
                        this.listarConsignacionesCaja(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "cancelarCambios": {
                    try {
                        this.cancelarCambios(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "cancelarCambiosCaja": {
                    try {
                        this.cancelarCambiosCaja(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "guardarCambios": {
                    try {
                        this.guardarCambios(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "guardarCambiosCaja": {
                    try {
                        this.guardarCambiosCaja(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "editarConsignacion": {
                    try {
                        this.editarConsignacion(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "consignacionesMes": {
                    try {
                        this.consignacionesMes(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "consignacionesDia": {
                    try {
                        this.consignacionesDia(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "consignacionesDevueltas": {
                    try {
                        this.consignacionesDevueltas(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "consignacionesPendientes": {
                    try {
                        this.consignacionesPendientes(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "consignacionesComprobadas": {
                    try {
                        this.consignacionesComprobadas(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "consignacionesAplicadas": {
                    try {
                        this.consignacionesAplicadas(req, resp);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
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
                case "actualizarConsignacion": {
                    try {
                        this.actualizarConsignacion(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "ConsignacionTemporal": {
                    try {
                        this.ConsignacionTemporal(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "ConsignacionTemporalCaja": {
                    try {
                        this.ConsignacionTemporalCaja(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "devolverConsignaciones": {
                    try {
                        this.devolverConsiganciones(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "ConsignacionTemporalDevolver": {
                    try {
                        this.ConsignacionTemporalDevolver(req, resp);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ServletControladorConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            }
        }

    }

    private void listarConsignaciones(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsignaciones();

        Gson gson = new Gson();

        String json = gson.toJson(consignaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void listarConsignacionesByEstado(HttpServletRequest req, HttpServletResponse resp) throws IOException, ClassNotFoundException {

        String estado = req.getParameter("estado");

        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsignacionesByEstado(estado);

        Gson gson = new Gson();

        String json = gson.toJson(consignaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void listarConsignacionesCedula(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String cedula = req.getParameter("cedula");

        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsignacionesByCedula(cedula);

        Gson gson = new Gson();

        String json = gson.toJson(consignaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void listarClientesByCedula(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {

        String cedula = req.getParameter("cedula");

        List<Obligaciones> obligaciones = new DaoConsignaciones().listarClienteByCedula(cedula);

        Gson gson = new Gson();

        String json = gson.toJson(obligaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void actualizarConsignacion(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idConsignaciones = Integer.parseInt(req.getParameter("idConsignacion"));
        HttpSession sesion = req.getSession(true);
        String email = (String) sesion.getAttribute("usuario");
        String estado = req.getParameter("estado");
        int idEstado = new DaoEstados().obtenerIdEstado(estado);
        int idUsuario = new DaoUsuarios().obtenerIdUsuario(email);
        Date fecha = Funciones.FuncionesGenerales.obtenerFechaServer("yyyy-MM-dd");

        Actualizacion actu = new Actualizacion(fecha, idEstado, idUsuario);

        int idActualizacion = new DaoActualizacion().guardarActualizacion(actu);

        int updateConsignacion = new DaoConsignaciones().actualizarEstadoConsig(idActualizacion, idConsignaciones);

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(updateConsignacion);
        out.flush();

    }

    private void listarConsignacionesCaja(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        HttpSession sesion = req.getSession(true);
        String email = (String) sesion.getAttribute("usuario");

        String sede = new DaoUsuarios().obtenerSedeUsuario(email);

        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsignacionesBySede(sede);

        Gson gson = new Gson();

        String json = gson.toJson(consignaciones);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();
    }

    private void ConsignacionTemporal(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));

        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);

        Consignacion conTemp = new DaoConsignaciones().listarConsignacionesById(id_consignacion);
        conTemp.setId_aplicado(id_usuario);

        int guardarConsignacionesTemp = new DaoConsignaciones().guardarConsigTemp(conTemp);

        id_consignacion = 0;
        conTemp = null;

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(guardarConsignacionesTemp);
        out.flush();
    }

    private void cancelarCambios(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        int eliminarTempo = new DaoConsignaciones().eliminarConsigTemp(id_usuario);
        int eliminarObservaTemp = new DaoObservacion().eliminarObserTemp();

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(eliminarTempo);
        out.flush();

    }

    private void guardarCambios(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession sesion = req.getSession(true);
        String email = (String) sesion.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsinacionesTemp(id_usuario);

        System.out.println(email);
        int confirmacion = 0;
        String estado = "Comprobado";

        for (Consignacion consig : consignaciones) {

            if (consig.getId_observacion() == 0) {

                //obtenemos datos para guardar los datos de la actualizacion
                int id_estado = new DaoEstados().obtenerIdEstado(estado);

                Actualizacion actu = new Actualizacion(id_estado, id_usuario);
                //guardamos la actualizacion
                int guardarActu = new DaoActualizacion().guardarActualizacion(actu);

                //enviamos el id de la actualizacion a la consignacion con el idConsignacion correspondiente
                int actualizarConsig = new DaoConsignaciones().actualizarEstadoConsig(guardarActu, consig.getIdConsignacion());
                confirmacion = actualizarConsig;

            } else {
                int id_observacion = consig.getId_observacion();
                Observaciones obs = new DaoObservacion().obtenerObservacionTemporalById(id_observacion);
                if (obs.getError() == null) {
                    int crearObservacionTablaObservacion = new DaoObservacion().guardarObservacion(obs);
                    int actualizarObservacionConsig = new DaoConsignaciones().actualizarObservacionConsignacion(crearObservacionTablaObservacion, consig.getIdConsignacion());
                    estado = "Devuelta";

                    int id_estado = new DaoEstados().obtenerIdEstado(estado);

                    Actualizacion actu = new Actualizacion(id_estado, id_usuario);
                    //guardamos la actualizacion
                    int guardarActu = new DaoActualizacion().guardarActualizacion(actu);

                    //enviamos el id de la actualizacion a la consignacion con el idConsignacion correspondiente
                    int actualizarConsig = new DaoConsignaciones().actualizarEstadoConsig(guardarActu, consig.getIdConsignacion());
                    confirmacion = actualizarConsig;
                } else {

                    //obtenemos datos para guardar los datos de la actualizacion
                    int id_estado = new DaoEstados().obtenerIdEstado(estado);

                    Actualizacion actu = new Actualizacion(id_estado, id_usuario);
                    //guardamos la actualizacion
                    int guardarActu = new DaoActualizacion().guardarActualizacion(actu);

                    //enviamos el id de la actualizacion a la consignacion con el idConsignacion correspondiente
                    int actualizarConsig = new DaoConsignaciones().actualizarEstadoConsig(guardarActu, consig.getIdConsignacion());
                    confirmacion = actualizarConsig;

                }
            }

        }

        if (confirmacion == 1) {

            List<Consignacion> conTemp = new DaoConsignaciones().listarConsinacionesTempPdf(id_usuario);

            String ruta = Funciones.FuncionesGenerales.generarPdf(conTemp, email);
            String nombreArchivo = Funciones.FuncionesGenerales.nombreArchivo(ruta);

            DateTime fechaHora = Funciones.FuncionesGenerales.stringToDateTime(Funciones.FuncionesGenerales.fechaDateTime());
            Archivo file = new Archivo(nombreArchivo, ruta, fechaHora, id_usuario);
            int guardarFile = new DaoFiles().guardarArchivoReportes(file);
            int eliminarTemp = new DaoConsignaciones().eliminarConsigTemp(id_usuario);
            int eliminarObservacionesTemp = new DaoObservacion().eliminarObserTemp();
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(eliminarTemp);
            out.flush();
        } else {
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(confirmacion);
            out.flush();
        }
    }

    private void devolverConsiganciones(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession sesion = req.getSession();
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));
        String observacion = req.getParameter("observacion");

        int id_estado = new DaoEstados().obtenerIdEstado("Devuelta");
        String email = (String) sesion.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);

        Observaciones observa = new Observaciones(observacion, id_usuario, id_consignacion);
        //guarda la observacion y obtiene el id, todo en este metodo 
        int id_observacion = new DaoObservacion().guardarObservacion(observa);

        //le enviamos el id de la observacion y el id de la consignacion para actualizarlo en la tabla consignaciones
        int actualizarObservacionConsignacion = new DaoConsignaciones().actualizarObservacionConsignacion(id_observacion, id_consignacion);

        Actualizacion actu = new Actualizacion(id_estado, id_usuario);
        int crearActua = new DaoActualizacion().guardarActualizacion(actu);
        int id_actualizacion = new DaoActualizacion().obtenerIdActualizacion();

        int actualizarConsi = new DaoConsignaciones().actualizarEstadoConsig(id_actualizacion, id_consignacion);

        if (actualizarObservacionConsignacion == 1 && actualizarConsi == 1) {
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(actualizarConsi);
            out.flush();
        } else {
            resp.sendError(500, "Error 500, Internal Server Error");
        }

    }

    private void editarConsignacion(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));

        Consignacion consignacion = new DaoConsignaciones().listarConsignacionesEditar(id_consignacion);

        Gson gson = new Gson();

        String json = gson.toJson(consignacion);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        out.print(json);
        out.flush();

    }

    private void ConsignacionTemporalCaja(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int id_consignacion = Integer.parseInt(req.getParameter("idConsignacion"));
        Consignacion conTemp = new DaoConsignaciones().listarConsignacionesById(id_consignacion);
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        conTemp.setId_aplicado(id_usuario);

        int guardarConsignacionesTemp = new DaoConsignaciones().guardarConsigTempCaja(conTemp);

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(guardarConsignacionesTemp);
        out.flush();
    }

    private void guardarCambiosCaja(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {

        HttpSession sesion = req.getSession(true);
        String email = (String) sesion.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        List<Consignacion> consignaciones = new DaoConsignaciones().listarConsinacionesTempCajaPdf(id_usuario);
        int confirmacion = 0;

        Consignacion cons = null;
        for (Consignacion con : consignaciones) {

            int id_estado = new DaoEstados().obtenerIdEstado("Aplicado");
            Actualizacion actu = new Actualizacion(id_estado, id_usuario);
            int guardarActu = new DaoActualizacion().guardarActualizacion(actu);
            int actualizarConsig = new DaoConsignaciones().actualizarEstadoConsig(guardarActu, con.getIdConsignacion());
            confirmacion = actualizarConsig;
        }

        if (confirmacion == 1) {

            String ruta = Funciones.FuncionesGenerales.generarPdfCaja(consignaciones, email);
            String nombreArchivo = Funciones.FuncionesGenerales.nombreArchivo(ruta);

            DateTime fechaHora = Funciones.FuncionesGenerales.stringToDateTime(Funciones.FuncionesGenerales.fechaDateTime());
            Archivo file = new Archivo(nombreArchivo, ruta, fechaHora, id_usuario);
            int guardarFile = new DaoFiles().guardarArchivoReportes(file);
            int eliminarTemp = new DaoConsignaciones().eliminarConsigTempCaja(id_usuario);
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(eliminarTemp);
            out.flush();
        } else {
            resp.setContentType("text/plain");

            PrintWriter out = resp.getWriter();

            out.print(confirmacion);
            out.flush();
        }

    }

    private void cancelarCambiosCaja(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        HttpSession session = req.getSession(true);
        String email = (String) session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);

        int eliminarTempo = new DaoConsignaciones().eliminarConsigTempCaja(id_usuario);

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(eliminarTempo);
        out.flush();
    }

    private void consignacionesMes(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String fechaInicoMes = Funciones.FuncionesGenerales.fechaInicioMes();
        String fechaFinMes = Funciones.FuncionesGenerales.fechaFinMes();
        System.out.println(fechaInicoMes);
        System.out.println(fechaFinMes);
        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesMes(fechaInicoMes, fechaFinMes);

        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void consignacionesDia(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        LocalDate date = LocalDate.now();
        String today = date.toString();

        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesDia(today);
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void consignacionesDevueltas(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesByEstado("Devuelta");
        System.out.println(congs.size());
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void consignacionesPendientes(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesByEstado("Pendiente");
        System.out.println(congs.size());
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void consignacionesComprobadas(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesByEstado("Comprobado");
        System.out.println(congs.size());
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void consignacionesAplicadas(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IOException {
        List<Consignacion> congs = new DaoConsignaciones().listarConsignacionesByEstado("Aplicado");
        System.out.println(congs.size());
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(congs.size());
        out.flush();
    }

    private void ConsignacionTemporalDevolver(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, SQLException, IOException {
        int idCosignacion = Integer.parseInt(req.getParameter("idConsignacion"));
        System.out.println(idCosignacion);
        String mensaje = req.getParameter("observacion");
        
        HttpSession session = req.getSession(true);
        String email = (String)session.getAttribute("usuario");
        int id_usuario = new DaoUsuarios().obtenerIdUsuario(email);
        
        Consignacion conTemp = new DaoConsignaciones().listarConsignacionesById(idCosignacion);
        conTemp.setId_aplicado(id_usuario);
        int temporal = new DaoConsignaciones().guardarConsigTemp(conTemp);

        int observacionTemporal = new DaoObservacion().observacionTemporal(mensaje, id_usuario);

        int enviarIdConsignacion = new DaoObservacion().enviarIdConsignacion(conTemp.getIdConsignacion(), observacionTemporal);

        int actualizarConsigObser = new DaoConsignaciones().actualizarObservacionConsignacionTemporal(observacionTemporal, conTemp.getIdConsignacion());
        resp.setContentType("text/plain");

        PrintWriter out = resp.getWriter();

        out.print(actualizarConsigObser);
        out.flush();
    }
}
