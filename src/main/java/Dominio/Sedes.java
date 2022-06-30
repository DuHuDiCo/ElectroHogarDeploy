package Dominio;

public class Sedes {

    int idSede;
    String nombre_sede;
    String municipio;
    String telefono;
    String datper;
    
    public Sedes() {
    }

    public Sedes(String nombre_sede) {
        this.nombre_sede = nombre_sede;
    }
    
    

    public Sedes(int idSede, String nombre_sede) {
        this.idSede = idSede;
        this.nombre_sede = nombre_sede;
    }

    public Sedes(String nombre_sede, String municipio, String telefono, String datper) {
        this.nombre_sede = nombre_sede;
        this.municipio = municipio;
        this.telefono = telefono;
        this.datper = datper;
    }
    
    
    
    

    public Sedes(int sede) {
        this.idSede = sede;
    }
    
    public Sedes(int sede, String nombre_sede, String municipio, String telefono, String datper) {
        this.idSede = sede;
        this.nombre_sede = nombre_sede;
        this.municipio = municipio;
        this.telefono = telefono;
        this.datper = datper;
    }

    public int getSede() {
        return idSede;
    }

    public void setSede(int sede) {
        this.idSede = sede;
    }

    public String getNombre_sede() {
        return nombre_sede;
    }

    public void setNombre_sede(String nombre_sede) {
        this.nombre_sede = nombre_sede;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDatper() {
        return datper;
    }

    public void setDatper(String datper) {
        this.datper = datper;
    }

}
