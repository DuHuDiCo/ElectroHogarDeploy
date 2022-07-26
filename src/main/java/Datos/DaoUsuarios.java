package Datos;

import Dominio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoUsuarios {

    private static final String SQL_SELECT_USUARIOS = "SELECT usuario.idUsuario, usuario.nombre, usuario.email, usuario.n_documento, usuario.telefono, usuario.estado_conexion, usuario.status, usuario.ultima_sesion, sede.nombre_sede, rol.nombre_rol FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede INNER JOIN rol ON usuario.id_rol = rol.idRol";
    private static final String SQL_SELEC_IDUSUARIO = "SELECT idUsuario FROM usuario WHERE email = ?";
    private static final String SQL_SELECT_SEDEUSUARIO = "SELECT sede.nombre_sede FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede WHERE usuario.email = ?";
    private static final String SQL_SELECT_DATOSPERFIL = "SELECT nombre, n_documento, email, telefono FROM usuario WHERE idUsuario = ?";
    private static final String SQL_UPDATE_ACTUALIZARPERFIL = "UPDATE usuario SET nombre = ?, n_documento = ?, email = ?, telefono = ? WHERE idUsuario = ?";
    private static final String SQL_UPDATE_ACTUALIZARPERFILCOMPLETO = "UPDATE usuario SET nombre = ?, n_documento = ? ,email = ?, telefono = ?, password = ? WHERE idUsuario = ?";
    private static final String SQL_UPDATE_DESACTIVARUSUARIO = "UPDATE usuario SET status = 0 WHERE idUsuario = ?";
    private static final String SQL_UPDATE_ACTIVARUSUARIO = "UPDATE usuario SET status = 1 WHERE idUsuario = ?";
    private static final String SQL_SELECT_USUARIOBYID = "SELECT usuario.idUsuario, usuario.nombre, usuario.email, usuario.n_documento, usuario.telefono, sede.idSede, sede.nombre_sede, rol.idRol, rol.nombre_rol FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede INNER JOIN rol ON usuario.id_rol = rol.idRol WHERE idUsuario = ?";
    private static final String SQL_UPDATE_USUARIO = "UPDATE usuario SET nombre = ?, n_documento = ?, email = ?, telefono = ?, id_rol = ?, id_sede = ? WHERE idUsuario = ?";
    private static final String SQL_SELECT_USUARIOBYCEDULA = "SELECT usuario.idUsuario, usuario.nombre, usuario.email, usuario.n_documento, usuario.telefono, usuario.estado_conexion, usuario.status, usuario.ultima_sesion, sede.nombre_sede, rol.nombre_rol FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede INNER JOIN rol ON usuario.id_rol = rol.idRol WHERE n_documento = ?";
    private static final String SQL_SELECT_USUARIOBYNOMBRE = "SELECT usuario.idUsuario, usuario.nombre, usuario.email, usuario.n_documento, usuario.telefono, usuario.estado_conexion, usuario.status, usuario.ultima_sesion, sede.nombre_sede, rol.nombre_rol FROM usuario INNER JOIN sede ON usuario.id_sede = sede.idSede INNER JOIN rol ON usuario.id_rol = rol.idRol WHERE nombre = ?";

    public List<Usuario> listarUsuarios() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user;

        List<Usuario> usuarios = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_USUARIOS);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                String nombreUsuario = rs.getString("nombre");
                String email = rs.getString("email");
                String n_documento = rs.getString("n_documento");
                String telefono = rs.getString("telefono");
                String estado_conexion = rs.getString("estado_conexion");
                int status = rs.getInt("status");
                String ultima_sesion = rs.getString("ultima_sesion");
                String sede = rs.getString("nombre_sede");
                String rol = rs.getString("nombre_rol");

                user = new Usuario(idUsuario, nombreUsuario, n_documento, email, telefono, estado_conexion, ultima_sesion, status, status, sede);
                user.setNombre_rol(rol);
                usuarios.add(user);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }
        return usuarios;
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

    public String obtenerSedeUsuario(String email) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sede = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_SEDEUSUARIO);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String se = rs.getString("nombre_sede");
                sede = se;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return sede;
    }

    public Usuario datosPerfil(int id) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_DATOSPERFIL);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String cedula = rs.getString("n_documento");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");

                user = new Usuario();
                user.setNombre(nombre);
                user.setN_documento(cedula);
                user.setEmail(email);
                user.setTelefonoUser(telefono);

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

    public int actualizarPerfil(Usuario user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_ACTUALIZARPERFIL);
            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getN_documento());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getTelefono());
            stmt.setInt(5, user.getIdUsuario());

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int actualizarPerfilCompleto(Usuario user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_ACTUALIZARPERFILCOMPLETO);
            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getN_documento());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getTelefono());
            stmt.setString(5, user.getPassword());
            stmt.setInt(6, user.getIdUsuario());

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int desactivarUsuario(int user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_DESACTIVARUSUARIO);

            stmt.setInt(1, user);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public int activarUsuario(int user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_ACTIVARUSUARIO);

            stmt.setInt(1, user);

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public Usuario obtenerUsuarioById(int id) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_USUARIOBYID);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String cedula = rs.getString("n_documento");
                String telefono = rs.getString("telefono");
                int idSede = rs.getInt("idSede");
                String nombreSede = rs.getString("nombre_sede");
                int idRol = rs.getInt("idRol");
                String nombreRol = rs.getString("nombre_rol");

                user = new Usuario();
                user.setIdUsuario(idUsuario);
                user.setNombre(nombre);
                user.setN_documento(cedula);
                user.setEmail(email);
                user.setTelefonoUser(telefono);
                user.setId_sede(idSede);
                user.setNombre_sede(nombreSede);
                user.setId_rol(idRol);
                user.setNombre_rol(nombreRol);

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

    public int actualizarUsuario(Usuario user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_UPDATE_USUARIO);

            stmt.setString(1, user.getNombre());
            stmt.setString(3, user.getEmail());
            stmt.setString(2, user.getN_documento());
            stmt.setString(4, user.getTelefonoUser());
            stmt.setInt(6, user.getId_sede());
            stmt.setInt(5, user.getId_rol());
            stmt.setInt(7, user.getIdUsuario());

            rown = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public Usuario obtenerUsuarioByCedula(String dato) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_USUARIOBYCEDULA);
            stmt.setString(1, dato);

            rs = stmt.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int idUsuario = rs.getInt("idUsuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String cedula = rs.getString("n_documento");
                    String telefono = rs.getString("telefono");
                    String estado_conexion = rs.getString("estado_conexion");
                    int estatus = rs.getInt("status");
                    String sesion = rs.getString("ultima_sesion");
                    String nombreSede = rs.getString("nombre_sede");
                    String nombreRol = rs.getString("nombre_rol");
                    String error = "Usuario Encontrado";

                    user = new Usuario();
                    user.setIdUsuario(idUsuario);
                    user.setNombre(nombre);
                    user.setN_documento(cedula);
                    user.setEmail(email);
                    user.setTelefonoUser(telefono);
                    user.setEstado_conexion(estado_conexion);
                    user.setStatus(estatus);
                    user.setUltima_sesion(sesion);
                    user.setNombre_sede(nombreSede);
                    user.setNombre_rol(nombreRol);
                    user.setError(error);

                }
            } else {
                String error = "null";
                user = new Usuario();
                user.setError(error);
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

    public Usuario obtenerUsuarioByNombre(String dato) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_USUARIOBYNOMBRE);
            stmt.setString(1, dato);

            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int idUsuario = rs.getInt("idUsuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String cedula = rs.getString("n_documento");
                    String telefono = rs.getString("telefono");
                    String estado_conexion = rs.getString("estado_conexion");
                    int estatus = rs.getInt("status");
                    String sesion = rs.getString("ultima_sesion");
                    String nombreSede = rs.getString("nombre_sede");
                    String nombreRol = rs.getString("nombre_rol");
                    String error = "Usuario Encontrado";

                    user = new Usuario();
                    user.setIdUsuario(idUsuario);
                    user.setNombre(nombre);
                    user.setN_documento(cedula);
                    user.setEmail(email);
                    user.setTelefonoUser(telefono);
                    user.setEstado_conexion(estado_conexion);
                    user.setStatus(estatus);
                    user.setUltima_sesion(sesion);
                    user.setNombre_sede(nombreSede);
                    user.setNombre_rol(nombreRol);
                    user.setError(error);

                }
            } else {
                String error = "null";
                user = new Usuario();
                user.setError(error);
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
}
