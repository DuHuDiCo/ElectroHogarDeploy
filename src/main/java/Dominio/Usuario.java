
package Dominio;

import java.sql.Date;
import org.joda.time.DateTime;




public class Usuario extends Sedes{

   
    
    private int idUsuario;
    private String nombre_usuario;
    private String tipo_documento;
    private String n_documento;
    private String email;
    private String password;
    private String telefonoUser;
    private Date fecha_creacion;
    private String estado_conexion;
    private String ultima_sesion;
    private int status;
    private int id_rol;
    private int id_sede;
    private String nombre_rol;
    private String error;

    public Usuario() {
    }
    
    //crear usuario

    public Usuario(String nombre_usuario, String tipo_documento, String n_documento, String email, String password, String telefono, Date fecha_creacion, int status, int id_rol, int id_sede) {
        this.nombre_usuario = nombre_usuario;
        this.tipo_documento = tipo_documento;
        this.n_documento = n_documento;
        this.email = email;
        this.password = password;
        this.telefonoUser = telefono;
        this.fecha_creacion = fecha_creacion;
        this.status = status;
        this.id_rol = id_rol;
        this.id_sede = id_sede;
    }

    public Usuario(int idUsuario, String nombre_usuario, String n_documento, String email, String telefono, String estado_conexion, String ultima_sesion, int status, int idSede, String nombre_sede) {
        super(idSede, nombre_sede);
        this.idUsuario = idUsuario;
        this.nombre_usuario = nombre_usuario;
        this.n_documento = n_documento;
        this.email = email;
        this.telefonoUser = telefono;
        this.estado_conexion = estado_conexion;
        this.ultima_sesion = ultima_sesion;
        this.status = status;
    }

//  
    
    
    
    
    
     public Usuario(int idUsuario, String nombre, String tipo_documento, String n_documento, String email, String password, String telefono, Date fecha_creacion, String estado_conexion, String ultima_sesion, int status, int id_rol, int id_sede, String nombre_rol) {
        this.idUsuario = idUsuario;
        this.nombre_usuario = nombre;
        this.tipo_documento = tipo_documento;
        this.n_documento = n_documento;
        this.email = email;
        this.password = password;
        this.telefonoUser = telefono;
        this.fecha_creacion = fecha_creacion;
        this.estado_conexion = estado_conexion;
        this.ultima_sesion = ultima_sesion;
        this.status = status;
        this.id_rol = id_rol;
        this.id_sede = id_sede;
    }

    public Usuario(int idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre_usuario = nombre;
    }
     
     

    public Usuario(String email, String password, String nombre_rol) {
        this.email = email;
        this.password = password;
        this.nombre_rol = nombre_rol;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre_usuario;
    }

    public void setNombre(String nombre) {
        this.nombre_usuario = nombre;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getN_documento() {
        return n_documento;
    }

    public void setN_documento(String n_documento) {
        this.n_documento = n_documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefonoUser() {
        return telefonoUser;
    }

    public void setTelefonoUser(String telefono) {
        this.telefonoUser = telefono;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getEstado_conexion() {
        return estado_conexion;
    }

    public void setEstado_conexion(String estado_conexion) {
        this.estado_conexion = estado_conexion;
    }

    public String getUltima_sesion() {
        return ultima_sesion;
    }

    public void setUltima_sesion(String ultima_sesion) {
        this.ultima_sesion = ultima_sesion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public int getId_sede() {
        return id_sede;
    }

    public void setId_sede(int id_sede) {
        this.id_sede = id_sede;
    }
 
}
