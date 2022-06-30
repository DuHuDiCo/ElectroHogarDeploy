
package Datos;

import Dominio.Consignacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoConsignaciones2 {
    
    private static final String SQL_SELECT_CONSIGNACIONESAPLICADASBYIDUSUARIO = "SELECT consignacion.idConsignacion FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado  WHERE estado.nombre_estado = 'Aplicado' AND actualizacion.id_usuarios = ? ";
    private static final String SQL_SELECT_CONSIGNACIONESDIACOMPROBADAS = "SELECT consignacion.idConsignacion FROM consignacion INNER JOIN actualizacion ON consignacion.id_actualizacion = actualizacion.idActualizacion INNER JOIN estado ON actualizacion.id_estado = estado.idEstado INNER JOIN plataforma ON consignacion.id_plataforma = plataforma.idPlataforma INNER JOIN obligacion ON consignacion.id_obligacion = obligacion.idObligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede WHERE estado.nombre_estado = ? AND consignacion.fecha_creacion = ? ORDER BY consignacion.fecha_creacion DESC ";
    private static final String SQL_UPDATE_CONSIGNACIONCARTERA = "UPDATE consignacion SET num_recibo = ?, fecha_pago = ?, valor = ?, id_plataforma = ?, id_obligacion = ? WHERE idConsignacion = ?";
    
    
    
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
    
    public int actualizarConsignacion(Consignacion cons) throws ClassNotFoundException{
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
}
