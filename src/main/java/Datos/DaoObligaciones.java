
package Datos;

import Dominio.Obligaciones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoObligaciones {
    
    private static final String SQL_INSERT_OBLIGACIONES = "INSERT INTO obligacion(nombre_titular, tipo_documento, n_documento, telefono, email, direccion, clasificacion_cliente, codigo_cliente, valor_cuota, saldo_capital, fecha_obligacion, saldo_mora, dias_mora, id_sede, id_filesTxt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_OBBLIGACIONPORCLIENTE = "INSERT INTO obligacion(nombre_titular, n_documento ,id_sede) VALUES (?,?,?)";
    private static final String SQL_SELECT_IDOBLIGACIONCREAD = "SELECT idObligacion FROM obligacion WHERE n_documento = ?";
    private static final String SQL_SELECT_OBLIGACIONES = "SELECT obligacion.idObligacion, obligacion.nombre_titular, obligacion.n_documento, obligacion.telefono, obligacion.email, obligacion.direccion, obligacion.codigo_cliente, "
            +" obligacion.valor_cuota, obligacion.saldo_capital, obligacion.fecha_obligacion, obligacion.saldo_mora, obligacion.dias_mora, sede.nombre_sede FROM obligacion INNER JOIN sede ON obligacion.id_sede = sede.idSede";
    
    public int guardarObligaciones (Obligaciones obliga) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_OBLIGACIONES);
            stmt.setString(1, obliga.getNombre_titular());
            stmt.setString(2, obliga.getTipo_documento());
            stmt.setString(3, obliga.getN_documento());
            stmt.setString(4, obliga.getTelefono_titular());
            stmt.setString(5, obliga.getEmail());
            stmt.setString(6, obliga.getDireccion());
            stmt.setString(7, obliga.getClasificacion_cliente());
            stmt.setString(8, obliga.getCodigo_cliente());
            stmt.setFloat(9, obliga.getValor_cuota());
            stmt.setFloat(10, obliga.getSaldo_capital());
            stmt.setDate(11, obliga.getFecha_obligacion());
            stmt.setFloat(12, obliga.getSaldo_mora());
            stmt.setInt(13, obliga.getDias_mora());
            stmt.setInt(14, obliga.getId_sede());
            stmt.setInt(15, obliga.getId_fileTxt());           
            
            
            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    public int guardarObligacionPorCliente(String cliente, int id_sede, String cedula) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_OBBLIGACIONPORCLIENTE);
            stmt.setString(1, cliente);
            stmt.setString(2, cedula);
            stmt.setInt(3, id_sede);
               
            
            
            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    public int obtenerIdObligacionCreada(String cedula) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDOBLIGACIONCREAD);
            stmt.setString(1, cedula);

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idObligacion = rs.getInt("idObligacion");
                
                rown = idObligacion;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
     public List<Obligaciones> listarObligaciones() throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Obligaciones obliga = null;

        List<Obligaciones> obligaciones = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_OBLIGACIONES);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idConsignacion = rs.getInt("idObligacion");
                String nombre_titular = rs.getString("nombre_titular");
                String n_documento = rs.getString("n_documento");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                String direccion = rs.getString("direccion");
                String codigo_cliente = rs.getString("codigo_cliente");
                float valor_cuota = rs.getFloat("valor_cuota");
                float saldo_capital = rs.getFloat("saldo_capital");
                Date fecha_obligacion = rs.getDate("fecha_obligacion");
                float saldo_mora = rs.getFloat("saldo_mora");
                int dias_mora = rs.getInt("dias_mora");
                String nombre_sede = rs.getString("nombre_sede");
                
                

                obliga = new Obligaciones(idConsignacion, nombre_titular, n_documento, telefono, email, direccion, codigo_cliente, valor_cuota, saldo_capital, fecha_obligacion, saldo_mora, dias_mora, nombre_sede);
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
}
