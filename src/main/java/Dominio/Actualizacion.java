
package Dominio;

import java.sql.Date;
import org.joda.time.DateTime;


public class Actualizacion {
    
    private int idActualizacion;
    private Date fecha_actualizacion;
    private DateTime fechaActualizacion;
    private int id_estado;
    private int id_usuarios;

    public Actualizacion() {
    }

    public Actualizacion(Date fecha_actualizacion, int id_estado, int id_usuarios) {
        this.fecha_actualizacion = fecha_actualizacion;
        this.id_estado = id_estado;
        this.id_usuarios = id_usuarios;
    }

    public Actualizacion(int id_estado, int id_usuarios) {
        this.id_estado = id_estado;
        this.id_usuarios = id_usuarios;
    }
    
    
    
    

    public int getIdActualizacion() {
        return idActualizacion;
    }

    public void setIdActualizacion(int idActualizacion) {
        this.idActualizacion = idActualizacion;
    }

    public Date getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public DateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(DateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    

    public void setFecha_actualizacion(Date fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(int id_usuarios) {
        this.id_usuarios = id_usuarios;
    }
    
    
    
    
}
