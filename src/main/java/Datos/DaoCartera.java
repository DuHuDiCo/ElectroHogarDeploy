package Datos;

import Dominio.Actualizacion;
import Dominio.Archivo;
import Dominio.Consignacion;
import Dominio.Plataforma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCartera {

    private static final String SQL_SELEC_ESTADO = "SELECT idEstado FROM estado WHERE nombre_estado = ?";
    private static final String SQL_INSERT_ARCHIVO = "INSERT INTO files(nombre, ruta, fecha, id_usuario) VALUES (?,?,?,?)";
    
    private static final String SQL_SELEC_IDARCHIVO = "SELECT idFile FROM files WHERE nombre = ?";
    private static final String SQL_SELEC_IDUSUARIO = "SELECT idUsuario FROM usuario WHERE email = ?";
    private static final String SQL_INSERT_ACTUALIZACION = "INSERT INTO actualizacion(fecha_actualizacion, id_estado, id_usuarios) VALUES (NOW(),?,?)";
    private static final String SQL_SELEC_IDACTUALIZACION = "SELECT MAX(idActualizacion) FROM actualizacion ";
    private static final String SQL_INSERT_CONSIGNACION = "INSERT INTO consignacion(num_recibo, fecha_creacion, fecha_pago, valor, id_files, id_actualizacion, id_usuario, id_plataforma, id_obligacion)"
            + " VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String SQL_SELEC_BANCOS = "SELECT plataforma.idPlataforma, plataforma.nombre_plataforma, tipopago.tipo_pago FROM plataforma INNER JOIN tipopago ON plataforma.id_tipoPago = tipopago.idTipoPago";
    private static final String SQL_SELECT_IDCONSIGNACION = "SELECT  MAX(idConsignacion) FROM consignacion";
    
    public int obtenerIdEstado(String estado) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_ESTADO);
            stmt.setString(1, estado);

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

    public int guardarArchivo(Archivo file) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_ARCHIVO);
            stmt.setString(1, file.getNombre_archivo());
            stmt.setString(2, file.getRuta());
            stmt.setDate(3, file.getFecha());
            stmt.setInt(4, file.getId_usuario());
            

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    public int obtenerIdFile(String name) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_IDARCHIVO);
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idFile = rs.getInt("idFile");
                
                rown = idFile;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    public int obtenerIdUsuario(String email) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_IDUSUARIO);
            stmt.setString(1, email);

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                
                rown = idUsuario;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    
     public int guardarActualizacion(Actualizacion actu) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_ACTUALIZACION);
            
            stmt.setInt(1, actu.getId_estado());
            stmt.setInt(2, actu.getId_usuarios());
            

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return obtenerIdActualizacion();
    }
     
     public int obtenerIdActualizacion() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_IDACTUALIZACION);
            

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idConsignacion = rs.getInt("MAX(idActualizacion)");
                
                rown = idConsignacion;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
     
     
     public int guardarConsignacion(Consignacion cons) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_CONSIGNACION);
            stmt.setString(1, cons.getNum_recibo());
            stmt.setDate(2, cons.getFecha_creacion());
            stmt.setDate(3, cons.getFecha_pago());
            stmt.setFloat(4, cons.getValor());
            stmt.setInt(5, cons.getId_files());
            stmt.setInt(6, cons.getId_actualizacion());
            stmt.setInt(7, cons.getId_usuario());
            stmt.setInt(8, cons.getId_plataforma());
            stmt.setInt(9, cons.getId_obligacion());
            
            

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return obtenerIdConsignacion();
    }
     
    public int obtenerIdConsignacion() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDCONSIGNACION);
            

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idActualizacion = rs.getInt("MAX(idConsignacion)");
                
                rown = idActualizacion;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    } 
     
     public List<Plataforma> listarBanco() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Plataforma banco = null;

        List<Plataforma> bancos = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_BANCOS);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idPlataforma = rs.getInt("idPlataforma");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                String tipoPago = rs.getString("tipo_pago");
                

                banco = new Plataforma(idPlataforma, nombre_plataforma, tipoPago);
                bancos.add(banco);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return bancos;
    }
}
