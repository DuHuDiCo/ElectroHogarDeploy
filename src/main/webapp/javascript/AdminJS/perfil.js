

/* global Swal */

function traerDatosPerfil() {
    validarSession();
    obtenerNombreUsuario();
    $.ajax({
        method: "GET",
        url: "ServletControlador?accion=traerDatosPerfil"


    }).done(function (data) {

        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        document.getElementById("nombreUsuarioPerfil").value = json.nombre_usuario;
        document.getElementById("emailperfil").value = json.email;
        document.getElementById("numeroCedulaPerfil").value = json.n_documento;

        document.getElementById("telefonoPerfil").value = json.telefonoUser;



        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function editarPerfil() {
    validarSession();
    document.getElementById("nombreUsuarioPerfil").disabled = false;
    document.getElementById("emailperfil").disabled = false;
    document.getElementById("passwordPerfil").disabled = false;
    document.getElementById("telefonoPerfil").disabled = false;
    document.getElementById("RepetirPasswordPerfil").disabled = false;
    document.getElementById("numeroCedulaPerfil").disabled = false;

    document.getElementById("actualizarPerfil").style.display = "block";
    document.getElementById("RepetirPasswordPerfil").style.display = "block";
    document.getElementById("labelRepetirPasswordPerfil").style.display = "block";


}


function DatosPerfil() {
    validarSession();
    var datos = {};
    datos.nombre = document.getElementById("nombreUsuarioPerfil").value;
    datos.n_documento = document.getElementById("numeroCedulaPerfil").value;
    datos.email = document.getElementById("emailperfil").value;
    datos.telefono = document.getElementById("telefonoPerfil").value;

    if (document.getElementById("passwordPerfil").value === "") {
        actualizarPerfil(datos);
    } else {
        if (document.getElementById("passwordPerfil").value !== document.getElementById("RepetirPasswordPerfil").value) {
            Swal.fire({
                icon: 'error',
                title: 'Error al Actualizar Perfil',
                text: 'Contraseñas no son Identicas',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        } else {
            var pass = hex_sha1(document.getElementById("passwordPerfil").value);
            datos.password = pass;
            actualizarPerfil(datos);
        }
    }



}

function actualizarPerfil(datos) {
    validarSession();
    $.ajax({
        method: "POST",
        url: "ServletControlador?accion=actualizarPerfil",
        data: datos,
        dataType: 'JSON'


    }).done(function (data) {

        var datos = data;

        if (datos > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Actualizacion Exitosa',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(recargarPagina, 2000);
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Actualizar Perfil',
                text: 'Error Desconocido, Reportelo',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }


        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function recargarPagina() {
    window.location.reload();
}