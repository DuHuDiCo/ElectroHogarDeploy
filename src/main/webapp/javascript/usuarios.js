/* global Swal */

function registrarUsuario() {
    validarSession();
    var datos = {};

    var pass = document.getElementById('password').value;
    var passx2 = document.getElementById('RepetirPassword').value;


    datos.nombre = document.getElementById('nombreUsuario').value;
    datos.Identificacion = document.getElementById('cedulaUsuario').value;
    datos.TipoDocumento = document.querySelector('input[name="tipdoc"]:checked').value;
    datos.Email = document.getElementById('email').value;
    datos.telefono = document.getElementById('telefono').value;
    datos.Rol = document.getElementById('sltRol').value;
    datos.Sede = document.getElementById('sltSede').value;
    datos.password = hex_sha1(pass);
    datos.RepetirPassword = hex_sha1(passx2);

    if (datos.password !== datos.RepetirPassword) {
        Swal.fire({
            icon: 'error',
            title: 'Error al Iniciar Sesion',
            text: 'Las Contraseñas no Coinciden',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {
        $.ajax({
            method: "POST",
            url: "ServletUsuarios?accion=registrarUsuario",
            data: datos,
            dataType: 'JSON'
        }).done(function (data) {
            var respues = data;


            if (respues > 0) {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Usuario Creado Con Exito',
                    showConfirmButton: false,
                    timer: 2000
                });
                setTimeout(recagar, 2000);
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Iniciar Sesion',
                    text: 'Usuario o Contreseña Incorrectos',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }


            // imprimimos la respuesta
        }).fail(function () {
            Swal.fire({
                icon: 'error',
                title: 'Error al Iniciar Sesion',
                text: 'Error Inesperado, Intente Nuevamente o Reporte el Error',
                footer: '<a href="">Why do I have this issue?</a>'
            });

        }).always(function () {

        });
    }
}

function cargarPagUsuarios() {

    validarSession();
    cargarRoles();
    cargarSedes('sltSede');
    obtenerNombreUsuario();



}

function cargarRoles() {
    validarSession();
    event.preventDefault();

    var admin = "Administrador";

    var Sadmin = "Super Administrador";

    $.ajax({
        method: "GET",
        url: "ServletRol?accion=listarRol"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $.each(json, function (key, value) {
            if (value.nombre_rol !== admin) {
                if (value.nombre_rol !== Sadmin) {
                    $("#sltRol").append('<option value="' + value.id_rol + '" >' + value.nombre_rol + '</option>');
                }

            }




        });

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });


}

function cargarSedes(id) {
    validarSession();
    event.preventDefault();
    $.ajax({
        method: "GET",
        url: "ServletSedes?accion=listarSede"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $.each(json, function (key, value) {
            $("#" + id).append('<option value="' + value.idSede + '" >' + value.nombre_sede + '</option>');
        });

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}


function obtenerRol() {
    validarSession();
    var rol = $.ajax({
        method: "GET",
        url: "ServletRol?accion=obtenerRol",
        async: false
    });

    return rol.responseText;
}


function listarUsuarios() {
    validarSession();
    obtenerNombreUsuario();


    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=listarUsuarios"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        $("#dataTable tbody").empty();
        var contador = 1;

        $.each(json, function (key, value) {
            if (value.status === 1 && value.estado_conexion === "Conectado") {
                var accion = "<a onclick='editarUsuario(" + value.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + value.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_usuario + '</td><td>' + value.email + '</td><td>' + value.n_documento + '</td><td>' + value.telefonoUser + '</td><td>' + "<i class='fas fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + value.ultima_sesion + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_rol + '</td><td>' + accion + '</td></tr>');
                contador++;
            } else {
                if (value.status === 1 && value.estado_conexion === "Desconectado") {
                    var accion = "<a onclick='editarUsuario(" + value.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + value.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_usuario + '</td><td>' + value.email + '</td><td>' + value.n_documento + '</td><td>' + value.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + value.ultima_sesion + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_rol + '</td><td>' + accion + '</td></tr>');
                    contador++;
                } else {
                    if (value.status === 0) {
                        var accion = "<a onclick='activarUsuario(" + value.idUsuario + ")' class='btn btn-success btn-sm'><i class='fas fa-check'></i></a>";
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_usuario + '</td><td>' + value.email + '</td><td>' + value.n_documento + '</td><td>' + value.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + value.ultima_sesion + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_rol + '</td><td>' + accion + '</td></tr>');
                        contador++;
                    } else {
                        var accion = "<a onclick='editarUsuario(" + value.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + value.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_usuario + '</td><td>' + value.email + '</td><td>' + value.n_documento + '</td><td>' + value.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + value.ultima_sesion + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_rol + '</td><td>' + accion + '</td></tr>');
                        contador++;
                    }

                }

            }

        });

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });

}


function editarUsuario(idUsuario) {

    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=obtenerUsuarioById&idUsuario=" + idUsuario

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);

        document.getElementById('IdUsuarioModal').value = json.idUsuario;
        document.getElementById('txtNombreUsuario').value = json.nombre_usuario;
        document.getElementById('TxtEmailusuario').value = json.email;
        document.getElementById('TxtDocumento').value = json.n_documento;
        document.getElementById('txtTelefono').value = json.telefonoUser;




        selectEditarUsuarioSede("sltSede", json.nombre_sede);
        selectEditarUsuarioRol("sltRol", json.nombre_rol);
        $('#modalEditarUsuario').modal('show');

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });


}

