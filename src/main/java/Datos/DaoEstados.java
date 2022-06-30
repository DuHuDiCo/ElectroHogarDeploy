
package Datos;

import Dominio.Estados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoEstados {
    
    
    private static final String SQL_SELEC_ESTADOS =  "SELECT * FROM estado";
    private static final String SQL_SELECT_IDESTADO = "SELECT idEstado FROM estado WHERE nombre_estado = ?";
    
    
    
     public List<Estados> listarEstados() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Estados estado = null;

        List<Estados> stated = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_ESTADOS);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idEstado = rs.getInt("idEstado");
                String nombre_estado = rs.getString("nombre_estado");
                

                estado = new Estados(idEstado, nombre_estado);
                stated.add(estado);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return stated;
    }
     
     
     public int obtenerIdEstado(String nombreEstado) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDESTADO);
            stmt.setString(1, nombreEstado);

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idEstado = rs.getInt("idEstado");
                
                rown = idEstado;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
}
