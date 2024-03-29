
/* global Swal */

function cargarDatosContabilidad() {
    validarSession();
    cargarEstados('sltEstadoConsignacionContabilidad');
    listarConsignacionesContabilidad();
    obtenerNombreUsuario();

}



function listarConsignacionesContabilidad() {
    validarSession();
    var valor = "Pendiente";
    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarConsignacionesByEstado&estado=" + valor

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);



        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {
            var devolver = '<a href="#" id="btn_devolver' + value.idConsignacion + '" onclick="abrirModal(' + value.idConsignacion + ', this.id);" class="btn btn-warning btn-sm"><i class="fas fa-backward"></i></a>';
            //var modalDevolver = '<button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#staticBackdrop"><i class="fas fa-backward"></i></button>';
            var obser = '<a href="#" id="btn_observa' + value.idConsignacion + '" onclick="abrirModalObservacionesContabilidad(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
            var imagen = '<a href="#" id="btn_image' + value.idConsignacion + '" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
            var comprobar = '<td><a href="#" id="btn_comprobar' + value.idConsignacion + '" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-check"></i></a>' + devolver + obser + imagen + '</td>';

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.num_recibo + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + '</tr>');
            contador = contador + 1;
        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function abrirModalImagen(idConsignacion) {
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=obtenerRutaImagen&idConsignacion=" + idConsignacion

    }).done(function (data) {
        var datos = data;

        var imagen = document.getElementById('imagenConsigancion');
        imagen.src = datos;
        $('#staticBackdropImage').modal('show');
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}




function abrirModal(idConsignacion, id) {

    $('#staticBackdrop').modal('show');
    document.getElementById('idConsignacion').value = idConsignacion;

}

var cerrar = document.getElementById('cerrarModal');
cerrar.addEventListener('click', (event)=>{
   
   document.getElementById('observacionDevolucion').value = " ";
});



var enviar = document.getElementById('enviarObservacion');
enviar.addEventListener("click", function () {
    var observa = document.getElementById('observacionDevolucion').value;
    var id_consignacion = document.getElementById('idConsignacion').value;
    
    if (observa === "") {
        Swal.fire({
            icon: 'error',
            title: 'El Campo de Observacion esta vacio',
            text: 'Ingrese una Observacion valida',
            footer: '<a href="">Why do I have this issue?</a>'

        });
        observa.focus();
    } else {

        var datos = {};
        datos.idConsignacion = id_consignacion;
        datos.observacion = observa;
        
        $.ajax({
            method: "POST",
            url: "ServletControladorConsignaciones?accion=ConsignacionTemporalDevolver",
            data: datos,
            dataType: 'JSON'


        }).done(function (data) {
            var resp = data;


            if (resp > 0) {

                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Consignacion Devuelta Correctamente',
                    showConfirmButton: false,
                    timer: 2000
                });
                document.getElementById('observacionDevolucion').value = "";
                $('#staticBackdrop').modal('hide');

                $("#btn_devolver" + id_consignacion).empty();
                document.getElementById('btn_devolver'+id_consignacion).outerHTML = '<a href="#"  class="btn btn-warning btn-sm" ><i class="fas fa-ban"></i></a></td>';

                var botonGroup = '<a href="#" class="btn btn-primary" onclick="guardarCambios();">Guardar Cambios</a> <a href="#" class="btn btn-danger" onclick="cancelarCambios();">Cancelar Cambios</a>';
                document.getElementById('btn_group').innerHTML = botonGroup;
                




            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al Devolver la Consignacion',
                    text: 'Error Desconocido Reporte el Error',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
                $('#staticBackdrop').modal('hide');
                setTimeout(recargarPagina, 2000);

            }




        }).fail(function () {

            window.location.replace("login.html");
        }).always(function () {

        });


        document.getElementById('sltEstadoConsignacionContabilidad').disabled = true;
        document.getElementById('txtCedula').disabled = true;
        $("#btn_comprobar" + id_consignacion).empty();
        document.getElementById('btn_comprobar' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-primary btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';
        $("#btn_observa" + id_consignacion).empty();
        document.getElementById('btn_observa' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-info btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';
        $("#btn_image" + id_consignacion).empty();
        document.getElementById('btn_image' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-success btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';


    }

});



var select = document.getElementById('sltEstadoConsignacionContabilidad');

select.addEventListener('change', (event) => {
    validarSession();
    event.preventDefault();
    var valor = document.getElementById('sltEstadoConsignacionContabilidad').value;


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
                var imagen = '<a href="#" id="btn_image' + value.idConsignacion + '" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                var obser = '<td><a href="#" id="btn_observa' + value.idConsignacion + '" onclick="abrirModalObservacionesContabilidad(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + imagen + '</td>';
                //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.num_recibo + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                contador = contador + 1;
            } else {
                var devolver = '<a href="#" id="btn_devolver' + value.idConsignacion + '" onclick="abrirModal(' + value.idConsignacion + ');" class="btn btn-warning btn-sm"><i class="fas fa-backward"></i></a>';
                var obser = '<a href="#" id="btn_observa' + value.idConsignacion + '" onclick="abrirModalObservacionesContabilidad(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                var imagen = '<a href="#" id="btn_image' + value.idConsignacion + '" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                var comprobar = '<td><a href="#" id="btn_comprobar' + value.idConsignacion + '" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-check"></i></a>' + devolver + obser + imagen + '</td>';

                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.num_recibo + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + '</tr>');
                contador = contador + 1;
            }

        });




        console.log(json);


    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

});