function actualizarUsuarios() {
    validarSession();
    var datos = {};
    datos.idUsuario = document.getElementById('IdUsuarioModal').value;
    datos.nombre = document.getElementById('txtNombreUsuario').value;
    datos.email = document.getElementById('TxtEmailusuario').value;
    datos.documento = document.getElementById('TxtDocumento').value;
    datos.telefono = document.getElementById('txtTelefono').value;
    datos.sede = document.getElementById('sltSede').value;
    datos.rol = document.getElementById('sltRol').value;



    $.ajax({
        method: "POST",
        url: "ServletUsuarios?accion=actualizarUsuario",
        data: datos,
        dataType: 'JSON'

    }).done(function (data) {
        var datos = data;
        if (datos > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Usuario Actualizado Con Exito',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(recagar, 2000);
            $('#modalEditarUsuario').modal('show');
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Actualizar Usuario',
                text: 'No Se Logro Actualizar el Usuario',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }


    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}


function selectEditarUsuarioSede(id, sede) {


    $.ajax({
        method: "GET",
        url: "ServletSedes?accion=listarSede"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        $("#sltSede").empty();

        $.each(json, function (key, value) {
            if (sede === value.nombre_sede) {
                $("#" + id).append('<option value="' + value.idSede + '" selected>' + value.nombre_sede + '</option>');
            } else {
                $("#" + id).append('<option value="' + value.idSede + '" >' + value.nombre_sede + '</option>');
            }

        });

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}

function selectEditarUsuarioRol(id, rol) {
    $.ajax({
        method: "GET",
        url: "ServletRol?accion=listarRol"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        $("#sltRol").empty();

        $.each(json, function (key, value) {
            if (rol === value.nombre_rol) {
                if (value.nombre_rol !== 'Super Administrador') {
                    $("#" + id).append('<option value="' + value.id_rol + '" selected>' + value.nombre_rol + '</option>');
                }

            } else {
                if (value.nombre_rol !== 'Super Administrador') {
                    $("#" + id).append('<option value="' + value.id_rol + '" >' + value.nombre_rol + '</option>');
                }
            }

        });

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}


function activarUsuario(idUsuario) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=activarUsuario&idUsuario=" + idUsuario

    }).done(function (data) {
        var datos = data;
        if (datos > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Usuario activado Con Exito',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(recagar, 2000);
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al activar Usuario',
                text: 'Usuario o Contreseña Incorrectos',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}


function desactivarUsuario(idUsuario) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=desactivarUsuario&idUsuario=" + idUsuario

    }).done(function (data) {
        var datos = data;
        if (datos > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Usuario Desactivado Con Exito',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(recagar, 2000);
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Desactivar Usuario',
                text: 'Usuario o Contreseña Incorrectos',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });


}

function usuariosCedulaAdmin() {
    validarSession();

    var buscar = document.getElementById('txtBuscar').value;
    if (buscar === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error al Filtrar Usuario',
            text: 'Campo Vacio',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {


        $.ajax({
            method: "GET",
            url: "ServletUsuarios?accion=usuarioByCedula&Dato=" + buscar

        }).done(function (data) {
            var datos = JSON.stringify(data);
            var json = JSON.parse(datos);
            var contador = 1;
            $("#dataTable tbody").empty();
            if (json.error === "null") {
                usuariosNombreAdmin(buscar);
            } else {
                if (json.status === 1 && json.estado_conexion === "Conectado") {
                    var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='fas fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                    contador++;
                } else {
                    if (json.status === 1 && json.estado_conexion === "Desconectado") {
                        var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                        contador++;
                    } else {
                        if (json.status === 0) {
                            var accion = "<a onclick='activarUsuario(" + json.idUsuario + ")' class='btn btn-success btn-sm'><i class='fas fa-check'></i></a>";
                            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                            contador++;
                        } else {
                            var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                            contador++;
                        }

                    }

                }
            }




        }).fail(function () {
            window.location.replace("login.html");
        }).always(function () {
        });
    }
}

function usuariosNombreAdmin(buscar) {

    $.ajax({
        method: "GET",
        url: "ServletUsuarios?accion=usuarioByNombre&Dato=" + buscar

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var contador = 1;
        $("#dataTable tbody").empty();

        if (json.error === "null") {
            Swal.fire({
                icon: 'error',
                title: 'Error al Encontrar Usuario',
                text: 'Usuario No Existe',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        } else {
            if (json.status === 1 && json.estado_conexion === "Conectado") {
                var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='fas fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                contador++;
            } else {
                if (json.status === 1 && json.estado_conexion === "Desconectado") {
                    var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-check'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                    contador++;
                } else {
                    if (json.status === 0) {
                        var accion = "<a onclick='activarUsuario(" + json.idUsuario + ")' class='btn btn-success btn-sm'><i class='fas fa-check'></i></a>";
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                        contador++;
                    } else {
                        var accion = "<a onclick='editarUsuario(" + json.idUsuario + ")' class='btn btn-primary btn-sm'><i class='fas fa-pen'></i></a><a onclick='desactivarUsuario(" + json.idUsuario + ")' class='btn btn-danger btn-sm'><i class='fas fa-ban'></i></a>";
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + json.nombre_usuario + '</td><td>' + json.email + '</td><td>' + json.n_documento + '</td><td>' + json.telefonoUser + '</td><td>' + "<i class='far fa-circle'></i>" + '</td><td>' + "<i class='fas fa-times'></i>" + '</td><td>' + json.ultima_sesion + '</td><td>' + json.nombre_sede + '</td><td>' + json.nombre_rol + '</td><td>' + accion + '</td></tr>');
                        contador++;
                    }

                }

            }
        }




    }).fail(function () {
        window.location.replace("login.html");
    }).always(function () {
    });
}


function recagar() {
    window.location.reload();
}