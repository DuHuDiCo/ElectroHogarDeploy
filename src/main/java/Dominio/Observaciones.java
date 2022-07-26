
package Dominio;

import org.joda.time.DateTime;


public class Observaciones {
    
    private int idObservaciones;
    private String observacion;
    private DateTime fecha;
    private String fecha_observacion;
    private int id_usuario;
    private int id_consignacion;
    private String nombre_usuario;
    private String Error;

    public Observaciones() {
    }

    public Observaciones(int idObservaciones, String observacion, String fecha, String nombre_usuario) {
        this.idObservaciones = idObservaciones;
        this.observacion = observacion;
        this.fecha_observacion = fecha;
        this.nombre_usuario = nombre_usuario;
    }
    
    

    public Observaciones(int idObservaciones, int id_consignacion) {
        this.idObservaciones = idObservaciones;
        this.id_consignacion = id_consignacion;
    }
    
    

    public Observaciones(String observacion, int id_usuario, int id_consignacion) {
        this.observacion = observacion;
        this.id_usuario = id_usuario;
        this.id_consignacion = id_consignacion;
    }
    
    

    public int getIdObservaciones() {
        return idObservaciones;
    }

    public void setIdObservaciones(int idObservaciones) {
        this.idObservaciones = idObservaciones;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public DateTime getFecha() {
        return fecha;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha_observacion() {
        return fecha_observacion;
    }

    public void setFecha_observacion(String fecha_observacion) {
        this.fecha_observacion = fecha_observacion;
    }
    

    public void setFecha(DateTime fecha) {
        this.fecha = fecha;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_consignacion() {
        return id_consignacion;
    }

    public void setId_consignacion(int id_consignacion) {
        this.id_consignacion = id_consignacion;
    }

    public String getError() {
        return Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }
    
    
    
    
}
