function cargarDatosInformes() {
    validarSession();
    obtenerNombreUsuario();
    traerReportesCartera();
}

function traerReportesCartera() {
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=traerReportesByIdUsuario"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = '<td><a href="#" id="btn_pdf_' + contador + '" onclick="descargarReporte('+value.idFile+');" class="btn btn-primary btn-sm"><i class="fas fa-eye"></i></a></td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.fecha + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}


var filtro = document.getElementById('filtroReportes');

filtro.addEventListener('change', (event) => {
    validarSession();
    event.preventDefault();

    var fecha = filtro.value;
    
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=traerReportesByFecha&fecha="+fecha

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = '<td><a href="#" id="btn_pdf_' + contador + '" onclick="descargarReporte('+value.idFile+');" class="btn btn-primary btn-sm"><i class="fas fa-eye"></i></a></td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.fecha + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
});

function descargarReporte(idFile){
    $('#staticBackdropPdfCartera').modal('show');
    
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=descargarReporte&idFile="+idFile

    }).done(function (data) {
        var datos = data;
        
        
        document.getElementById('mostrarPdf').src = datos;
        


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}