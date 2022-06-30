
package Dominio;


public class Plataforma {
    
    private int idPlataforma;
    private String nombre_plataforma;
    private int id_tipoPago;
    private int idTipoPago;
    private String tipo_pago;

    public Plataforma() {
    }

    public Plataforma(int idPlataforma, String nombre_plataforma, String tipo_pago) {
        this.idPlataforma = idPlataforma;
        this.nombre_plataforma = nombre_plataforma;
        this.tipo_pago = tipo_pago;
    }

    
    
    public Plataforma(int idPlataforma, String nombre_plataforma, int id_tipoPago) {
        this.idPlataforma = idPlataforma;
        this.nombre_plataforma = nombre_plataforma;
        this.id_tipoPago = id_tipoPago;
    }

    public Plataforma(int idPlataforma, String nombre_plataforma, int idTipoPago, String tipo_pago) {
        this.idPlataforma = idPlataforma;
        this.nombre_plataforma = nombre_plataforma;
        this.idTipoPago = idTipoPago;
        this.tipo_pago = tipo_pago;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }
    
    
    

    public int getIdPlataforma() {
        return idPlataforma;
    }

    public void setIdPlataforma(int idPlataforma) {
        this.idPlataforma = idPlataforma;
    }

    public String getNombre_plataforma() {
        return nombre_plataforma;
    }

    public void setNombre_plataforma(String nombre_plataforma) {
        this.nombre_plataforma = nombre_plataforma;
    }

    public int getId_tipoPago() {
        return id_tipoPago;
    }

    public void setId_tipoPago(int id_tipoPago) {
        this.id_tipoPago = id_tipoPago;
    }
    
    
    
}
