package Datos;

import Dominio.Archivo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DaoFiles {

    private static final String SQL_INSERT_ARCHIVOTXT = "INSERT INTO filestxt(nombre_archivo, ruta, fecha, id_usuario) VALUES (?,?,?,?)";
    private static final String SQL_SELECT_OBTENERIDTXT = "SELECT idFile FROM filestxt WHERE nombre_archivo = ?";
    private static final String SQL_SELECT_LISTARFILES = "SELECT filestxt.idFile, filestxt.nombre_archivo, filestxt.fecha, usuario.idUsuario, usuario.nombre FROM filestxt INNER JOIN usuario ON filestxt.id_usuario = usuario.idUsuario";
    private static final String SQL_INSERT_ARCHIVOREPORTES = "INSERT INTO filesreportes(nombre_archivo, ruta, fecha, id_usuario) VALUES (?,?,NOW(),?)";
    private static final String SQL_SELECT_IDFILEIMAGEN = "SELECT id_files FROM consignacion WHERE idConsignacion = ?";
    private static final String SQL_SELECT_NOMBREFILE = "SELECT nombre FROM files WHERE idFile = ?";
    private static final String SQL_SELECT_NOMBREREPORTE = "SELECT nombre_archivo FROM filesreportes WHERE id_reporte = ?";
    private static final String SQL_SELECT_RUTAIMAGEN = "SELECT ruta FROM files WHERE idFile = ?";
    private static final String SQL_SELECT_REPORTESBYIDUSUARIO = "SELECT filesreportes.id_reporte, filesreportes.nombre_archivo, filesreportes.fecha, usuario.nombre FROM filesreportes INNER JOIN usuario ON filesreportes.id_usuario = usuario.idUsuario WHERE filesreportes.id_usuario = ?";
    private static final String SQL_SELECT_REPORTESBYFECHA = "SELECT filesreportes.id_reporte, filesreportes.nombre_archivo, filesreportes.fecha, usuario.nombre FROM filesreportes INNER JOIN usuario ON filesreportes.id_usuario = usuario.idUsuario WHERE filesreportes.id_usuario = ? AND fecha BETWEEN ? AND ?";
    private static final String SQL_SELECT_REPORTESADMINBYFECHA = "SELECT filesreportes.id_reporte, filesreportes.nombre_archivo, filesreportes.fecha, usuario.nombre FROM filesreportes INNER JOIN usuario ON filesreportes.id_usuario = usuario.idUsuario WHERE fecha BETWEEN ? AND ?";
    private static final String SQL_SELECT_REPORTESADMIN = "SELECT filesreportes.id_reporte, filesreportes.nombre_archivo, filesreportes.fecha, usuario.nombre FROM filesreportes INNER JOIN usuario ON filesreportes.id_usuario = usuario.idUsuario ";
    private static final String SQL_SELECT_NOMBREYUSUARIOREPORTE = "SELECT filesreportes.id_reporte, filesreportes.nombre_archivo, filesreportes.id_usuario, usuario.nombre, usuario.email FROM filesreportes INNER JOIN usuario ON filesreportes.id_usuario = usuario.idUsuario WHERE id_reporte = ?";

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

    public int obtenerIdFileImg(int idCon) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int rown = 0;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_IDFILEIMAGEN);
            stmt.setInt(1, idCon);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFileIma = rs.getInt("id_files");

                rown = idFileIma;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return rown;
    }

    public String obtenerNombreFile(int idFile) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String nombreFile = null;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBREFILE);
            stmt.setInt(1, idFile);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");

                nombreFile = nombre;

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return nombreFile;
    }

    public String obtenerRutaImagen(int idFile) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String path = null;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_RUTAIMAGEN);
            stmt.setInt(1, idFile);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String ruta = rs.getString("ruta");

                path = ruta;

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return path;
    }

    public List<Archivo> listarFilesByIdUsuario(int id_usuario) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

        List<Archivo> files = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_REPORTESBYIDUSUARIO);
            stmt.setInt(1, id_usuario);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("id_reporte");
                String nombre_archivo = rs.getString("nombre_archivo");
                Date fecha = rs.getDate("fecha");

                String nombre_usuario = rs.getString("nombre");

                file = new Archivo(idFile, nombre_archivo, fecha, id_usuario, nombre_usuario);
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

    public List<Archivo> listarFilesByFecha(int id_usuario, String fecha1, String fecha2) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

        List<Archivo> files = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_REPORTESBYFECHA);
            stmt.setInt(1, id_usuario);
            stmt.setString(2, fecha1);
            stmt.setString(3, fecha2);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("id_reporte");
                String nombre_archivo = rs.getString("nombre_archivo");
                Date fecha = rs.getDate("fecha");
                System.out.println(fecha);

                String nombre_usuario = rs.getString("nombre");

                file = new Archivo(idFile, nombre_archivo, fecha, id_usuario, nombre_usuario);
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
    
     public List<Archivo> listarFilesAdminByFecha(int id_usuario, String fecha1, String fecha2) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

        List<Archivo> files = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_REPORTESADMINBYFECHA);
            
            stmt.setString(1, fecha1);
            stmt.setString(2, fecha2);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("id_reporte");
                String nombre_archivo = rs.getString("nombre_archivo");
                Date fecha = rs.getDate("fecha");
                System.out.println(fecha);

                String nombre_usuario = rs.getString("nombre");

                file = new Archivo(idFile, nombre_archivo, fecha, id_usuario, nombre_usuario);
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

    public String obtenerNombreReporte(int idFile) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String path = null;
        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBREREPORTE);
            stmt.setInt(1, idFile);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre_archivo");

                path = nombre;

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);

        }
        return path;
    }

    public List<Archivo> listarReportesAdmin() throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

        List<Archivo> files = new ArrayList<>();

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_REPORTESADMIN);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("id_reporte");
                String nombre_archivo = rs.getString("nombre_archivo");
                Date fecha = rs.getDate("fecha");
                int id_usuario = 0;

                String nombre_usuario = rs.getString("nombre");

                file = new Archivo(idFile, nombre_archivo, fecha, id_usuario, nombre_usuario);
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

    public Archivo obtenerNombreUsuarioArchivo(int id_file) throws ClassNotFoundException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Archivo file = null;

      

        try {
            con = Conexion.getConnection();
            stmt = con.prepareStatement(SQL_SELECT_NOMBREYUSUARIOREPORTE);
            stmt.setInt(1, id_file);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idFile = rs.getInt("id_reporte");
                String nombre_archivo = rs.getString("nombre_archivo");
                int id_usuario = rs.getInt("id_usuario");
                String nombre_usuario = rs.getString("nombre");
                String email = rs.getString("email");

                file = new Archivo();
                file.setIdFile(idFile);
                file.setNombre_archivo(nombre_archivo);
                file.setIdUsuario(id_usuario);
                file.setNombre(nombre_usuario);
                file.setEmail(email);
               

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(con);
            Conexion.close(stmt);
            Conexion.close(rs);
        }

        return file;
    }

}
