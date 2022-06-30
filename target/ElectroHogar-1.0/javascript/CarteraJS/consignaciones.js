/* global Swal */




$(function () {
    $('.input-file__input').on('change', function () {
        if ($(this)[0].files[0]) {
            $(this).prev().text($(this)[0].files[0].name);
        }
    });
});

const $form = document.querySelector('#formConsignacion');



function abrirModalObservaciones() {
    var recibo = document.getElementById('txtNumRecibo').value;
    var valor = document.getElementById('txtValor').value;
    var fecha = document.getElementById('dateCreacion').value;
    var sede = document.getElementById('sltBancoCartera').value;
    var file = document.getElementById('file').files;
    if (recibo === "" || valor === "" || fecha === "" || sede === "" || file.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error al guardar la consignacion',
            text: 'Alguno de los Campos estan Vacios',
            footer: '<a href="">Why do I have this issue?</a>'
        });
        recibo.focus();
    } else {
        $('#modalConsignacion').modal('show');
    }

}


function crearObservacion() {
    validarSession();
    var obser = document.getElementById('observacionGuardarConsig').value;
    if (obser === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error al guardar la consignacion',
            text: 'Campo de Observacion Vacio, Por Favor Ingrese una Observacion',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {
        guardarConsig();

        var datos = {};

        datos.observacion = obser;


        $.ajax({
            method: "POST",
            url: "ServletObservaciones?accion=guardarObservacion",
            data: datos,
            dataType: 'JSON'

        }).done(function (data) {

            var datos = data;


            window.location.reload();

            if (datos !== 0) {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Consignacion Guardada Exitosamente',
                    showConfirmButton: false,
                    timer: 2000


                });

                roles(datos.nombre_rol);




            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al guardar la consignacion',
                    text: 'No se logro guardar la consignacion, por favor revise bien la informacion o reporte el error',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }

            window.location.reload();


            // imprimimos la respuesta
        }).fail(function () {

            window.location.replace("login.html");
        }).always(function () {

        });

    }
}


function noCrearObservacion() {
    validarSession();
    guardarConsig();
}

function guardarConsig() {
    validarSession();
    var form = document.getElementById('formConsignacion');
    var formData = new FormData(form);


    $.ajax({
        method: "POST",
        url: "ServletControladorCartera?accion=guardarConsignacion",
        data: formData,
        processData: false,
        contentType: false

    }).done(function (data) {

        var datos = data;


        window.location.reload();

        if (datos !== 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Consignacion Guardada Exitosamente',
                showConfirmButton: false,
                timer: 2000


            });

            roles(datos.nombre_rol);




        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al guardar la consignacion',
                text: 'No se logro guardar la consignacion, por favor revise bien la informacion o reporte el error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        window.location.reload();


        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}




function cargarDatos() {
    validarSession();
    obtenerNombreUsuario();


    event.preventDefault();

    $.ajax({
        method: "GET",
        url: "ServletControladorCartera?accion=llenarBanco"

    }).done(function (data) {

        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var html = "";

        $.each(json, function (key, value) {
            if (value.idPlataforma === 1) {
                $("#sltBancoCartera").append('<option value="' + value.idPlataforma + '" >' + value.nombre_plataforma + '--' + value.tipo_pago + '</option>');
            }
            $("#sltBancoCartera").append('<option value="' + value.idPlataforma + '" >' + value.nombre_plataforma + '--' + value.tipo_pago + '</option>');
        });


        cargarEstados('sltEstadoConsignacion');
        cargarConsignacionesGeneral();




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}

function cargarBancos(id, dato) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletControladorCartera?accion=llenarBanco"

    }).done(function (data) {

        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $.each(json, function (key, value) {
            if (value.nombre_plataforma === dato) {
                $("#" + id).append('<option value="' + value.idPlataforma + '" selected>' + value.nombre_plataforma + '--' + value.tipo_pago + '</option>');
            } else {
                $("#" + id).append('<option value="' + value.idPlataforma + '" >' + value.nombre_plataforma + '--' + value.tipo_pago + '</option>');
            }




        });



    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}


function cargarEstados(idSelect) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletControladorEstados?accion=cargarEstados"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var html = "";



        $.each(json, function (key, value) {
            if (value.idEstado === 1) {
                $("#" + idSelect).append('<option value="' + value.nombre_estado + '" selected> ' + value.nombre_estado + '</option>');
            } else {
                $("#" + idSelect).append('<option value="' + value.nombre_estado + '" > ' + value.nombre_estado + '</option>');
            }


        });




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}

var select = document.getElementById('sltEstadoConsignacion');

select.addEventListener('change', (event) => {
    validarSession();
    event.preventDefault();
    var valor = document.getElementById('sltEstadoConsignacion').value;


    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarConsignacionesByEstado&estado=" + valor

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {
            if (value.nombre_estado === "Devuelta") {
                var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCaja(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>" + observa + "</td>";
                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
                contador = contador + 1;
            } else {
                if (value.nombre_estado === "Pendiente") {
                    var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a></td>";
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
                    contador = contador + 1;
                } else {
                    var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCaja(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td><td>' + observa + '</td></tr>');
                    contador = contador + 1;
                }
            }


        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

});



function cargarConsignacionesGeneral() {

    validarSession();

    var rol = document.getElementById('rol').value;

    var valor = "";

    if (rol === 'Caja') {
        valor = "Comprobado";
    } else {
        if (rol === 'Cartera') {
            valor = "Aplicada";
        } else {
            valor = "Pendiente";
        }
    }




    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarConsignacionesByEstado&estado=" + valor

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a></td>";

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });



}

function consignacionesCedula() {
    validarSession();
    var cedula = document.getElementById('txtCedula').value;

    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarConsignacionesByCedula&cedula=" + cedula

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $("#dataTable tbody").empty();

        if (json.length > 0) {
            var contador = 1;

            $.each(json, function (key, value) {

                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td></tr>');
                contador = contador + 1;
            });




            console.log(json);

        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Consultar la Cedula',
                text: 'No existe una Consignacion Relacionada a la Cedula Ingresada',
                footer: '<a href="">Why do I have this issue?</a>'

            });

            cargarConsignacionesGeneral();
        }




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}


