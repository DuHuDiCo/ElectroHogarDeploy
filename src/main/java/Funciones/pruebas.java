/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Funciones;

import static Funciones.FuncionesGenerales.fechaSQL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.joda.time.LocalDate;

/**
 *
 * @author DUVAN
 */
public class pruebas {

   
    public static void main(String[] args) {
        
        String fecha = "2022-06-01";
        Date f = fechaSQL(fecha, "yyyy-MM-dd");
        Date fech = voltearFecha(f);
        System.out.println(fech);
        
    }
    
    public static void fechaInicio(){
        LocalDate date = LocalDate.now();
        System.out.println(date);
    }
    
    public static Date voltearFecha(Date fecha){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(fecha);
        Date f = fechaSQL(formattedDate, "dd-MM-yyyy");
        return f;
    }
    
}
