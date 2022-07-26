
package Dominio;

import java.sql.Date;
import org.joda.time.DateTime;


public class Archivo extends Usuario{
    
    private int idFile;
    private String nombre_archivo;
    private String ruta;
    private Date fecha;
    private DateTime fechaHora;
    private String fecha_hora;
    private int id_usuario;

    public Archivo() {
        
    }

    public Archivo(int idFile, String nombre_archivo, Date fecha, int idUsuario, String nombre) {
        super(idUsuario, nombre);
        this.idFile = idFile;
        this.nombre_archivo = nombre_archivo;
        this.fecha = fecha;
    }
    
    public Archivo(int idFile, String nombre_archivo, String fecha, int idUsuario, String nombre) {
        super(idUsuario, nombre);
        this.idFile = idFile;
        this.nombre_archivo = nombre_archivo;
        this.fecha_hora = fecha;
    }

    public Archivo(String nombre_archivo, String ruta, DateTime fechaHora, int id_usuario) {
        this.nombre_archivo = nombre_archivo;
        this.ruta = ruta;
        this.fechaHora = fechaHora;
        this.id_usuario = id_usuario;
    }

    

  

    
    
    
    
    public Archivo(String nombre, String ruta, Date fecha, int id_usuario) {
        this.nombre_archivo = nombre;
        this.ruta = ruta;
        this.fecha = fecha;
        this.id_usuario = id_usuario;
    }
    
    

    public Archivo(int idFile, String nombre, String ruta) {
        this.idFile = idFile;
        this.nombre_archivo = nombre;
        this.ruta = ruta;
    }

    public Archivo(String nombre, String ruta) {
        this.nombre_archivo = nombre;
        this.ruta = ruta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
    
    

    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }

    public DateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(DateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    
    
    
}
