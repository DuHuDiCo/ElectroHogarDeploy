package Funciones;

import Datos.DaoConsignaciones;
import Datos.DaoConsignaciones2;
import Datos.DaoFiles;
import Datos.DaoRoles;
import Datos.DaoUsuarios;
import Dominio.Consignacion;
import com.itextpdf.text.pdf.BaseFont;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
        String ruta = null;
        switch(cargo){
            case "Administrador":
                ruta = "/var/lib/tomcat9/webapps/ROOT/archivos/reportes/Admin/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";
            break;
            case "Cartera":
                ruta = "/var/lib/tomcat9/webapps/ROOT/archivos/reportes/Cartera/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";
            break;
            case "Contabilidad":
                ruta = "/var/lib/tomcat9/webapps/ROOT/archivos/reportes/Contabilidad/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";
            break;
            case "Caja":
                ruta = "/var/lib/tomcat9/webapps/ROOT/archivos/reportes/Caja/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";
            break;
        }
        
         

        try {
            try (PDDocument doc = new PDDocument()) {
                PDPage page = new PDPage();
                doc.addPage(page);
                int height = (int) page.getTrimBox().getHeight();//792
                int width = (int) page.getTrimBox().getWidth();//612
                int initX = 20;
                int initY = height - 150;
                int cellHeight = 20;
                int cellWidth = 95;
                int colCount = 6;

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
                                    if (j == 2) {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX - 35, initY - cellHeight + 10);
                                        contens.setFont(PDType1Font.HELVETICA_BOLD, 12);
                                        String cabecerosTabla = retornarCabeceros(j);
                                        contens.showText(cabecerosTabla);
                                        contens.endText();
                                        initX += cellWidth;
                                    } else {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX + 20, initY - cellHeight + 10);
                                        contens.setFont(PDType1Font.HELVETICA_BOLD, 12);
                                        String cabecerosTabla = retornarCabeceros(j);
                                        contens.showText(cabecerosTabla);
                                        contens.endText();
                                        initX += cellWidth;
                                    }

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
                                    if (j == 2) {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX - 35, initY - 10);
                                        contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                        String body = retornarBody(j, consig.get(i - 1));
                                        contens.showText(body);
                                        contens.endText();
                                        initX += cellWidth;
                                    } else {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX + 20, initY - 10);
                                        contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                        String body = retornarBody(j, consig.get(i - 1));
                                        contens.showText(body);
                                        contens.endText();
                                        initX += cellWidth;
                                    }

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
                                    if (j == 2) {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX - 35, initY - 10);
                                        contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                        String body = retornarBody(j, consig.get(i - 1));
                                        contens.showText(body);
                                        contens.endText();
                                        initX += cellWidth;
                                    } else {
                                        contens.addRect(initX, initY, cellWidth, -cellHeight);

                                        contens.beginText();
                                        contens.newLineAtOffset(initX + 20, initY - 10);
                                        contens.setFont(PDType1Font.TIMES_ROMAN, 12);
                                        String body = retornarBody(j, consig.get(i - 1));
                                        contens.showText(body);
                                        contens.endText();
                                        initX += cellWidth;
                                    }
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

    public static String generarPdfCaja(List<Consignacion> con, String email) throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
        String titulo = "Reporte de Consignaciones";
        Date fecha = obtenerFechaServer("yyyy-MM-dd");
        String fechaHora = fechaDateTime();
        String cargo = new DaoRoles().obtenerRolUsuario(email);
        String ciudad = new DaoUsuarios().obtenerSedeUsuario(email);
        String nombre = new DaoConsignaciones().obtenerNombreUsuario(email);
        int random = (int) (Math.random() * 100);
        String ruta = "/var/lib/tomcat9/webapps/ROOT/archivos/reportes/Caja/reporte_" + fecha + "_" + random + "_" + cargo + ".pdf";

        float heightDoc = 792;
        float widthDoc = 612;
        float margen = (float) 14.17;
        float imageWidth = (float) 277.8;
        float imageHeight = (float) 226.77;
        int colums = 2;
        int rows = 2;
        float positionX = heightDoc - margen - imageHeight;
        float positionY = margen;
        float datos = (float) 140.89;

        try {
            try (PDDocument doc = new PDDocument()) {

                if (con.size() >= 5) {
                    PDPage blanckPage = null;
                    int cantidad = con.size();
                    int numeroPaginas = (int) (cantidad / 4) + 1;
                    int list = 0;
                    int count = 0;
                    for (int i = 0; i < numeroPaginas; i++) {
                        blanckPage = new PDPage();
                        doc.addPage(blanckPage);
                        if (i < numeroPaginas) {
                            Consignacion co = con.get(count);
                            Consignacion co2 = con.get(count + 1);
                            Consignacion co3 = con.get(count + 2);
                            Consignacion co4 = con.get(count + 3);
                            count += 4;
                            int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                            int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                            int idFile3 = new DaoFiles().obtenerIdFileImg(co3.getIdConsignacion());
                            int idFile4 = new DaoFiles().obtenerIdFileImg(co4.getIdConsignacion());
                            String img = new DaoFiles().obtenerRutaImagen(idFile);
                            String img2 = new DaoFiles().obtenerRutaImagen(idFile2);
                            String img3 = new DaoFiles().obtenerRutaImagen(idFile3);
                            String img4 = new DaoFiles().obtenerRutaImagen(idFile4);

                            FileInputStream in = new FileInputStream(img);
                            PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                            PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                            PDImageXObject image3 = PDImageXObject.createFromFile(img3, doc);
                            PDImageXObject image4 = PDImageXObject.createFromFile(img4, doc);
                            try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                                positionX = 0;
                                positionX = margen;
                                positionY = 0;
                                positionY = heightDoc - margen - imageHeight;
                                contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea5 = "Observaciones: ";
                                nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                positionX = 0;
                                positionX = margen + imageWidth + margen + margen;
                                contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea10 = "Observaciones: ";
                                nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                positionX = 0;
                                positionY = 0;
                                positionX = margen;
                                positionY = heightDoc - margen - imageHeight - datos - margen - margen - imageHeight;
                                contents.drawImage(image3, positionX, positionY, imageWidth, imageHeight);
                                String linea11 = "Fecha: " + co3.getFecha_pago() + "    Valor: " + co3.getValor() + "    Medio Pago: " + co3.getNombre_plataforma();
                                nuevaLineaFloat(linea11, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea12 = "Numero Recibo: " + co3.getNum_recibo() + "   Sede: " + co3.getNombre_sede() + "  Cedula: " + co3.getNumero_documento();
                                nuevaLineaFloat(linea12, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea13 = "Nombre de Cliente: " + co3.getNombre_titular();
                                nuevaLineaFloat(linea13, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea14 = "¿Quien Confirmo?: " + co3.getNombreUsuario();
                                nuevaLineaFloat(linea14, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea15 = "Observaciones: ";
                                nuevaLineaFloat(linea15, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                positionX = 0;
                                positionX = margen + imageWidth + margen + margen;
                                contents.drawImage(image4, positionX, positionY, imageWidth, imageHeight);
                                String linea16 = "Fecha: " + co4.getFecha_pago() + "    Valor: " + co4.getValor() + "    Medio Pago: " + co4.getNombre_plataforma();
                                nuevaLineaFloat(linea16, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea17 = "Numero Recibo: " + co4.getNum_recibo() + "   Sede: " + co4.getNombre_sede() + "  Cedula: " + co4.getNumero_documento();
                                nuevaLineaFloat(linea17, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea18 = "Nombre de Cliente: " + co4.getNombre_titular();
                                nuevaLineaFloat(linea18, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea19 = "¿Quien Confirmo?: " + co4.getNombreUsuario();
                                nuevaLineaFloat(linea19, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea20 = "Observaciones: ";
                                nuevaLineaFloat(linea20, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                contents.close();

                            }
                        } else {

                            if (i == numeroPaginas) {
                                int num = i * 4;
                                int valor = cantidad - num;

                                if (valor == 1) {
                                    Consignacion co = con.get(count);
                                    int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                    String img = new DaoFiles().obtenerRutaImagen(idFile);

                                    FileInputStream in = new FileInputStream(img);
                                    PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                    try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                                        positionX = 0;
                                        positionX = margen;
                                        positionY = 0;
                                        positionY = heightDoc - margen - imageHeight;
                                        contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                        String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                        nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                        nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                        nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                        nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea5 = "Observaciones: ";
                                        nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);

                                        contents.close();

                                    }
                                } else {
                                    if (valor == 2) {
                                        Consignacion co = con.get(count);
                                        Consignacion co2 = con.get(count + 1);
                                        count += 2;
                                        int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                        int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                                        String img = new DaoFiles().obtenerRutaImagen(idFile);
                                        String img2 = new DaoFiles().obtenerRutaImagen(idFile2);

                                        FileInputStream in = new FileInputStream(img);
                                        PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                        PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                                        try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                                            positionX = 0;
                                            positionX = margen;
                                            positionY = 0;
                                            positionY = heightDoc - margen - imageHeight;
                                            contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                            String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                            nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                            nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                            nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                            nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea5 = "Observaciones: ";
                                            nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            positionX = 0;
                                            positionX = margen + imageWidth + margen + margen;
                                            contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                            String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                            nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                            nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                            nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                            nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            String linea10 = "Observaciones: ";
                                            nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                            contents.close();

                                        }
                                    } else {
                                        if (valor == 3) {
                                            Consignacion co = con.get(count);
                                            Consignacion co2 = con.get(count + 1);
                                            Consignacion co3 = con.get(count + 2);
                                            count += 3;
                                            int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                            int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                                            int idFile3 = new DaoFiles().obtenerIdFileImg(co3.getIdConsignacion());
                                            String img = new DaoFiles().obtenerRutaImagen(idFile);
                                            String img2 = new DaoFiles().obtenerRutaImagen(idFile2);
                                            String img3 = new DaoFiles().obtenerRutaImagen(idFile3);

                                            FileInputStream in = new FileInputStream(img);
                                            PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                            PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                                            PDImageXObject image3 = PDImageXObject.createFromFile(img3, doc);
                                            try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                                                positionX = 0;
                                                positionX = margen;
                                                positionY = 0;
                                                positionY = heightDoc - margen - imageHeight;
                                                contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                                String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                                nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                                nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                                nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                                nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea5 = "Observaciones: ";
                                                nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                positionX = 0;
                                                positionX = margen + imageWidth + margen + margen;
                                                contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                                String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                                nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                                nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                                nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                                nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea10 = "Observaciones: ";
                                                nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                positionX = 0;
                                                positionY = 0;
                                                positionX = margen;
                                                positionY = heightDoc - margen - imageHeight - datos - margen - margen - imageHeight;
                                                contents.drawImage(image3, positionX, positionY, imageWidth, imageHeight);
                                                String linea11 = "Fecha: " + co3.getFecha_pago() + "    Valor: " + co3.getValor() + "    Medio Pago: " + co3.getNombre_plataforma();
                                                nuevaLineaFloat(linea11, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea12 = "Numero Recibo: " + co3.getNum_recibo() + "   Sede: " + co3.getNombre_sede() + "  Cedula: " + co3.getNumero_documento();
                                                nuevaLineaFloat(linea12, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea13 = "Nombre de Cliente: " + co3.getNombre_titular();
                                                nuevaLineaFloat(linea13, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea14 = "¿Quien Confirmo?: " + co3.getNombreUsuario();
                                                nuevaLineaFloat(linea14, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                String linea15 = "Observaciones: ";
                                                nuevaLineaFloat(linea15, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                contents.close();

                                            }
                                        } else {
                                            if (valor == 4) {
                                                Consignacion co = con.get(count);
                                                Consignacion co2 = con.get(count + 1);
                                                Consignacion co3 = con.get(count + 2);
                                                Consignacion co4 = con.get(count + 3);
                                                count += 4;
                                                int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                                int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                                                int idFile3 = new DaoFiles().obtenerIdFileImg(co3.getIdConsignacion());
                                                int idFile4 = new DaoFiles().obtenerIdFileImg(co4.getIdConsignacion());
                                                String img = new DaoFiles().obtenerRutaImagen(idFile);
                                                String img2 = new DaoFiles().obtenerRutaImagen(idFile2);
                                                String img3 = new DaoFiles().obtenerRutaImagen(idFile3);
                                                String img4 = new DaoFiles().obtenerRutaImagen(idFile4);

                                                FileInputStream in = new FileInputStream(img);
                                                PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                                PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                                                PDImageXObject image3 = PDImageXObject.createFromFile(img3, doc);
                                                PDImageXObject image4 = PDImageXObject.createFromFile(img4, doc);
                                                try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                                                    positionX = 0;
                                                    positionX = margen;
                                                    positionY = 0;
                                                    positionY = heightDoc - margen - imageHeight;
                                                    contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                                    String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                                    nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                                    nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                                    nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                                    nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea5 = "Observaciones: ";
                                                    nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    positionX = 0;
                                                    positionX = margen + imageWidth + margen + margen;
                                                    contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                                    String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                                    nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                                    nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                                    nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                                    nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea10 = "Observaciones: ";
                                                    nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    positionX = 0;
                                                    positionY = 0;
                                                    positionX = margen;
                                                    positionY = heightDoc - margen - imageHeight - datos - margen - margen - imageHeight;
                                                    contents.drawImage(image3, positionX, positionY, imageWidth, imageHeight);
                                                    String linea11 = "Fecha: " + co3.getFecha_pago() + "    Valor: " + co3.getValor() + "    Medio Pago: " + co3.getNombre_plataforma();
                                                    nuevaLineaFloat(linea11, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea12 = "Numero Recibo: " + co3.getNum_recibo() + "   Sede: " + co3.getNombre_sede() + "  Cedula: " + co3.getNumero_documento();
                                                    nuevaLineaFloat(linea12, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea13 = "Nombre de Cliente: " + co3.getNombre_titular();
                                                    nuevaLineaFloat(linea13, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea14 = "¿Quien Confirmo?: " + co3.getNombreUsuario();
                                                    nuevaLineaFloat(linea14, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea15 = "Observaciones: ";
                                                    nuevaLineaFloat(linea15, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    positionX = 0;
                                                    positionX = margen + imageWidth + margen + margen;
                                                    contents.drawImage(image4, positionX, positionY, imageWidth, imageHeight);
                                                    String linea16 = "Fecha: " + co4.getFecha_pago() + "    Valor: " + co4.getValor() + "    Medio Pago: " + co4.getNombre_plataforma();
                                                    nuevaLineaFloat(linea16, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea17 = "Numero Recibo: " + co4.getNum_recibo() + "   Sede: " + co4.getNombre_sede() + "  Cedula: " + co4.getNumero_documento();
                                                    nuevaLineaFloat(linea17, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea18 = "Nombre de Cliente: " + co4.getNombre_titular();
                                                    nuevaLineaFloat(linea18, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea19 = "¿Quien Confirmo?: " + co4.getNombreUsuario();
                                                    nuevaLineaFloat(linea19, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    String linea20 = "Observaciones: ";
                                                    nuevaLineaFloat(linea20, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                                    contents.close();

                                                }
                                            }
                                        }
                                    }
                                }
                            } else {

                            }

                        }
                    }
                    doc.save(ruta);
                    doc.close();

                } else {

                    PDPage blanckPage = new PDPage();
                    doc.addPage(blanckPage);
                    positionX = 0;
                    positionY = 0;
                    positionX = margen;
                    positionY = heightDoc - margen - imageHeight;
                    int list = 0;
                    int can = con.size();
                    if (can == 1) {
                        Consignacion co = con.get(list);
                        int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                        String img = new DaoFiles().obtenerRutaImagen(idFile);

                        FileInputStream in = new FileInputStream(img);
                        PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                        try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {

                            contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                            String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                            nuevaLineaFloat(linea1, margen, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                            String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                            nuevaLineaFloat(linea2, margen, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                            String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                            nuevaLineaFloat(linea3, margen, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                            String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                            nuevaLineaFloat(linea4, margen, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                            String linea5 = "Observaciones: ";
                            nuevaLineaFloat(linea5, margen, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                            contents.close();

                        }
                    } else {
                        if (can == 2) {
                            Consignacion co = con.get(list);
                            Consignacion c = con.get(list + 1);
                            int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                            int idFile2 = new DaoFiles().obtenerIdFileImg(c.getIdConsignacion());
                            String img = new DaoFiles().obtenerRutaImagen(idFile);
                            String img2 = new DaoFiles().obtenerRutaImagen(idFile2);

                            FileInputStream in = new FileInputStream(img);
                            PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                            PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                            try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {

                                contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea5 = "Observaciones: ";
                                nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                positionX = 0;
                                positionX = margen + imageWidth + margen + margen;
                                contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                String linea6 = "Fecha: " + c.getFecha_pago() + "    Valor: " + c.getValor() + "    Medio Pago: " + c.getNombre_plataforma();
                                nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea7 = "Numero Recibo: " + c.getNum_recibo() + "   Sede: " + c.getNombre_sede() + "  Cedula: " + c.getNumero_documento();
                                nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea8 = "Nombre de Cliente: " + c.getNombre_titular();
                                nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea9 = "¿Quien Confirmo?: " + c.getNombreUsuario();
                                nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                String linea10 = "Observaciones: ";
                                nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                contents.close();

                            }
                        } else {
                            if (can == 3) {
                                Consignacion co = con.get(list);
                                Consignacion co2 = con.get(list + 1);
                                Consignacion co3 = con.get(list + 2);
                                int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                                int idFile3 = new DaoFiles().obtenerIdFileImg(co3.getIdConsignacion());
                                String img = new DaoFiles().obtenerRutaImagen(idFile);
                                String img2 = new DaoFiles().obtenerRutaImagen(idFile2);
                                String img3 = new DaoFiles().obtenerRutaImagen(idFile3);

                                FileInputStream in = new FileInputStream(img);
                                PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                                PDImageXObject image3 = PDImageXObject.createFromFile(img3, doc);
                                try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {

                                    contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                    String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                    nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                    nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                    nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                    nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea5 = "Observaciones: ";
                                    nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    positionX = 0;
                                    positionX = margen + imageWidth + margen + margen;
                                    contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                    String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                    nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                    nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                    nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                    nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea10 = "Observaciones: ";
                                    nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    positionX = 0;
                                    positionY = 0;
                                    positionX = margen;
                                    positionY = heightDoc - margen - imageHeight - datos - margen - margen - imageHeight;
                                    contents.drawImage(image3, positionX, positionY, imageWidth, imageHeight);
                                    String linea11 = "Fecha: " + co3.getFecha_pago() + "    Valor: " + co3.getValor() + "    Medio Pago: " + co3.getNombre_plataforma();
                                    nuevaLineaFloat(linea11, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea12 = "Numero Recibo: " + co3.getNum_recibo() + "   Sede: " + co3.getNombre_sede() + "  Cedula: " + co3.getNumero_documento();
                                    nuevaLineaFloat(linea12, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea13 = "Nombre de Cliente: " + co3.getNombre_titular();
                                    nuevaLineaFloat(linea13, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea14 = "¿Quien Confirmo?: " + co3.getNombreUsuario();
                                    nuevaLineaFloat(linea14, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    String linea15 = "Observaciones: ";
                                    nuevaLineaFloat(linea15, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                    contents.close();

                                }
                            } else {
                                if (can == 4) {
                                    Consignacion co = con.get(list);
                                    Consignacion co2 = con.get(list + 1);
                                    Consignacion co3 = con.get(list + 2);
                                    Consignacion co4 = con.get(list + 3);
                                    int idFile = new DaoFiles().obtenerIdFileImg(co.getIdConsignacion());
                                    int idFile2 = new DaoFiles().obtenerIdFileImg(co2.getIdConsignacion());
                                    int idFile3 = new DaoFiles().obtenerIdFileImg(co3.getIdConsignacion());
                                    int idFile4 = new DaoFiles().obtenerIdFileImg(co4.getIdConsignacion());
                                    String img = new DaoFiles().obtenerRutaImagen(idFile);
                                    String img2 = new DaoFiles().obtenerRutaImagen(idFile2);
                                    String img3 = new DaoFiles().obtenerRutaImagen(idFile3);
                                    String img4 = new DaoFiles().obtenerRutaImagen(idFile4);

                                    FileInputStream in = new FileInputStream(img);
                                    PDImageXObject image = PDImageXObject.createFromFile(img, doc);
                                    PDImageXObject image2 = PDImageXObject.createFromFile(img2, doc);
                                    PDImageXObject image3 = PDImageXObject.createFromFile(img3, doc);
                                    PDImageXObject image4 = PDImageXObject.createFromFile(img4, doc);
                                    try (PDPageContentStream contents = new PDPageContentStream(doc, blanckPage, PDPageContentStream.AppendMode.OVERWRITE, true)) {

                                        contents.drawImage(image, positionX, positionY, imageWidth, imageHeight);
                                        String linea1 = "Fecha: " + co.getFecha_pago() + "    Valor: " + co.getValor() + "    Medio Pago: " + co.getNombre_plataforma();
                                        nuevaLineaFloat(linea1, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea2 = "Numero Recibo: " + co.getNum_recibo() + "   Sede: " + co.getNombre_sede() + "  Cedula: " + co.getNumero_documento();
                                        nuevaLineaFloat(linea2, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea3 = "Nombre de Cliente: " + co.getNombre_titular();
                                        nuevaLineaFloat(linea3, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea4 = "¿Quien Confirmo?: " + co.getNombreUsuario();
                                        nuevaLineaFloat(linea4, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea5 = "Observaciones: ";
                                        nuevaLineaFloat(linea5, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        positionX = 0;
                                        positionX = margen + imageWidth + margen + margen;
                                        contents.drawImage(image2, positionX, positionY, imageWidth, imageHeight);
                                        String linea6 = "Fecha: " + co2.getFecha_pago() + "    Valor: " + co2.getValor() + "    Medio Pago: " + co2.getNombre_plataforma();
                                        nuevaLineaFloat(linea6, positionX, heightDoc - margen - imageHeight - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea7 = "Numero Recibo: " + co2.getNum_recibo() + "   Sede: " + co2.getNombre_sede() + "  Cedula: " + co2.getNumero_documento();
                                        nuevaLineaFloat(linea7, positionX, heightDoc - margen - imageHeight - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea8 = "Nombre de Cliente: " + co2.getNombre_titular();
                                        nuevaLineaFloat(linea8, positionX, heightDoc - margen - imageHeight - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea9 = "¿Quien Confirmo?: " + co2.getNombreUsuario();
                                        nuevaLineaFloat(linea9, positionX, heightDoc - margen - imageHeight - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea10 = "Observaciones: ";
                                        nuevaLineaFloat(linea10, positionX, heightDoc - margen - imageHeight - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        positionX = 0;
                                        positionY = 0;
                                        positionX = margen;
                                        positionY = heightDoc - margen - imageHeight - datos - margen - margen - imageHeight;
                                        contents.drawImage(image3, positionX, positionY, imageWidth, imageHeight);
                                        String linea11 = "Fecha: " + co3.getFecha_pago() + "    Valor: " + co3.getValor() + "    Medio Pago: " + co3.getNombre_plataforma();
                                        nuevaLineaFloat(linea11, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea12 = "Numero Recibo: " + co3.getNum_recibo() + "   Sede: " + co3.getNombre_sede() + "  Cedula: " + co3.getNumero_documento();
                                        nuevaLineaFloat(linea12, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea13 = "Nombre de Cliente: " + co3.getNombre_titular();
                                        nuevaLineaFloat(linea13, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea14 = "¿Quien Confirmo?: " + co3.getNombreUsuario();
                                        nuevaLineaFloat(linea14, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea15 = "Observaciones: ";
                                        nuevaLineaFloat(linea15, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        positionX = 0;
                                        positionX = margen + imageWidth + margen + margen;
                                        contents.drawImage(image4, positionX, positionY, imageWidth, imageHeight);
                                        String linea16 = "Fecha: " + co4.getFecha_pago() + "    Valor: " + co4.getValor() + "    Medio Pago: " + co4.getNombre_plataforma();
                                        nuevaLineaFloat(linea16, positionX, positionY - 10, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea17 = "Numero Recibo: " + co4.getNum_recibo() + "   Sede: " + co4.getNombre_sede() + "  Cedula: " + co4.getNumero_documento();
                                        nuevaLineaFloat(linea17, positionX, positionY - 30, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea18 = "Nombre de Cliente: " + co4.getNombre_titular();
                                        nuevaLineaFloat(linea18, positionX, positionY - 50, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea19 = "¿Quien Confirmo?: " + co4.getNombreUsuario();
                                        nuevaLineaFloat(linea19, positionX, positionY - 70, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        String linea20 = "Observaciones: ";
                                        nuevaLineaFloat(linea20, positionX, positionY - 90, contents, PDType1Font.HELVETICA_BOLD, 9);
                                        contents.close();

                                    }
                                }
                            }
                        }
                    }

                    doc.save(ruta);
                    doc.close();

                }

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

    public static void nuevaLineaFloat(String linea, float x, float y, PDPageContentStream contens, PDFont fuente, int tamañoFont) throws IOException {
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
                pal = "Cedula";
                break;
            case 3:
                pal = "N° Recibo";
                break;
            case 4:
                pal = "F. Pago";
                break;
            case 5:
                pal = "Valor";
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
                pal = con.getNumero_documento();
                break;
            case 3:
                pal = con.getNum_recibo();
                break;
            case 4:
                f = fechaString(con.getFecha_pago());
                pal = f;
                break;

            case 5:
                int va  = (int) con.getValor();
                pal = Integer.toString(va);
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

    public Date voltearFecha(Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(fecha);
        Date f = fechaSQL(formattedDate, "dd-MM-yyyy");
        return f;
    }

    public static void enviarCorreo(String asunto, String mensaje, String emailDestino) throws AddressException, MessagingException {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");
        propiedad.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(propiedad);
        String emailOrigen = "dudicodiaz@gmail.com";
        String password = "moqdfhvobkdzgjfn";

        MimeMessage mail = new MimeMessage(session);

        mail.setFrom(new InternetAddress(emailOrigen));
        mail.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDestino));
        mail.setSubject(asunto);
        mail.setText(mensaje);

        try {
            Transport transporte = session.getTransport("smtp");
            transporte.connect(emailOrigen, password);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            System.out.println("----" + transporte);
        } catch (MessagingException ex) {
            System.out.println(ex);
        }

    }

}