function traerCliente() {
    validarSession();
    var cedula = document.getElementById('txtCliente').value;


    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarClienteByCedula&cedula=" + cedula

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);


        $("#tblCliente tbody").empty();


        if (json.length > 0) {
            document.getElementById('nuevoCliente').style.display = "none";

            var contador = 1;

            $.each(json, function (key, value) {

                $("#tblCliente tbody").append('<tr> <td><input type="checkbox" value=' + value.idObligacion + ' id="obligacion" name="obligacion" required></td><td>' + value.nombre_titular + '</td><td>' + value.saldo_capital + '</td><td>' + value.fecha_obligacion + '</td><td>' + value.nombre_sede + '</td></tr>');
                contador = contador + 1;
            });

            document.getElementById('tblCliente').style.display = "block";




            console.log(json);

        } else {
            document.getElementById('tblCliente').style.display = "none";
            document.getElementById('nuevoCliente').style.display = "block";
            document.getElementById('sltSedeCon').style.display = "block";
            document.getElementById('cedulaCliente').style.display = "block";
            cargarSedes('sltSedeCon');
            Swal.fire({
                icon: 'error',
                title: 'El Cliente no Existe',
                text: 'No se encontro un cliente relacionado con el documento ingresado'
            });
        }




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });



}

function editarConsignacion(idConsignacion) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=editarConsignacion&idConsignacion=" + idConsignacion


    }).done(function (data) {

        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);




        if (Object.keys(json).length > 0) {
            $('#modalEditarConsignacion').modal('show');


            document.getElementById('txtIdConModal').value = json.idConsignacion;
            document.getElementById('txtNumReciboModal').value = json.num_recibo;
            document.getElementById('txtValorModal').value = json.valor;
            document.getElementById('dateCreacionModal').value = json.fecha_pago_string;

            $("#tblClienteModal tbody").empty();
            $("#tblClienteModal tbody").append('<tr> <td><input type="checkbox" value=' + json.id_obligacion + ' id="obligacionModal" name="obligacion" required checked></td><td>' + json.nombre_titular + '</td><td>' + json.valor_obligacion + '</td><td>' + json.fecha_obligacion + '</td><td>' + json.nombre_sede + '</td></tr>');

            cargarBancos('sltBancoCarteraModal', json.nombre_plataforma);

        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Editar la consignacion',
                text: 'No se logro Editar la consignacion, por favor reporte el error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function traerClienteModal() {
    validarSession();
    var cedula = document.getElementById('txtClienteModal').value;


    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarClienteByCedula&cedula=" + cedula

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);





        if (Object.keys(json).length > 0) {
            $("#tblClienteModal tbody").empty();

            var contador = 1;

            $.each(json, function (key, value) {

                $("#tblClienteModal tbody").append('<tr> <td><input type="checkbox" value=' + value.idObligacion + ' id="obligacionModal" name="obligacion" required></td><td>' + value.nombre_titular + '</td><td>' + value.saldo_capital + '</td><td>' + value.fecha_obligacion + '</td><td>' + value.nombre_sede + '</td></tr>');
                contador = contador + 1;
            });

            console.log(json);

        } else {

            Swal.fire({
                icon: 'error',
                title: 'El Cliente no Existe',
                text: 'No se encontro un cliente relacionado con el documento ingresado'
            });
        }




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });



}

function actualizarConsignacion() {
    validarSession();
    var datos = {};
    datos.idConsignacion = document.getElementById('txtIdConModal').value;
    datos.num_recibo = document.getElementById('txtNumReciboModal').value;
    datos.valor = document.getElementById('txtValorModal').value;
    datos.fecha_pago = document.getElementById('dateCreacionModal').value;
    datos.id_obligacion = document.getElementById('obligacionModal').value;
    datos.banco = document.getElementById('obligacionModal').value;

    if (datos.num_recibo === "" || datos.valor === "" || datos.fecha_pago === "" || datos.id_obligacion === "" || datos.banco === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error al Actualizar la Consignacion',
            text: 'Existen Campos Vacios',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {
        $.ajax({
            method: "POST",
            url: "ServletControladorConsignaciones2?accion=actualizarConsignaciones",
            data: datos,
            dataType: 'JSON'

        }).done(function (data) {
            var datos = data;

            if (datos > 0) {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Consignacion Actualizada Correctamente',
                    showConfirmButton: false,
                    timer: 2000
                });
                $('#modalEditarConsignacion').modal('hide');
                setTimeout(recargarPaginaCartera, 2000);
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Actualizar la Consignacion',
                    text: 'Error Desconocido Reporte el Error',
                    footer: '<a href="">Why do I have this issue?</a>'
                });

            }




        }).fail(function () {

            window.location.replace("login.html");
        }).always(function () {

        });
    }



}

function recargarPaginaCartera() {
    window.location.reload();
}








