package Datos;

import Dominio.Sedes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoSedes {

    private static final String SQL_SELECT_SEDES = "SELECT * FROM sede";
    private static final String SQL_INSERT_SEDES = "INSERT INTO sede(nombre_sede, municipio, telefono, dato_personalizado) VALUES (?,?,?,?)";
    private static final String SQL_SELECT_SEDEBYID = "SELECT * FROM sede WHERE idSede = ?";
    private static  String SQL_UPDATE_SEDE = "";
    private static final String SQL_DELETE_SEDE = "DELETE FROM sede WHERE idSede = ?";
    private static final String SQL_SELECT_NOMBRESEDE = "SELECT sede.nombre_sede FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede WHERE idUsuario = ?";
    

    public List<Sedes> listarSedes() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Sedes Sede;

        List<Sedes> Sedes = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_SEDES);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idSede = rs.getInt("idSede");
                String nombreSede = rs.getString("nombre_sede");
                String muni = rs.getString("municipio");
                String tel = rs.getString("telefono");
                String datper = rs.getString("dato_personalizado");

                Sede = new Sedes(idSede, nombreSede, muni, tel, datper);
                Sedes.add(Sede);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return Sedes;
    }

    public int guardarSedes(Sedes sede) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
       
        int row = 0;
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_SEDES);
            stmt.setString(1, sede.getNombre_sede());
            stmt.setString(2, sede.getMunicipio());
            stmt.setString(3, sede.getTelefono());
            stmt.setString(4, sede.getDatper());
            
            

            row = stmt.executeUpdate();

            
           

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            
        }
        
        return row;
    }
    
    public Sedes obtenerSedeById(int id_sede) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Sedes sede = null;

        

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_SEDEBYID);
            stmt.setInt(1, id_sede);
            
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idSede = rs.getInt("idSede");
                String nombreSede = rs.getString("nombre_sede");
                String muni = rs.getString("municipio");
                String tel = rs.getString("telefono");
                String datper = rs.getString("dato_personalizado");

                sede = new Sedes(idSede, nombreSede, muni, tel, datper);
               
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return sede;
    }
    
    public int actualizarSede(Sedes sede) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
       
        int row = 0;
        
        if(sede.getDatper() == null){
            SQL_UPDATE_SEDE = "UPDATE sede SET nombre_sede = ?, municipio = ?, telefono = ? WHERE idSede = ?";
        }else{
            SQL_UPDATE_SEDE = "UPDATE sede SET nombre_sede = ?, municipio = ?, telefono = ?, dato_personalizado = ? WHERE idSede = ?";
        }
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_SEDE);
            stmt.setString(1, sede.getNombre_sede());
            stmt.setString(2, sede.getMunicipio());
            stmt.setString(3, sede.getTelefono());
            if(sede.getDatper() == null){
                stmt.setInt(4, sede.getSede());
            }else{
                stmt.setString(4, sede.getDatper());
                stmt.setInt(5, sede.getSede());
            }
            
            
            

            row = stmt.executeUpdate();

            
           

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            
        }
        
        return row;
    }
    
    public int eliminarSede(int sede) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
       
        int row = 0;
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_DELETE_SEDE);
            stmt.setInt(1, sede);
            
            
            

            row = stmt.executeUpdate();

            
           

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            
        }
        
        return row;
    }
    
    public String obtenerSedeByIdUsuario(int id_usuario) throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sede = null;
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBRESEDE);
            stmt.setInt(1, id_usuario);
            
            rs = stmt.executeQuery();

            while (rs.next()) {
               
                String nombreSede = rs.getString("nombre_sede");
                
                sede = nombreSede;
               
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return sede;
    }
}
