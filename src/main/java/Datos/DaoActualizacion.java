package Datos;

import Dominio.Actualizacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoActualizacion {

    private static final String SQL_INSERT_ACTUALIZACION = "INSERT INTO actualizacion(fecha_actualizacion, id_estado, id_usuarios) VALUES (NOW(),?,?)";
    private static final String SQL_SELEC_IDACTUALIZACION = "SELECT MAX(idActualizacion) FROM actualizacion ";

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

        if (rown == 1) {
            return obtenerIdActualizacion();
        }else{
            return rown;
        }

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
                int idActualizacion = rs.getInt("MAX(idActualizacion)");

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

}
