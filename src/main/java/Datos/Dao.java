package Datos;

import Dominio.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.joda.time.DateTime;

public class Dao {

    private static final String SQL_SELECT_INICIARSESION = "SELECT usuario.email, usuario.password, rol.nombre_rol FROM usuario INNER JOIN rol ON usuario.id_rol = rol.idRol WHERE usuario.email = ?";
    private static final String SQL_INSERT_USUARIO = "INSERT INTO usuario(nombre, tipo_documento, n_documento, email, password, telefono, fecha_creacion, status, id_rol, id_sede) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_USUARIO =  "UPDATE usuario SET estado_conexion = ?, ultima_sesion = NOW() WHERE email = ?";
    private static final String SQL_SELECT_NOMBREUSUARIO = "SELECT nombre FROM usuario WHERE email = ?";
    

    public Usuario iniciarSesion(String email) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_INICIARSESION);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            boolean valid = rs.next();
            if (valid) {
                String correo = rs.getString("email");
                String password = rs.getString("password");
                String nombreRol = rs.getString("nombre_rol");

                user = new Usuario(correo, password, nombreRol);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return user;
    }

    public int crearUsuario(Usuario user) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        Usuario usr = null;
        int row = 0;
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_USUARIO);
            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getTipo_documento());
            stmt.setString(3, user.getN_documento());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getTelefonoUser());
            stmt.setDate(7, user.getFecha_creacion());
            stmt.setInt(8, user.getStatus());
            stmt.setInt(9, user.getId_rol());
            stmt.setInt(10, user.getId_sede());
            

            row = stmt.executeUpdate();

            
           

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            
        }
        
        return row;
    }
    
    public int datosConexion(String conex,  String email) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
       
        int row = 0;
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_USUARIO);
            stmt.setString(1, conex);
            
            stmt.setString(2, email);
            
            

            row = stmt.executeUpdate();

            
           

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            
        }
        
        return row;
    }
    
    
    public String obtenerNombreUsuario(String email) throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String response = null;
        
         try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBREUSUARIO);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            boolean valid = rs.next();
            if (valid) {
                String nombre = rs.getString("nombre");
                response = nombre;

               
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return response;
    }

}
