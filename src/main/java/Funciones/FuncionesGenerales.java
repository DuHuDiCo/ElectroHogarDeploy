package Funciones;

import Datos.DaoConsignaciones;
import Datos.DaoRoles;
import Datos.DaoUsuarios;
import Dominio.Consignacion;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FuncionesGenerales {

    public static Date obtenerFechaServer(String formato) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formato);

        java.util.Date datObj = calendar.getTime();
        String formattedDate = sdf.format(datObj);
        java.sql.Date dateformated = fechaSQL(formattedDate, formato);

        return dateformated;
    }

    public static Date fechaSQL(String fecha, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        java.util.Date javaDate = null;
        try {
            javaDate = sdf.parse(fecha);
        } catch (ParseException e) {
        }

        java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        return sqlDate;
    }

    public static String fechaString(Date fecha) {
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        String StrDate = dateFormat.format(fecha);
        return StrDate;
    }

    public static Date dateToDateSql(java.util.Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = simpleDateFormat.format(fecha);
        Date date = fechaSQL(formattedDate, "yyyy-MM-dd HH:mm:ss");
        return date;
    }

    public static String fechaDateTime() {
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = dtf.format(LocalDateTime.now());
        return fecha;
    }

    public static DateTime stringToDateTime(String fecha) {
        DateTimeFormatter dateTimeformat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime joda_time = dateTimeformat.parseDateTime(fecha);
        return joda_time;
    }

    public static Date fechaDateTimeToDate(DateTime fecha) {
        java.util.Date date = fecha.toDate();
        Date fech = dateToDateSql(date);
        return fech;
    }

    public static String generarPdf(List<Consignacion> consig, String email) throws IOException, ClassNotFoundException {
        String titulo = "Reporte de Actividades";
        Date fecha = obtenerFechaServer("yyyy-MM-dd");
        String fechaHora = fechaDateTime();
        String cargo = new DaoRoles().obtenerRolUsuario(email);
        String ciudad = new DaoUsuarios().obtenerSedeUsuario(email);
        String nombre = new DaoConsignaciones().obtenerNombreUsuario(email);
        int random = (int) (Math.random() * 100);
        String ruta = "/opt/glassfish/glassfish/domains/domain1/applications/ROOT/archivos/reportes/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";

        try {
            try (PDDocument doc = new PDDocument()) {
                PDPage page = new PDPage();
                doc.addPage(page);
                int height = (int) page.getTrimBox().getHeight();//792
                int width = (int) page.getTrimBox().getWidth();//612
                int initX = 20;
                int initY = height - 150;
                int cellHeight = 20;
                int cellWidth = 70;
                int colCount = 8;

                try (PDPageContentStream contens = new PDPageContentStream(doc, page)) {
                    nuevaLinea(titulo, 220, 750, contens, PDType1Font.HELVETICA_BOLD, 18);
                    int con = 10;

                    String usuario = "Usuario: " + nombre;
                    String date = "Fecha: " + fechaHora;
                    String rol = "Cargo: " + cargo;
                    String sede = "Ciudad: " + ciudad;

                    nuevaLinea(usuario, 70, 720, contens, PDType1Font.HELVETICA_BOLD, 12);
                    nuevaLinea(date, 70, 700, contens, PDType1Font.HELVETICA_BOLD, 12);
                    nuevaLinea(rol, 70, 680, contens, PDType1Font.HELVETICA_BOLD, 12);
                    nuevaLinea(sede, 70, 660, contens, PDType1Font.HELVETICA_BOLD, 12);

                    //tabla con el contenido
                    for (int i = 1; i <= consig.size(); i++) {
                        if (i == 1) {
                            //cabeceron tabla
                            for (int j = 1; j <= colCount; j++) {
                                if (j == 1) {
                                    contens.addRect(initX, initY, 20, -cellHeight);
                                    contens.beginText();
                                    contens.newLineAtOffset(initX, initY - cellHeight + 10);
                                    contens.setFont(PDType1Font.HELVETICA_BOLD, 12);
                                    String cabecerosTabla = retornarCabeceros(j);
                                    contens.showText(cabecerosTabla);
                                    contens.endText();
                                    initX += cellWidth;
                                } else {
                                    contens.addRect(initX, initY, cellWidth, -cellHeight);

                                    contens.beginText();
                                    contens.newLineAtOffset(initX + 10, initY - cellHeight + 10);
                                    contens.setFont(PDType1Font.HELVETICA_BOLD, 12);
                                    String cabecerosTabla = retornarCabeceros(j);
                                    contens.showText(cabecerosTabla);
                                    contens.endText();
                                    initX += cellWidth;
                                }

                            }
                            initX = 20;

                            initY = (height - 150) - (con += 10);
                            //body tabla primera
                            for (int j = 1; j <= colCount; j++) {

                                if (j == 1) {
                                    contens.addRect(initX, initY, 20, -cellHeight);

                                    contens.beginText();
                                    contens.newLineAtOffset(initX, initY - 10);
                                    contens.setFont(PDType1Font.TIMES_ROMAN, 10);
                                    String body = Integer.toString(i);
                                    contens.showText(body);
                                    contens.endText();
                                    initX += cellWidth;
                                } else {
                                    contens.addRect(initX, initY, cellWidth, -cellHeight);

                                    contens.beginText();
                                    contens.newLineAtOffset(initX + 10, initY - 10);
                                    contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                    String body = retornarBody(j, consig.get(i - 1));
                                    contens.showText(body);
                                    contens.endText();
                                    initX += cellWidth;
                                }

                            }
                        } else {
                            initX = 20;

                            initY = (height - 150) - (con += 10);

                            for (int j = 1; j <= colCount; j++) {

                                if (j == 1) {
                                    contens.addRect(initX, initY, 20, -cellHeight);

                                    contens.beginText();
                                    contens.newLineAtOffset(initX, initY - 10);
                                    contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                    String body = Integer.toString(i);
                                    contens.showText(body);
                                    contens.endText();
                                    initX += cellWidth;
                                } else {
                                    contens.addRect(initX, initY, cellWidth, -cellHeight);

                                    contens.beginText();
                                    contens.newLineAtOffset(initX + 10, initY - 10);
                                    contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                    String body = retornarBody(j, consig.get(i - 1));
                                    contens.showText(body);
                                    contens.endText();
                                    initX += cellWidth;
                                }

                            }
                        }

                    }

                    contens.close();
                }

                doc.save(ruta);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return ruta;
    }

    public static void nuevaLinea(String linea, int x, int y, PDPageContentStream contens, PDFont fuente, int tamañoFont) throws IOException {
        contens.beginText();
        PDFont font = fuente;
        contens.setFont(font, tamañoFont);
        contens.newLineAtOffset(x, y);

        contens.showText(linea);
        contens.endText();
    }

    public static void body(int initX, int initY, int cellWidth, int cellHeight, PDPageContentStream contens, List<Consignacion> consig) throws IOException {

        for (int i = 1; i <= consig.size(); i++) {

        }
    }

    public static String retornarCabeceros(int num) {
        String pal = null;
        switch (num) {
            case 1:
                pal = "N°";
                break;
            case 2:
                pal = "Titular";
                break;
            case 3:
                pal = "N° Recibo";
                break;
            case 4:
                pal = "F. Pago";
                break;
            case 5:
                pal = "F. Creacion";
                break;
            case 6:
                pal = "Valor";
                break;
            case 7:
                pal = "Estado";
                break;
            default:
                pal = "Banco";
        }

        return pal;
    }

    public static String retornarBody(int num, Consignacion con) {
        String pal = null;
        String f = null;

        switch (num) {
            case 2:
                pal = con.getNombre_titular();
                break;
            case 3:
                pal = con.getNum_recibo();
                break;
            case 4:
                f = fechaString(con.getFecha_pago());
                pal = f;
                break;
            case 5:
                f = fechaString(con.getFecha_creacion());
                pal = f;
                break;
            case 6:
                int va  = (int) con.getValor();
                pal = Integer.toString(va);
                break;
            case 7:
                pal = con.getNombre_estado();
                break;

            default:
                pal = con.getNombre_plataforma();
        }

        return pal;
    }

    public static String nombreArchivo(String ruta) {
        File archivo = new File(ruta);
        String nombreArch = archivo.getName();
        return nombreArch;
    }

    public static String fechaInicioMes() {
        String date = Integer.toString(LocalDate.now().getMonthOfYear());
        String year = Integer.toString(LocalDate.now().getYear());
        String fullDate = year + "-" + "0" + date + "-01";
        return fullDate;
    }

    public static String fechaFinMes() {
        String date = Integer.toString(LocalDate.now().getMonthOfYear());
        String year = Integer.toString(LocalDate.now().getYear());
        String fullDate = null;
        switch (date) {
            case "1":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "2":
                fullDate = year + "-" + "0" + date + "-28";
                break;
            case "3":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "4":
                fullDate = year + "-" + "0" + date + "-30";
                break;
            case "5":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "6":
                fullDate = year + "-" + "0" + date + "-30";
                break;
            case "7":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "8":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "9":
                fullDate = year + "-" + "0" + date + "-30";
                break;
            case "10":
                fullDate = year + "-" + "0" + date + "-31";
                break;
            case "11":
                fullDate = year + "-" + "0" + date + "-30";
                break;
            default:
                fullDate = year + "-" + "0" + date + "-31";
        }

        return fullDate;
    }

    public Date voltearFecha (Date fecha){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(fecha);
        Date f = fechaSQL(formattedDate, "dd-MM-yyyy");
        return f;
    }

}
