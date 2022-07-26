/* global Swal */
function cargarDatosAdmin() {
    validarSession();
    cargarDatosBanco();
    cargarConsignacionesGeneralAdmin();
    cargarEstados('sltEstadoConsignacion');
    obtenerNombreUsuario();
}

function abrirModalObservaciones() {
    var recibo = document.getElementById('txtNumRecibo').value;
    var valor = document.getElementById('txtValor').value;
    var fecha = document.getElementById('dateCreacion').value;
    var sede = document.getElementById('sltBancoCartera').value;
    var file = document.getElementById('file').files;
    var cliente = null;
    var valid = document.getElementById('validacionVacio').value;


    if (valid !== "") {
        cliente = document.getElementById('obligacion').checked;
    } else {
        cliente = false;
    }
    if (recibo === "" || valor === "" || fecha === "" || sede === "" || file.length === 0 || !cliente) {

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


function cargarConsignacionesGeneralAdmin() {

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
            var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
            var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
            var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>" + observa + imagen + "</td>";

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
            contador = contador + 1;
        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });



}

function cargarDatosBanco() {
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
            } else {
                $("#sltBancoCartera").append('<option value="' + value.idPlataforma + '" >' + value.nombre_plataforma + '--' + value.tipo_pago + '</option>');
            }

        });






    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}

function guardarConsignacionConObservacion() {
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

            var idConsignacion = data;



            if (idConsignacion !== 0) {

                crearObservacion(obser, idConsignacion);





            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al guardar la consignacion',
                    text: 'No se logro guardar la consignacion, por favor revise bien la informacion o reporte el error',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }




            // imprimimos la respuesta
        }).fail(function () {

            window.location.replace("login.html");
        }).always(function () {

        });





    }
}

function crearObservacion(obser, idConsignacion) {
    var datos = {};
    datos.observacion = obser;
    datos.idConsignacion = idConsignacion;

    $.ajax({
        method: "POST",
        url: "ServletObservaciones?accion=guardarObservacion",
        data: datos,
        dataType: 'JSON'

    }).done(function (data) {

        var dato = data;

        if (dato !== 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Consignacion Guardada Exitosamente',
                showConfirmButton: false,
                timer: 2000


            });

            setTimeout(recargarPaginaAdmin, 2000);

        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al guardar la consignacion',
                text: 'No se logro guardar la consignacion, por favor revise bien la informacion o reporte el error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }




        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
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

function  abrirModalObservacionesAdmin(id_consignacion) {
    validarSession();

    $('#staticBackdropObserAdmin').modal('show');

    traerObservaciones(id_consignacion);
    document.getElementById('idConsignacion').value = id_consignacion;

}

var enviar = document.getElementById('enviarObservacionCon').addEventListener("click", function () {
    var id_consignacion = document.getElementById('idConsignacion').value;
    observacionesConsignacion(id_consignacion);
});

function observacionesConsignacion(id_consignacion) {
    validarSession();
    var txtObservacion = document.getElementById('txtObservacion').value;

    if (txtObservacion === "") {
        Swal.fire({
            icon: 'error',
            title: 'Error al Guardar la Observacion',
            text: 'El Campo de Observacion se Encuentra Vacio',
            footer: '<a href="">Why do I have this issue?</a>'
        });
    } else {

        var datos = {};
        datos.observacion = txtObservacion;
        datos.idConsignacion = id_consignacion;

        $.ajax({
            method: "POST",
            url: "ServletObservaciones?accion=nuevaObservacion",
            data: datos,
            dataType: 'JSON'
        }).done(function (data) {

            var json = data;

            if (json !== 0) {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Observacion Guardada Correctamente',
                    showConfirmButton: false,
                    timer: 2000
                });
                document.getElementById('txtObservacion').value = "";
                $('#staticBackdropObserAdmin').modal('hide');


            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Guardar la Observacion',
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


function traerObservaciones(idConsignacion) {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletObservaciones?accion=obtenerObservaciones&idConsignacion=" + idConsignacion

    }).done(function (data) {

        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        $("#tableObservaciones tbody").empty();

        var contador = 1;


        if (Object.keys(json).length > 0) {
            $.each(json, function (key, value) {

                $("#tableObservaciones").append('<tr> <td>' + contador + '</td><td>' + value.observacion + '</td><td>' + value.fecha_observacion + '</td><td>' + value.nombre_usuario + '</td></tr>');
                contador = contador + 1;

            });

        } else {
            Swal.fire({
                icon: 'error',
                title: 'No Existen Observaciones',
                text: 'No Existen Observaciones en esta Consignacion',
                footer: '<a href="">Why do I have this issue?</a>'
            });

        }

        // imprimimos la respuesta
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
            if (valor !== 'Pendiente') {
                if (valor === 'Devuelta') {
                    var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                    var editar = "<a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>";
                    var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + editar + imagen + '</td>';
                    //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                    contador = contador + 1;
                    document.getElementById('btnCancelarConsignacion').style.display = "block";
                    document.getElementById('nuevoEstado').value = "Pendiente";
                } else {
                    var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                    var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + imagen + '</td>';
                    //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                    contador = contador + 1;
                }


            } else {
                var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                var obser = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                var comprobar = '<td><a href="#" id="btn_comprobar" onclick="editarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-pen"></i></a>' + obser + imagen + '</td>';

                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + '</tr>');
                contador = contador + 1;
            }

        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

});


function consignacionesCedulaAdmin() {

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
                if (value.nombre_estado !== 'Pendiente') {
                    if (value.nombre_estado === 'Devuelta') {
                        var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                        var editar = "<a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>";
                        var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + editar + imagen + '</td>';
                        //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                        contador = contador + 1;
                    } else {
                        var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                        var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a></td>';
                        //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + imagen + '</tr>');
                        contador = contador + 1;
                    }

                } else {
                    var imagen = '<a href="#" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                    var obser = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesAdmin(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                    var comprobar = '<td><a href="#" id="btn_comprobar" onclick="editarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-pen"></i></a>' + obser + '</td>';

                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + imagen + '</tr>');
                    contador = contador + 1;
                }


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

function recargarPaginaAdmin() {
    window.location.reload();
}

