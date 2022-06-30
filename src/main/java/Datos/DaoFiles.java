
package Datos;

import Dominio.Archivo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoFiles {
    
    
    private static final String SQL_INSERT_ARCHIVOTXT = "INSERT INTO filestxt(nombre_archivo, ruta, fecha, id_usuario) VALUES (?,?,?,?)";
    private static final String SQL_SELECT_OBTENERIDTXT = "SELECT idFile FROM filestxt WHERE nombre_archivo = ?";
    private static final String SQL_SELECT_LISTARFILES = "SELECT filestxt.idFile, filestxt.nombre_archivo, filestxt.fecha, usuario.idUsuario, usuario.nombre FROM filestxt INNER JOIN usuario ON filestxt.id_usuario = usuario.idUsuario";
    private static final String SQL_INSERT_ARCHIVOREPORTES = "INSERT INTO filesreportes(nombre_archivo, ruta, fecha, id_usuario) VALUES (?,?,NOW(),?)";
    
    
    
    
    public int guardarArchivoTxt(Archivo file) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_ARCHIVOTXT);
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
    
    
    public int obtenerIdFileTxt(String name) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_OBTENERIDTXT);
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            
             while (rs.next()) {
                int idFileTxt = rs.getInt("idFile");
                
                rown = idFileTxt;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    
    public List<Archivo> listarFiles() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

        List<Archivo> files = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_LISTARFILES);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("idFile");
                String nombre_archivo = rs.getString("nombre_archivo");
                Date fecha = rs.getDate("fecha");
                int idUsuario = rs.getInt("idUsuario");
                String nombre_usuario = rs.getString("nombre");
                

                file = new Archivo(idFile, nombre_archivo, fecha, idUsuario, nombre_usuario);
                files.add(file);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return files;
    }
    
    
    public int guardarArchivoReportes(Archivo file) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_ARCHIVOREPORTES);
            stmt.setString(1, file.getNombre_archivo());
            stmt.setString(2, file.getRuta());
            stmt.setInt(3, file.getId_usuario());
            

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
