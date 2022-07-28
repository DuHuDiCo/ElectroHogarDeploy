
package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;


public class Conexion {
     private static final String JDBC_URL = "jdbc:mysql://103.23.61.139:3306/electro_hogar?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String JDBC_USER = "admin";
    private static final String JDBC_PASSWORD = "admin";
    private static Connection cnx = null;
    
    private static BasicDataSource ds;
    
    public static DataSource getDataSource(){
        if(ds == null){
            ds = new BasicDataSource();
            ds.setUrl(JDBC_URL);
            ds.setUsername(JDBC_USER);
            ds.setPassword(JDBC_PASSWORD);
            ds.setInitialSize(50);//para tener 50 conecciones de manera inicial
        }
        return ds;
    }
    
    
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(cnx == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cnx = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException ex){
                throw new ClassNotFoundException(ex.getMessage());
            }
        }else{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cnx = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException ex){
                throw new ClassNotFoundException(ex.getMessage());
            }
        }
        
        return cnx;
    }
    
    public static void close(Connection con){
        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    
    public static void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    
    public static void close(PreparedStatement stmt){
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
