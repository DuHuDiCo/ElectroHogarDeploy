package Datos;

import Dominio.Consignacion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DaoConsignaciones2 {

    private static final String SQL_SELECT_CONSIGNACIONESAPLICADASBYIDUSUARIO = "SELECT consignacion.idConsignacion FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado  WHERE estado.nombre_estado = 'Aplicado' AND actualizacion.id_usuarios = ? ";
    private static final String SQL_SELECT_CONSIGNACIONESDIACOMPROBADAS = "SELECT consignacion.idConsignacion FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE estado.nombre_estado = ? AND consignacion.fecha_creacion = ? ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_UPDATE_CONSIGNACIONCARTERA = "UPDATE consignacion SET num_recibo = ?, fecha_pago = ?, valor = ?, id_plataforma = ?, id_obligacion = ? WHERE idConsignacion = ?";
    private static final String SQL_INSERT_CONSIGNACIONTEMPORALCARTERA = "INSERT INTO temporal_consignacion_cartera(idConsignacion, num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion, id_observacion, id_guardado)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_SELECT_CONSIGNACIONBYID = "SELECT * FROM consignacion WHERE idConsignacion = ?";
    private static final String SQL_SELECT_CONSIGNACIONESTEMPORALESCARTERA = "SELECT temporal_consignacion_cartera.idConsignacion, temporal_consignacion_cartera.num_recibo, temporal_consignacion_cartera.fecha_creacion, temporal_consignacion_cartera.fecha_pago, temporal_consignacion_cartera.valor, actualizacion.fecha_actualizacion, estado.nombre_estado, plataforma.nombre_plataforma, obligacion.n_documento, sede.nombre_sede FROM temporal_consignacion_cartera INNER JOIN actualizacion ON temporal_consignacion_cartera.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON temporal_consignacion_cartera.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON temporal_consignacion_cartera.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE id_guardado = ? ORDER BY temporal_consignacion_cartera.fecha_creacion DESC ";
    private static final String SQL_DELETE_TEMPORALCARTERA = "DELETE FROM temporal_consignacion_cartera WHERE id_guardado = ?";

    public List<Consignacion> listarConsignacionesAplicadasByIdUsuario(int id) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESAPLICADASBYIDUSUARIO);
            stmt.setInt(1, id);

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

    public List<Consignacion> listarConsignacionesDiaContabilidad(String estado, String f) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESDIACOMPROBADAS);
            stmt.setString(1, estado);
            stmt.setString(2, f);

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

    public int actualizarConsignacion(Consignacion cons) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_CONSIGNACIONCARTERA);
            stmt.setString(1, cons.getNum_recibo());
            stmt.setDate(2, cons.getFecha_pago());
            stmt.setFloat(3, cons.getValor());
            stmt.setInt(4, cons.getId_plataforma());
            stmt.setInt(5, cons.getId_obligacion());
            stmt.setInt(6, cons.getIdConsignacion());

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int obtenerConsignacionById(int id, int idusuario) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONBYID);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idConsignacion");
                String num_recibo = rs.getString("num_recibo");
                Date fecha_creacion = rs.getDate("fecha_creacion");
                Date fecha_pago = rs.getDate("fecha_pago");
                Float valor = rs.getFloat("valor");
                int id_file = rs.getInt("id_files");
                int id_actualizacion = rs.getInt("id_actualizacion");
                int id_usuario = rs.getInt("id_usuario");
                int id_plataforma = rs.getInt("id_plataforma");
                int id_obligacion = rs.getInt("id_obligacion");

                consignaciones = new Consignacion();
                consignaciones.setIdConsignacion(idConsignacion);
                consignaciones.setNum_recibo(num_recibo);
                consignaciones.setFecha_creacion(fecha_creacion);
                consignaciones.setFecha_pago(fecha_pago);
                consignaciones.setValor(valor);
                consignaciones.setId_files(id_file);
                consignaciones.setId_actualizacion(id_actualizacion);
                consignaciones.setId_usuario(id_usuario);
                consignaciones.setId_plataforma(id_plataforma);
                consignaciones.setId_obligacion(id_obligacion);

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return guardarConsignacionCarteraTemporal(consignaciones, idusuario);

    }

    public int guardarConsignacionCarteraTemporal(Consignacion cons, int idusuario) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_CONSIGNACIONTEMPORALCARTERA);
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
            stmt.setInt(11, cons.getId_observacion());
            stmt.setInt(12, idusuario);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    public List<Consignacion> listarConsignacionesTempoCartera(int id_usuario) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consignacion consignaciones = null;

        List<Consignacion> consigna = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_CONSIGNACIONESTEMPORALESCARTERA);
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
    
    
    public int eliminarTemporalCartera(int id_usuario) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_DELETE_TEMPORALCARTERA);
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

}
