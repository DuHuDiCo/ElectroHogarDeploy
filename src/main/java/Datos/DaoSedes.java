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
}
