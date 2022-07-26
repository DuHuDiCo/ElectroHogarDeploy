package Datos;

import Dominio.Observaciones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DaoObservacion {

    private static final String SQL_INSERT_OBSERVACIONES = "INSERT INTO observaciones(observacion, fecha, id_usuario, id_consignacion) VALUES (?,NOW(),?,?)";
    private static final String SQL_SELECT_IDOBSERVACION = "SELECT MAX(idObservaciones) FROM observaciones ";
    private static final String SQL_SELECT_IDOBSERVACIONTEMPORAL = "SELECT MAX(idObservaciones) FROM observaciones_temporal";
    private static final String SQL_SELECT_OBSERVACIONES = "SELECT observaciones.idObservaciones, observaciones.observacion, observaciones.fecha, usuario.nombre FROM observaciones INNER JOIN usuario ON observaciones.id_usuario = usuario.idUsuario WHERE id_consignacion = ?  ORDER BY fecha DESC";
    private static final String SQL_INSERT_OBSERVACIONTEMPORAL = "INSERT INTO observaciones_temporal(observacion, fecha, id_usuario) VALUES (?,NOW(),?)";
    private static final String SQL_DELETE_OBSERVACIONTEMPORAL = "DELETE FROM observaciones_temporal";
    private static final String SQL_SELECT_OBSERVACIONTEMPORALBYID = "SELECT * FROM observaciones_temporal WHERE idObservaciones = ?";
    private static final String SQL_UPDATE_ENVIARIDCONSIGNACION = "UPDATE observaciones_temporal SET id_consignacion = ? WHERE idObservaciones = ?";

    public int guardarObservacion(Observaciones obs) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_OBSERVACIONES);
            stmt.setString(1, obs.getObservacion());

            stmt.setInt(2, obs.getId_usuario());
            stmt.setInt(3, obs.getId_consignacion());

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return obtenerIdObservacion();
    }

    public int obtenerIdObservacion() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDOBSERVACION);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idObservacion = rs.getInt("MAX(idObservaciones)");

                rown = idObservacion;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public List<Observaciones> obtenerObservaciones(int idConsignacion) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Observaciones observaciones = null;

        List<Observaciones> obs = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_OBSERVACIONES);
            stmt.setInt(1, idConsignacion);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idObservacion = rs.getInt("idObservaciones");
                String observacion = rs.getString("observacion");
                String fecha = rs.getString("fecha");

                String nombre_usuario = rs.getString("nombre");

                observaciones = new Observaciones(idObservacion, observacion, fecha, nombre_usuario);
                obs.add(observaciones);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return obs;
    }

    public int observacionTemporal(String observacion, int id_usuario) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_OBSERVACIONTEMPORAL);
            stmt.setString(1, observacion);

            stmt.setInt(2,  id_usuario);
            

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        int idObservacion = obtenerIdObservacionTemporal();
        return idObservacion;
    }

    public int obtenerIdObservacionTemporal() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDOBSERVACIONTEMPORAL);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idObservacion = rs.getInt("MAX(idObservaciones)");

                rown = idObservacion;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int eliminarObserTemp() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_DELETE_OBSERVACIONTEMPORAL);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public Observaciones obtenerObservacionTemporalById(int id_observacion) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Observaciones observaciones = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_OBSERVACIONTEMPORALBYID);
            stmt.setInt(1, id_observacion);

            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int idObservacion = rs.getInt("idObservaciones");
                    String observacion = rs.getString("observacion");
                    String fecha = rs.getString("fecha");
                    DateTime fechaHora = Funciones.FuncionesGenerales.stringToDateTime(fecha);
                    int id_usuario = rs.getInt("id_usuario");
                    int id_consignacion = rs.getInt("id_consignacion");

                    observaciones = new Observaciones();
                    observaciones.setIdObservaciones(idObservacion);
                    observaciones.setObservacion(observacion);
                    observaciones.setFecha(fechaHora);
                    observaciones.setId_usuario(id_usuario);
                    observaciones.setId_consignacion(id_consignacion);

                }
            }else{
                String error = "null";
                observaciones = new Observaciones();
                observaciones.setError(error);
            }

            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return observaciones;
    }
    
     public int enviarIdConsignacion(int id_consignacion, int id_observacion) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_ENVIARIDCONSIGNACION);
            stmt.setInt(1, id_consignacion);

            stmt.setInt(2, id_observacion);
            

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
