function cargarDatosInformes() {
    validarSession();
    obtenerNombreUsuario();
    traerReportesAdmin();
    cargarUsuariosReporte();
}

function traerReportesAdmin() {
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=traerReportesAdmin"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = '<td><a href="#" id="btn_pdf_' + contador + '" onclick="descargarReporteAdmin(' + value.idFile + ');" class="btn btn-primary btn-sm"><i class="fas fa-eye"></i></a></td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.nombre_usuario + '</td><td>' + value.fecha + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}

function descargarReporteAdmin(idFile) {
    $('#staticBackdropPdfCartera').modal('show');

    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=descargarReporteAdmin&idFile=" + idFile

    }).done(function (data) {
        var datos = data;


        document.getElementById('mostrarPdf').src = datos;



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
        url: "ServletControladorFiles?accion=traerReportesAdminByFecha&fecha=" + fecha

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = '<td><a href="#" id="btn_pdf_' + contador + '" onclick="descargarReporteAdmin(' + value.idFile + ');" class="btn btn-primary btn-sm"><i class="fas fa-eye"></i></a></td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.nombre_usuario + '</td><td>' + value.fecha + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
});

function cargarUsuariosReporte() {
    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=listarUsuarios"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var html = "";

        $.each(json, function (key, value) {

            $("#sltUsuariosReportes").append('<option value="' + value.idUsuario + '" >' + value.nombre_usuario + '</option>');
        });





    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

var filtroUsuario = document.getElementById('sltUsuariosReportes');

filtroUsuario.addEventListener('change', (event) => {
    validarSession();
    event.preventDefault();

    var usuario = filtroUsuario.value;
    var fecha = document.getElementById('filtroReportes').value;
    var URL = null;
    if (fecha === "") {
        URL = "ServletControladorFiles?accion=traerReportesAdminByUsuario&idUsuario=" + usuario;
    }else{
        URL = "ServletControladorFiles?accion=traerReportesAdminByUsuario&idUsuario=" + usuario+"&fecha="+fecha;
    }
    

    $.ajax({
        method: "GET",
        url: URL

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = '<td><a href="#" id="btn_pdf_' + contador + '" onclick="descargarReporteAdmin(' + value.idFile + ');" class="btn btn-primary btn-sm"><i class="fas fa-eye"></i></a></td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.nombre_usuario + '</td><td>' + value.fecha + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


});