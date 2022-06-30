
package Datos;

import Dominio.Estados;
import Dominio.Plataforma;
import Dominio.TipoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoAdmin {
    
    private static final String SQL_INSERT_ESTADOS = "INSERT INTO estado(nombre_estado) VALUES (?)";
    private static final String SQL_SELEC_ESTADOS =  "SELECT * FROM estado";
    private static final String SQL_INSERT_TIPOPAGO = "INSERT INTO tipoPago(tipo_pago) VALUES (?)";
    private static final String SQL_SELEC_TIPOPAGO =  "SELECT * FROM tipoPago";
    private static final String SQL_INSERT_BANCO = "INSERT INTO plataforma(nombre_plataforma, id_tipoPago) VALUES (?, ?)";
    private static final String SQL_SELEC_BANCO =  "SELECT plataforma.idPlataforma, plataforma.nombre_plataforma,  tipoPago.idTipoPago, tipoPago.tipo_pago "
            + " FROM plataforma INNER JOIN tipoPago ON plataforma.id_tipoPago = tipoPago.idTipoPago";
    
    
    
    
    public int crearEstados (String estado) throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_ESTADOS);
            stmt.setString(1, estado);
            
            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    
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
    
    
    public int crearTipoPago(String tipo_pago) throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_TIPOPAGO);
            stmt.setString(1, tipo_pago);
            
            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    public List<TipoPago> listarTipoPago() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TipoPago pago = null;

        List<TipoPago> stated = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_TIPOPAGO);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idTipoPago = rs.getInt("idTipoPago");
                String tipo_pago = rs.getString("tipo_pago");
                

                pago = new TipoPago(idTipoPago, tipo_pago);
                stated.add(pago);
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
    
    
    public int crearBanco (String banco, int tipo_pago) throws ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_INSERT_BANCO);
            stmt.setString(1, banco);
            stmt.setInt(2, tipo_pago);
            
            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }
    
    
    
     public List<Plataforma> listarBancos() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Plataforma banco = null;

        List<Plataforma> bancos = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELEC_BANCO);
            

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idBanco = rs.getInt("idPlataforma");
                String nombre_plataforma = rs.getString("nombre_plataforma");
                int idTipoPago = rs.getInt("idTipoPago");
                String tipo_pago = rs.getString("tipo_pago");
                

                banco = new Plataforma(idBanco, nombre_plataforma, idTipoPago, tipo_pago);
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
