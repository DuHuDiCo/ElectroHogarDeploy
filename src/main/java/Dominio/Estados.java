
package Dominio;


public class Estados {
    
    private int idEstado;
    private String nombre_estado;

    public Estados() {
    }

    public Estados(int idEstado, String nombre_estado) {
        this.idEstado = idEstado;
        this.nombre_estado = nombre_estado;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre_estado() {
        return nombre_estado;
    }

    public void setNombre_estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }
    
    
    
}