function consignacionesByCedula() {
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
                    var imagen = '<a href="#" id="btn_image' + value.idConsignacion + '" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                    var obser = '<td><a href="#" id="btn_observa' + value.idConsignacion + '" onclick="abrirModalObservacionesContabilidad(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + imagen + '</td>';

                    //var accion = '<td><a href="#" onclick=""  class="btn btn-primary btn-sm disabled" ><i class="fas fa-check"></i></a>' + obser + '</td>';
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.num_recibo + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                    contador = contador + 1;
                } else {
                    var devolver = '<a href="#" id="btn_devolver' + value.idConsignacion + '" onclick="abrirModal(' + value.idConsignacion + ');" class="btn btn-warning btn-sm"><i class="fas fa-backward"></i></a>';
                    var obser = '<a href="#" id="btn_observa' + value.idConsignacion + '" onclick="abrirModalObservacionesContabilidad(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                    var imagen = '<a href="#" id="btn_image' + value.idConsignacion + '" onclick="abrirModalImagen(' + value.idConsignacion + ')" class="btn btn-success btn-sm"><i class="fas fa-image"></i></a>';
                    var comprobar = '<td><a href="#" id="btn_comprobar' + value.idConsignacion + '" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-check"></i></a>' + devolver + obser + imagen + '</td>';

                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.num_recibo + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + '</tr>');
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

            listarConsignacionesContabilidad();
        }




    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}

function comprobarConsignacion(id_consignacion) {
    validarSession();
    var datos = {};
    datos.idConsignacion = id_consignacion;


    $("#btn_comprobar" + id_consignacion).empty();
    document.getElementById('btn_comprobar' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-primary btn-sm disabled" ><i class="fas fa-ban"></i></a>';

    $.ajax({
        method: "POST",
        url: "ServletControladorConsignaciones?accion=ConsignacionTemporal",
        data: datos,
        dataType: 'JSON'
    }).done(function (data) {

        var json = data;




        if (json > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Consignacion Comprobada Correctamente',
                showConfirmButton: false,
                timer: 2000
            });
            document.getElementById('sltEstadoConsignacionContabilidad').disabled = true;
            document.getElementById('txtCedula').disabled = true;
            $("#btn_devolver" + id_consignacion).empty();
            document.getElementById('btn_devolver' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-warning btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';
            $("#btn_observa" + id_consignacion).empty();
            document.getElementById('btn_observa' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-info btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';
            $("#btn_image" + id_consignacion).empty();
            document.getElementById('btn_image' + id_consignacion).outerHTML = '<a href="#"  class="btn btn-success btn-sm disabled" ><i class="fas fa-ban"></i></a></td>';


        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Comprobar la Consignacion',
                text: 'Error Desconocido Reporte el Error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        var botonGroup = '<a href="#" class="btn btn-primary" onclick="guardarCambios();">Guardar Cambios</a> <a href="#" class="btn btn-danger" onclick="cancelarCambios();">Cancelar Cambios</a>';
        document.getElementById('btn_group').innerHTML = botonGroup;


        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });


}




function cancelarCambios() {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=cancelarCambios"

    }).done(function (data) {

        var json = data;




        if (json > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Cambios Cancelados Correctamente',
                showConfirmButton: false,
                timer: 2000
            });


        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Cancelar los Cambios',
                text: 'Error Desconocido Reporte el Error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        setTimeout(recargarPagina, 2000);



        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function guardarCambios() {
    validarSession();
    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=guardarCambios"

    }).done(function (data) {

        var json = data;




        if (json > 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Consignacion Comprobada Correctamente',
                showConfirmButton: false,
                timer: 2000
            });



        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al Comprobar la Consignacion',
                text: 'Error Desconocido Reporte el Error',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }

        setTimeout(recargarPagina, 2000);



        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function imprimirReporte(nombre) {
    validarSession();
    Swal.fire({
        title: 'Deseas Imprimir el Reporte?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Imprimir Reporte!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                method: "GET",
                url: "ServletControladorFiles?accion=imprimirReporte&name=" + nombre

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
                    $('#staticBackdropObser').modal('hide');


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
    });

}


function  abrirModalObservacionesContabilidad(id_consignacion) {
    validarSession();

    $('#staticBackdropObserContabilidad').modal('show');

    traerObservaciones(id_consignacion);


    var enviar = document.getElementById('enviarObservacionCon').addEventListener("click", function () {
        observacionesConsignacion(id_consignacion);
    });

}

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
                $('#staticBackdropObserContabilidad').modal('hide');


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


function recargarPagina() {
    window.location.reload();
}

