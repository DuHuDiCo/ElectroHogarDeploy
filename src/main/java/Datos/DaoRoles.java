/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Dominio.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class DaoRoles {

    private static final String SQL_SELECT_ROLES = "SELECT * FROM rol";
    private static final String SQL_SELECT_ROLUSUARIO = "SELECT rol.nombre_rol FROM usuario INNER JOIN rol ON usuario.id_rol = rol.idRol WHERE usuario.email = ?";

    public List<Rol> listarRoles() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Rol rol;

        List<Rol> roles = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_ROLES);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idRol = rs.getInt("idRol");
                String nombreRol = rs.getString("nombre_rol");

                rol = new Rol(idRol, nombreRol);
                roles.add(rol);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return roles;
    }

    public String obtenerRolUsuario(String email) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String nombreRol = "";
        
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_ROLUSUARIO);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String rol = rs.getString("nombre_rol");
                nombreRol = rol;
                
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return nombreRol;
    }

}
