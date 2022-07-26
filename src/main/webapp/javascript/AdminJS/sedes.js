
/* global Swal */
function guardarSede() {
    validarSession();
    var json = {};

    json.sede = document.getElementById('txtSede').value;
    json.municipio = document.getElementById('txtMunicipio').value;
    json.telefono = document.getElementById('txtTelefono').value;
    json.dato_personalizado = document.getElementById('txtDatoPersonalizado').value;



    $.ajax({
        method: "POST",
        url: "ServletSedes?accion=guardarSede",
        data: json,
        dataType: 'JSON'
    }).done(function (data) {

        var json = data;




        if (json > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Sede Guardada Exitosamente',
                showConfirmButton: false,
                timer: 2000
            });
            
            setTimeout(recargarPagina, 2000);


        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Guardar la Sede',
                text: 'Hubo un error al guardar la sede',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function listarSedes() {
    validarSession();
    obtenerNombreUsuario();

    $.ajax({
        method: "GET",
        url: "ServletSedes?accion=listarSede"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {
            var eliminar = "<a onclick='eliminarSede(" + value.idSede + ")' class='btn btn-danger btn-sm'><i class='fas fa-trash'></i></a>";
            var accion = "<td><a onclick='editarSede(" + value.idSede + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a>" + eliminar + "</td>";
            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_sede + '</td><td>' + value.municipio + '</td><td>' + value.telefono + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });




        console.log(json);



    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}

function eliminarSede(idSede) {
    Swal.fire({
        title: 'Estas Seguro?',
        text: "No Podras Revertir los Cambios!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, Eliminar!'
    }).then((result) => {
        if (result.isConfirmed) {
            borrarSede(idSede);
            
        }
    });
}

function borrarSede(idSede){
     $.ajax({
        method: "GET",
        url: "ServletSedes?accion=eliminarSede&idSede="+idSede

    }).done(function (data) {
        var datos = data;
        
        if (datos > 0) {
               
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Sede Eliminada Exitosamente',
                    showConfirmButton: false,
                    timer: 2000
                });
                setTimeout(recargarPagina, 2000);
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Eliminar la Sede',
                    text: 'Hubo un error al Actualizar la sede',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }

    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
    
    
    
}

function editarSede(idSede) {
    $.ajax({
        method: "GET",
        url: "ServletSedes?accion=obtenerSedeById&idSede=" + idSede

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        console.log(json);

        document.getElementById('IdSedeModal').value = json.idSede;
        document.getElementById('txtNombreSede').value = json.nombre_sede;
        document.getElementById('Municipio').value = json.municipio;
        document.getElementById('Telefono').value = json.telefono;
        if (json.datper === undefined) {
            document.getElementById('txtDato').value = "";
        } else {
            document.getElementById('txtDato').value = json.datper;
        }

        $('#modalEditarSede').modal('show');


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}

function actualizarSede() {
    var datos = {};
    datos.idSede = document.getElementById('IdSedeModal').value;
    datos.nombre_sede = document.getElementById('txtNombreSede').value;
    datos.municipio = document.getElementById('Municipio').value;
    datos.telefono = document.getElementById('Telefono').value;
    var datoPerso = document.getElementById('txtDato').value;

    if (datoPerso !== "") {
        datos.datoPer = document.getElementById('txtDato').value;
    }
    if (datos.nombre_sede === "" || datos.municipio === "" || datos.telefono === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error al Guardar la Sede',
            text: 'Campos Vacios',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {
        $.ajax({
            method: "POST",
            url: "ServletSedes?accion=actualizarSede",
            data: datos,
            dataType: 'JSON'

        }).done(function (data) {
            var datos = data;
            if (datos > 0) {
                $('#modalEditarSede').modal('hide');
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Sede Actualizada Exitosamente',
                    showConfirmButton: false,
                    timer: 2000
                });
                setTimeout(recargarPagina, 2000);
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Guardar la Sede',
                    text: 'Hubo un error al Actualizar la sede',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }

        }).fail(function () {

            window.location.replace("login.html");
        }).always(function () {

        });
    }

}

function recargarPagina() {
    window.location.reload();
}





