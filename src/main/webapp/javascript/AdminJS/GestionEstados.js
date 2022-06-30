

/* global Swal */

function crearEstado() {
    validarSession();
    var datos = {};

    datos.estado = document.getElementById('txtEstado').value;

    
    event.preventDefault();


    $.ajax({
        method: "POST",
        url: "ServletControladorAdministrador?accion=crearEstado",
        data: datos,
        dataType: 'JSON'
    }).done(function (data) {
        var dato = data;
        
        document.getElementById('txtEstado').value = "";
        if (dato === 1) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Estado Guardado',
                showConfirmButton: false,
                timer: 3500
                
            });
            window.location.reload();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'No Pudimos Guardar el Estado!',
                footer: '<a href="">Reportar el error?</a>'
            });
        }

    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}


function listarEstados() {
    validarSession();
    obtenerNombreUsuario();
    event.preventDefault();

    $.ajax({
        method: "GET",
        url: "ServletControladorEstados?accion=cargarEstados"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var html = "";
        
        var contador = 1;

        for (var estado of json) {
            var btnEliminar = '<td><a href="#" onclick="editarEstado(' + estado.idEstado + ')" class="btn btn-primary"><i class="fas fa-pen"></i></a></td>';
            var estadoHtml = '<tr> <td>'+contador+'</td><td>'+estado.nombre_estado+'</td>'+btnEliminar+'</tr>';
            html += estadoHtml;
            contador = contador + 1;
        }
        console.log(json);
        
        document.querySelector('#estados tbody').outerHTML = html;


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}
