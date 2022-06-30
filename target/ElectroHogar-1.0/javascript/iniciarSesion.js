/* global resp,respSesion, Swal  */
$('#txtPassword').keypress(function (e){
    
    if(e.keyCode === 13){
        
        $("#btnIniciar").click();
    }
    
});

function iniciarSesion() {

    var datos = {};
    var passSinEncriptar = document.getElementById('txtPassword').value;

    datos.email = document.getElementById('txtEmail').value;
    datos.password = hex_sha1(passSinEncriptar);
    

    $.ajax({
        method: "POST",
        url: "ServletControlador?accion=iniciarSesion",
        data: datos,
        dataType: 'JSON'
    }).done(function (data) {

        var json = JSON.stringify(data);
        var datos = JSON.parse(json);



        if (datos.nombre_rol !== "null") {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Inicio Exitoso',
                showConfirmButton: false,
                timer: 2000
            });
            
            cargarPagina(datos.nombre_rol);
            

            
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Iniciar Sesion',
                text: 'Usuario o Contraseña Invalidos',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function cerrarSesion() {
    Swal.fire({
        title: 'Estas Seguro?',
        text: "No podras revertir esto.!",
        icon: 'Advertencia',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, Cerrar Sesion!'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminarSession();
            
        }
    });
}

function eliminarSession() {
    $.ajax({
        url: "ServletControlador?accion=cerrarSesion"

    }).done(function (data) {

        var resp = data;
        
        if(resp === "null"){
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Sesion Cerrada',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(redireccion, 2000);
        }



        // imprimimos la respuesta
    }).fail(function () {

    }).always(function () {


    });
}

function redireccion(){
    window.location.replace("login.html");
}

function cargarPagina(datos) {
     roles(datos);
//    window.onload = function (datos) {
//        roles(datos);
//    };
}

function obtenerSesion() {


    event.preventDefault();

    $.ajax({
        url: "ServletControlador?accion=sesion"

    }).done(function (data) {

        var respSesion = data;



        if (respSesion === "null") {

            window.location.replace("login.html");

        }

        // imprimimos la respuesta
    }).fail(function () {

        window.location.reload();
    }).always(function () {


    });


    // simulamos tiempo de carga

}



function roles(datos) {
    
    switch (datos) {
        case "Administrador":
            window.location.replace("inicioAdmin.html");
            break;

        case "Cartera":
            window.location.replace("inicioCartera.html");
            break;
        case "Contabilidad":
            window.location.replace("inicioContabilidad.html");
            break;
        case "Caja":
            window.location.replace("inicioCaja.html");
            break;
        default:
            window.location.replace("inicioSuperAdmin.html");
    }

}

function accion(id) {
    
    document.getElementById(id).style.display = "none";
}












