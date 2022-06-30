
package Dominio;

import java.sql.Date;




public class Obligaciones extends Sedes{
   
    
    private int idObligacion;
    private String nombre_titular;
    private String tipo_documento;
    private String n_documento;
    private String telefono_titular;
    private String email;
    private String direccion;
    private String clasificacion_cliente;
    private String codigo_cliente;
    private float valor_cuota;
    private float saldo_capital;
    private Date fecha_obligacion;
    private float saldo_mora;
    private int dias_mora;
    private String dato_personalizado;
    private int id_sede;
    private int id_consignacion;
    private int id_fileTxt;
    

    public Obligaciones() {
    }

    public Obligaciones(int idObligacion, String nombre_titular, float saldo_capital, Date fecha_obligacion, int idSede, String nombre_sede) {
        super(idSede, nombre_sede);
        this.idObligacion = idObligacion;
        this.nombre_titular = nombre_titular;
        this.saldo_capital = saldo_capital;
        this.fecha_obligacion = fecha_obligacion;
    }

    public Obligaciones(int idObligacion, String nombre_titular, String n_documento, String telefono_titular, String email, String direccion, String codigo_cliente, float valor_cuota, float saldo_capital, Date fecha_obligacion, float saldo_mora, int dias_mora, String nombre_sede) {
        super(nombre_sede);
        this.idObligacion = idObligacion;
        this.nombre_titular = nombre_titular;
        this.n_documento = n_documento;
        this.telefono_titular = telefono_titular;
        this.email = email;
        this.direccion = direccion;
        this.codigo_cliente = codigo_cliente;
        this.valor_cuota = valor_cuota;
        this.saldo_capital = saldo_capital;
        this.fecha_obligacion = fecha_obligacion;
        this.saldo_mora = saldo_mora;
        this.dias_mora = dias_mora;
    }
    
    
    
    

    public Obligaciones(String nombre_titular, String tipo_documento, String n_documento, String telefono_titular, String email, String direccion, String clasificacion_cliente, String codigo_cliente, float valor_cuota, float saldo_capital, Date fecha_obligacion ,float saldo_mora, int dias_mora, int id_sede, int id_fileTxt) {
        this.nombre_titular = nombre_titular;
        this.tipo_documento = tipo_documento;
        this.n_documento = n_documento;
        this.telefono_titular = telefono_titular;
        this.email = email;
        this.direccion = direccion;
        this.clasificacion_cliente = clasificacion_cliente;
        this.codigo_cliente = codigo_cliente;
        this.valor_cuota = valor_cuota;
        this.saldo_capital = saldo_capital;
        this.fecha_obligacion = fecha_obligacion;
        this.saldo_mora = saldo_mora;
        this.dias_mora =  dias_mora;
        this.id_sede = id_sede;
        this.id_fileTxt = id_fileTxt;
    }

    public int getId_fileTxt() {
        return id_fileTxt;
    }

    public void setId_fileTxt(int id_fileTxt) {
        this.id_fileTxt = id_fileTxt;
    }

    public Date getFecha_obligacion() {
        return fecha_obligacion;
    }

    public void setFecha_obligacion(Date fecha_obligacion) {
        this.fecha_obligacion = fecha_obligacion;
    }
    
    
    
    
    

    public int getIdObligacion() {
        return idObligacion;
    }

    public void setIdObligacion(int idObligacion) {
        this.idObligacion = idObligacion;
    }

    public String getNombre_titular() {
        return nombre_titular;
    }

    public void setNombre_titular(String nombre_titular) {
        this.nombre_titular = nombre_titular;
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

    public String getTelefono_titular() {
        return telefono_titular;
    }

    public void setTelefono_titular(String telefono_titular) {
        this.telefono_titular = telefono_titular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getClasificacion_cliente() {
        return clasificacion_cliente;
    }

    public void setClasificacion_cliente(String clasificacion_cliente) {
        this.clasificacion_cliente = clasificacion_cliente;
    }

    public String getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(String codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public float getValor_cuota() {
        return valor_cuota;
    }

    public void setValor_cuota(float valor_cuota) {
        this.valor_cuota = valor_cuota;
    }

    public float getSaldo_capital() {
        return saldo_capital;
    }

    public void setSaldo_capital(float saldo_capital) {
        this.saldo_capital = saldo_capital;
    }

    public float getSaldo_mora() {
        return saldo_mora;
    }

    public void setSaldo_mora(float saldo_mora) {
        this.saldo_mora = saldo_mora;
    }

    public int getDias_mora() {
        return dias_mora;
    }

    public void setDias_mora(int dias_mora) {
        this.dias_mora = dias_mora;
    }

    public String getDato_personalizado() {
        return dato_personalizado;
    }

    public void setDato_personalizado(String dato_personalizado) {
        this.dato_personalizado = dato_personalizado;
    }

    public int getId_sede() {
        return id_sede;
    }

    public void setId_sede(int id_sede) {
        this.id_sede = id_sede;
    }

    public int getId_consignacion() {
        return id_consignacion;
    }

    public void setId_consignacion(int id_consignacion) {
        this.id_consignacion = id_consignacion;
    }

   

    

  
    
    
    
}
