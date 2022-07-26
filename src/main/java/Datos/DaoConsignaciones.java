package Datos;

import Dominio.Consignacion;
import Dominio.Obligaciones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DaoConsignaciones {

    private static final String SQL_SELECT_CONSIGNACIONES = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion ORDER BY consignacion.fecha_creacion DESC";
    private static final String SQL_SELECT_CONSIGNACIONESBYESTADO = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.idPlataforma ,plataforma.nombre_plataforma, obligacion.nombre_titular, obligacion.n_documento, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE estado.nombre_estado = ? ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_SELECT_CONSIGNACIONESBYCEDULA = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.idPlataforma, plataforma.nombre_plataforma, obligacion.nombre_titular, obligacion.n_documento, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE obligacion.n_documento = ? ORDER BY consignacion.fecha_creacion DESC";
    private static final String SQL_SELECT_CLIENTEBYID = "SELECT obligacion.idObligacion, obligacion.nombre_titular, obligacion.saldo_capital, obligacion.fecha_obligacion, sede.idSede, sede.nombre_sede FROM obligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE n_documento = ?";
    private static final String SQL_UPDATE_CONSIGNACION = "UPDATE consignacion SET id_actualizacion = ? WHERE idConsignacion = ?";
    private static final String SQL_SELECT_CONSIGNACIONESBYSEDE = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, obligacion.n_documento, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE sede.nombre_sede = ? AND estado.nombre_estado = 'Comprobado' ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_SELECT_CONSIGNACIONESBYID = "SELECT * FROM consignacion WHERE idConsignacion = ?";
    private static final String SQL_INSERT_CONSIGNACIONTEMP = "INSERT INTO temporal_consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion, id_comprobado) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_CONSIGNACIONTEMP2 = "INSERT INTO temporal_consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_comprobado) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_DELETE_CONSIGNACIONESTEMP = "DELETE FROM temporal_consignacion WHERE id_comprobado = ?";
    private static final String SQL_DELETE_CONSIGNACIONESTEMPCAJA = "DELETE FROM temporal_consignacion_caja WHERE id_aplicado = ?";
    private static final String SQL_SELECT_CONSIGNACIONESTEMP = "SELECT * FROM temporal_consignacion WHERE id_comprobado = ?";
    private static final String SQL_SELECT_CONSIGNACIONESTEMPCAJA = "SELECT * FROM temporal_consignacion_caja";
    private static final String SQL_SELECT_CONSIGNACIONESTEMPPDF = "SELECT temporal_consignacion.idConsignacion, temporal_consignacion.num_recibo, temporal_consignacion.fecha_creacion, temporal_consignacion.fecha_pago, temporal_consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.n_documento, sede.nombre_sede FROM temporal_consignacion INNER JOIN actualizacion ON temporal_consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON temporal_consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON temporal_consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE id_comprobado = ? ORDER BY temporal_consignacion.fecha_creacion DESC ";
    private static final String SQL_SELECT_CONSIGNACIONESTEMPCAJAPDF = "SELECT temporal_consignacion_caja.idConsignacion, temporal_consignacion_caja.num_recibo, temporal_consignacion_caja.fecha_creacion, temporal_consignacion_caja.fecha_pago, temporal_consignacion_caja.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, obligacion.n_documento, sede.nombre_sede, usuario.nombre FROM temporal_consignacion_caja INNER JOIN actualizacion ON temporal_consignacion_caja.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON temporal_consignacion_caja.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON temporal_consignacion_caja.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede INNER JOIN usuario ON actualizacion.id_usuarios = usuario.idUsuario WHERE id_aplicado = ? ORDER BY temporal_consignacion_caja.fecha_creacion DESC ";
    private static final String SQL_SELECT_NOMBREUSUARIO = "SELECT nombre FROM usuario WHERE email = ?";
    private static final String SQL_UPDATE_CONSIGNACIONOBSERVACION = "UPDATE consignacion SET id_observacion = ? WHERE idConsignacion = ?";
    private static final String SQL_SELECT_OBTENERIDCONSIGNACION = "SELECT MAX(idConsignacion) FROM consignacion";
    private static final String SQL_SELEC_CONSIGNACIONEDITAR = "SELECT consignacion.idConsignacion, consignacion.num_recibo,  consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.idEstado, estado.nombre_estado, plataforma.idPlataforma, plataforma.nombre_plataforma, obligacion.idObligacion, obligacion.nombre_titular, obligacion.saldo_capital, obligacion.fecha_obligacion, sede.idSede, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE idConsignacion = ?";
    private static final String SQL_INSERT_CONSIGNACIONTEMPCAJASINOBSERVACION = "INSERT INTO temporal_consignacion_caja(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_aplicado) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_CONSIGNACIONTEMPCAJA = "INSERT INTO temporal_consignacion_caja(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion, id_aplicado) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_SELECT_CONSIGNACIONESMES = "SELECT idConsignacion FROM consignacion WHERE fecha_creacion >= ? AND fecha_creacion <= ?";
    private static final String SQL_SELECT_CONSIGNACIONESDIA = "SELECT idConsignacion FROM consignacion WHERE fecha_creacion = ?";
    private static final String SQL_SELECT_CONSIGNACIONESMESBYIDUSUARIO = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE consignacion.fecha_creacion >= ? AND consignacion.fecha_creacion <= ? AND consignacion.id_usuario = ? ";
    private static final String SQL_SELECT_CONSIGNACIONESDIABYIDUSUARIO = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE consignacion.fecha_creacion = ?  AND consignacion.id_usuario = ?";
    private static final String SQL_SELECT_CONSIGNACIONESMESBYSEDE = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE sede.nombre_sede = ? AND consignacion.fecha_creacion >= ? AND consignacion.fecha_creacion <= ? AND estado.nombre_estado = 'Comprobado'  ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_SELECT_CONSIGNACIONESDIABYSEDE = "SELECT consignacion.idConsignacion, consignacion.num_recibo, consignacion.fecha_creacion, consignacion.fecha_pago, consignacion.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.nombre_titular, sede.nombre_sede FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE sede.nombre_sede = ? AND consignacion.fecha_creacion = ? AND estado.nombre_estado = 'Comprobado'  ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_UPDATE_CONSIGNACIONOBSERVACIONTEMPORAL = "UPDATE temporal_consignacion SET id_observacion = ? WHERE idConsignacion = ?";

    public List<Consignacion> listarConsignaciones() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONES);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String num_documento = "N/A";
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, num_documento, nombre_titular, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesByEstado(String estado) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESBYESTADO);
            stmt.setString(1, estado);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                int idPlataforma = rs.getInt("idPlataforma");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String num_documento = rs.getString("n_documento");
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, num_documento, nombre_sede);
                consignaciones.setId_plataforma(idPlataforma);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesByCedula(String cedula) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESBYCEDULA);
            stmt.setString(1, cedula);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                int idPlataforma = rs.getInt("idPlataforma");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String num_cedula = rs.getString("n_documento");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, num_cedula, nombre_sede);
                consignaciones.setId_plataforma(idPlataforma);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Obligaciones> listarClienteByCedula(String cedula) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Obligaciones obliga = null;

        List<Obligaciones> obligaciones = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CLIENTEBYID);
            stmt.setString(1, cedula);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idObligacion = rs.getInt("idObligacion");
                String nombre_titular = rs.getString("nombre_titular");
                float saldo_capital = rs.getFloat("saldo_capital");
                Date fecha_obligacion = rs.getDate("fecha_obligacion");
                int idSede = rs.getInt("idSede");
                String nombre_sede = rs.getString("nombre_sede");

                obliga = new Obligaciones(idObligacion, nombre_titular, saldo_capital, fecha_obligacion, idSede, nombre_sede);
                obligaciones.add(obliga);

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return obligaciones;

    }

    public int actualizarEstadoConsig(int idActu, int idCon) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_CONSIGNACION);
            stmt.setInt(1, idActu);
            stmt.setInt(2, idCon);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }

        return rown;

    }

    public List<Consignacion> listarConsignacionesBySede(String sede) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESBYSEDE);
            stmt.setString(1, sede);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String numero_documento = rs.getString("n_documento");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, numero_documento, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public Consignacion listarConsignacionesById(int id) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESBYID);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                int id_files = rs.getInt("id_files");
                int id_actualizacion = rs.getInt("id_actualizacion");
                int id_usuario = rs.getInt("id_usuario");
                int id_plataforma = rs.getInt("id_plataforma");
                int id_obligacion = rs.getInt("id_obligacion");
                int id_observacion = rs.getInt("id_observacion");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion);

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consignaciones;

    }

    public int guardarConsigTemp(Consignacion cons) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            if (cons.getId_observacion() != 0) {
                stmt = con.prepareStatement(SQL_INSERT_CONSIGNACIONTEMP);
            } else {
                stmt = con.prepareStatement(SQL_INSERT_CONSIGNACIONTEMP2);
            }
            stmt.setInt(1, cons.getIdConsignacion());
            stmt.setString(2, cons.getNum_recibo());
            stmt.setDate(3, cons.getFecha_creacion());
            stmt.setDate(4, cons.getFecha_pago());
            stmt.setFloat(5, cons.getValor());
            stmt.setInt(6, cons.getId_files());
            stmt.setInt(7, cons.getId_actualizacion());
            stmt.setInt(8, cons.getId_usuario());
            stmt.setInt(9, cons.getId_plataforma());
            stmt.setInt(10, cons.getId_obligacion());
            if (cons.getId_observacion() != 0) {
                stmt.setInt(11, cons.getId_observacion());
                stmt.setInt(12, cons.getId_aplicado());
            } else {
                stmt.setInt(11, cons.getId_aplicado());
            }

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int guardarConsigTempCaja(Consignacion cons) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            if (cons.getId_observacion() != 0) {
                stmt = con.prepareStatement(SQL_INSERT_CONSIGNACIONTEMPCAJA);
            } else {
                stmt = con.prepareStatement(SQL_INSERT_CONSIGNACIONTEMPCAJASINOBSERVACION);
            }

            stmt.setInt(1, cons.getIdConsignacion());
            stmt.setString(2, cons.getNum_recibo());
            stmt.setDate(3, cons.getFecha_creacion());
            stmt.setDate(4, cons.getFecha_pago());
            stmt.setFloat(5, cons.getValor());
            stmt.setInt(6, cons.getId_files());
            stmt.setInt(7, cons.getId_actualizacion());
            stmt.setInt(8, cons.getId_usuario());
            stmt.setInt(9, cons.getId_plataforma());
            stmt.setInt(10, cons.getId_obligacion());
            if (cons.getId_observacion() != 0) {
                stmt.setInt(11, cons.getId_observacion());
                stmt.setInt(12, cons.getId_aplicado());
            } else {
                stmt.setInt(11, cons.getId_aplicado());
            }

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int eliminarConsigTemp(int id_usuario) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_DELETE_CONSIGNACIONESTEMP);
            stmt.setInt(1, id_usuario);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int eliminarConsigTempCaja(int id_usuario) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_DELETE_CONSIGNACIONESTEMPCAJA);
            stmt.setInt(1, id_usuario);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public List<Consignacion> listarConsinacionesTemp(int idusuario) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESTEMP);
            stmt.setInt(1, idusuario);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                int id_files = rs.getInt("id_files");
                int id_actualizacion = rs.getInt("id_actualizacion");
                int id_usuario = rs.getInt("id_usuario");
                int id_plataforma = rs.getInt("id_plataforma");
                int id_obligacion = rs.getInt("id_obligacion");
                int id_observacion = rs.getInt("id_observacion");
                int id_comprobado = rs.getInt("id_comprobado");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsinacionesTempCaja() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESTEMPCAJA);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                int id_files = rs.getInt("id_files");
                int id_actualizacion = rs.getInt("id_actualizacion");
                int id_usuario = rs.getInt("id_usuario");
                int id_plataforma = rs.getInt("id_plataforma");
                int id_obligacion = rs.getInt("id_obligacion");
                int id_observacion = rs.getInt("id_observacion");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsinacionesTempPdf(int id_comprobado) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESTEMPPDF);
            stmt.setInt(1, id_comprobado);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String n_documento = rs.getString("n_documento");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, n_documento, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsinacionesTempCajaPdf(int id_usuario) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESTEMPCAJAPDF);
            stmt.setInt(1, id_usuario);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String n_documento = rs.getString("n_documento");
                String nombre_sede = rs.getString("nombre_sede");
                String nombre_usuario = rs.getString("nombre");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, nombre_sede);
                consignaciones.setNumero_documento(n_documento);
                consignaciones.setNombreUsuario(nombre_usuario);
                consignaciones.setNombre_titular(nombre_titular);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public String obtenerNombreUsuario(String email) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String nombre = null;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBREUSUARIO);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("nombre");
                nombre = name;

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return nombre;
    }

    public int actualizarObservacionConsignacion(int id_observacion, int id_consignacion) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_CONSIGNACIONOBSERVACION);
            stmt.setInt(1, id_observacion);
            stmt.setInt(2, id_consignacion);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int obtenerIdConsignacion() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_OBTENERIDCONSIGNACION);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("MAX(idConsignacion)");
                rown = idConsignacion;

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return rown;
    }

    public Consignacion listarConsignacionesEditar(int id) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_CONSIGNACIONEDITAR);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_pago_date = rs.getDate("fecha_pago");
                String fecha_pago = Funciones.FuncionesGenerales.fechaString(fecha_pago_date);
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                int idEstado = rs.getInt("idEstado");
                String nombre_estado = rs.getString("nombre_estado");
                int idPlataforma = rs.getInt("idPlataforma");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                int idObligacion = rs.getInt("idObligacion");
                String nombre_titular = rs.getString("nombre_titular");
                float saldo = rs.getFloat("saldo_capital");
                Date fecha_obli = rs.getDate("fecha_obligacion");
                int idSede = rs.getInt("idSede");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_pago, valor, fecha_actualizacion, idEstado, nombre_estado, idPlataforma, nombre_plataforma, idObligacion, nombre_titular, saldo, fecha_obli, idSede, nombre_sede);

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consignaciones;

    }

    public List<Consignacion> listarConsignacionesMes(String fechaInicio, String fechaFin) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESMES);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");

                consignaciones = new Consignacion();
                consignaciones.setIdConsignacion(idConsignacion);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesDia(String today) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESDIA);
            stmt.setString(1, today);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");

                consignaciones = new Consignacion();
                consignaciones.setIdConsignacion(idConsignacion);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesMesByIdUsuario(String fechaInicio, String fechaFin, int id) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESMESBYIDUSUARIO);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            stmt.setInt(3, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesDiaByIdUsuario(int id, String date) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESDIABYIDUSUARIO);
            stmt.setString(1, date);
            stmt.setInt(2, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesMesBySede(String sede, String fechaInicio, String fechaFin) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESMESBYSEDE);
            stmt.setString(1, sede);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public List<Consignacion> listarConsignacionesDiaBySede(String sede, String today) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESDIABYSEDE);
            stmt.setString(1, sede);
            stmt.setString(2, today);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                float valor = rs.getFloat("valor");
                String fecha = rs.getString("fecha_actualizacion");
                DateTime fecha_actualizacion = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                String nombre_estado = rs.getString("nombre_estado");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String nombre_titular = rs.getString("nombre_titular");
                String nombre_sede = rs.getString("nombre_sede");

                consignaciones = new Consignacion(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, fecha_actualizacion, nombre_estado, nombre_plataforma, nombre_titular, nombre_sede);
                consigna.add(consignaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return consigna;

    }

    public int actualizarObservacionConsignacionTemporal(int id_observacion, int id_consignacion) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_CONSIGNACIONOBSERVACIONTEMPORAL);
            stmt.setInt(1, id_observacion);
            stmt.setInt(2, id_consignacion);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

}
