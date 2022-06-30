
/* global Swal */

function crearTipoPago() {
    validarSession();
    var datos = {};

    datos.tipo_pago = document.getElementById('txtTipoPago').value;

    
    event.preventDefault();


    $.ajax({
        method: "POST",
        url: "ServletControladorAdministrador?accion=crearTipoPago",
        data: datos,
        dataType: 'JSON'
    }).done(function (data) {
        var dato = data;
        
        document.getElementById('txtTipoPago').value = "";
        if (dato !== 1) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'No Pudimos Guardar el Tipo Pago!',
                footer: '<a href="">Reportar el error?</a>'
            });

        } else {

            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Tipo Pago Guardado',
                showConfirmButton: false,
                timer: 3500

            });
            window.location.reload();
        }

    }).fail(function () {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Hubo un Error Reprotalo!',
            footer: '<a href="">Reportar el error?</a>'
        });
    }).always(function () {
        
    });

}


function crearBanco() {
    validarSession();
    var datos = {};

    datos.banco = document.getElementById('txtBanco').value;
    datos.tipo_pago = document.getElementById('sltTipoPagoBanco').value;

    event.preventDefault();
    
    $.ajax({
        method: "POST",
        url: "ServletControladorAdministrador?accion=crearBanco",
        data: datos,
        dataType: 'JSON'
    }).done(function (data) {
        var dato = data;
        
        document.getElementById('txtBanco').value = "";
        if (dato === 1) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Banco Guardado',
                showConfirmButton: false,
                timer: 3500

            });
            window.location.reload();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'No Pudimos Guardar el Tipo Pago!',
                footer: '<a href="">Reportar el error?</a>'
            });
        }

    }).fail(function () {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Hubo un Error Reprotalo!',
            footer: '<a href="">Reportar el error?</a>'
        });
    }).always(function () {

    });

}
function cargarDatosPago() {
    validarSession();
    obtenerNombreUsuario();
    event.preventDefault();

    $.ajax({
        method: "GET",
        url: "ServletControladorAdministrador?accion=llenarTipoPago"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var html = "";

        $.each(json, function (key, value) {
            $("#sltTipoPagoBanco").append('<option value="' + value.idTipoPago + '" >' + value.tipo_pago + '</option>');
        });
        listarBancos();



    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}

function listarBancos() {
    validarSession();
    event.preventDefault();

    $.ajax({
        method: "GET",
        url: "ServletControladorAdministrador?accion=listarBancos"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
       
        var html = "";

        var contador = 1;

        for (var banco of json) {
            var btnEditarBanco = '<td><a href="#" onclick="editarBanco(' + banco.idPlataforma + ')" class="btn btn-primary"><i class="fas fa-pen"></i></a></td>';
            var btnEditarTipoPago = '<td><a href="#" onclick="editarTipoPago(' + banco.idTipoPago + ')" class="btn btn-primary"><i class="fas fa-pen"></i></a></td>';
            var estadoHtml = '<tr> <td>' + contador + '</td><td>'+banco.nombre_plataforma+'</td>'+btnEditarBanco+'<td>'+banco.tipo_pago+'</td>' + btnEditarTipoPago + '</tr>';
            html += estadoHtml;
            contador = contador + 1;
        }
        console.log(json);

        document.querySelector('#tblBanco tbody').outerHTML = html;


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}


