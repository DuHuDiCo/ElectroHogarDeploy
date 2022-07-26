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
    var cliente = document.getElementById('obligacionModal').checked;

    var file = document.getElementById('file').files;
    if (recibo === "" || valor === "" || fecha === "" || sede === "" || file.length === 0 || !cliente) {
        alert(cliente);
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

            setTimeout(recargarPaginaCartera, 2000);

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




        if (datos !== 0) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Consignacion Guardada Exitosamente',
                showConfirmButton: false,
                timer: 2000


            });

            setTimeout(recargarPaginaCartera, 2000);




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
            if (value.idPlataforma === dato) {
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
                var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>" + observa + "</td>";
                $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
                contador = contador + 1;
                document.getElementById('btnCancelarConsignacion').style.display = "block";
                document.getElementById('nuevoEstado').value = "Pendiente";
            } else {
                if (value.nombre_estado === "Pendiente") {
                    var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                    var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>" + observa + "</td>";
                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + accion + '</tr>');
                    contador = contador + 1;
                } else {
                    var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
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
            var observa = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
            var accion = "<td><a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>" + observa + "</td>";

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
                if (value.nombre_estado !== 'Pendiente') {
                    if (value.nombre_estado === 'Devuelta') {
                        var editar = "<a href='#' class='btn btn-primary btn-sm' onclick='editarConsignacion(" + value.idConsignacion + ")'><i class='fas fa-pen'></i></a>";
                        var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>' + editar + '</td>';
                        //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                        contador = contador + 1;
                    } else {
                        var obser = '<td><a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a></td>';
                        //var comprobar = '<td><a href="#" id="btn_comprobar" onclick="comprobarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm" disabled><i class="fas fa-check"></i></a>' +  obser + '</td>';
                        $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + obser + '</tr>');
                        contador = contador + 1;
                    }

                } else {

                    var obser = '<a href="#" id="btn_observa" onclick="abrirModalObservacionesCartera(' + value.idConsignacion + ');" class="btn btn-info btn-sm"><i class="fas fa-eye"></i></a>';
                    var comprobar = '<td><a href="#" id="btn_comprobar" onclick="editarConsignacion(' + value.idConsignacion + ');" class="btn btn-primary btn-sm"><i class="fas fa-pen"></i></a>' + obser + '</td>';

                    $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.num_recibo + '</td><td>' + value.nombre_titular + '</td><td>' + value.fecha_pago + '</td><td>' + value.fecha_creacion + '</td><td>' + value.valor + '</td><td>' + value.nombre_estado + '</td><td>' + value.nombre_sede + '</td><td>' + value.nombre_plataforma + '</td>' + comprobar + '</tr>');
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


function traerCliente() {
    validarSession();
    var cedula = document.getElementById('txtCliente').value;
    document.getElementById('validacionVacio').value = cedula;
    


    $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=listarClienteByCedula&cedula=" + cedula

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        document.getElementById('nuevoCliente').style.display = "none";
        document.getElementById('cedulaCliente').style.display = "none";
        document.getElementById('sltSedeCon').style.display = "none";

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
        console.log(json);



        if (Object.keys(json).length > 0) {
            $('#modalEditarConsignacion').modal('show');


            document.getElementById('txtIdConModal').value = json.idConsignacion;
            document.getElementById('txtNumReciboModal').value = json.num_recibo;
            document.getElementById('txtValorModal').value = json.valor;
            document.getElementById('dateCreacionModal').value = json.fecha_pago_string;

            $("#tblClienteModal tbody").empty();
            $("#tblClienteModal tbody").append('<tr> <td><input type="checkbox" value=' + json.id_obligacion + ' id="obligacionModal" name="obligacion" required checked></td><td>' + json.nombre_titular + '</td><td>' + json.valor_obligacion + '</td><td>' + json.fecha_obligacion + '</td><td>' + json.nombre_sede + '</td></tr>');
            $("#sltBancoCarteraModal").empty();

            cargarBancos('sltBancoCarteraModal', json.id_plataforma);


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
    var nuevoEstado = document.getElementById("nuevoEstado").value;
    var valid = document.getElementById('obligacionModal').checked;
    if (nuevoEstado !== "") {
        datos.idConsignacion = document.getElementById('txtIdConModal').value;
        datos.num_recibo = document.getElementById('txtNumReciboModal').value;
        datos.valor = document.getElementById('txtValorModal').value;
        datos.fecha_pago = document.getElementById('dateCreacionModal').value;
        datos.id_obligacion = document.getElementById('obligacionModal').value;
        datos.banco = document.getElementById('sltBancoCarteraModal').value;
        datos.estado = nuevoEstado;
    } else {
        datos.idConsignacion = document.getElementById('txtIdConModal').value;
        datos.num_recibo = document.getElementById('txtNumReciboModal').value;
        datos.valor = document.getElementById('txtValorModal').value;
        datos.fecha_pago = document.getElementById('dateCreacionModal').value;
        datos.id_obligacion = document.getElementById('obligacionModal').value;
        datos.banco = document.getElementById('sltBancoCarteraModal').value;
    }
    



    if (datos.num_recibo === "" || datos.valor === "" || datos.fecha_pago === "" || datos.id_obligacion === "" || datos.banco === "" || !valid) {
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

function  abrirModalObservacionesCartera(id_consignacion) {
    validarSession();

    $('#staticBackdropObserCartera').modal('show');

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
                $('#staticBackdropObserCartera').modal('hide');


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

function generarReporte() {
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=generarReporte"

    }).done(function (data) {
        var datos = data;
        if (datos > 0) {
            if (datos === "Error") {
                Swal.fire({
                    icon: 'error',
                    title: 'Reporte No Generado',
                    text: 'Error al Generar el Reporte',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            } else {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Reporte Generado Correctamente',
                    showConfirmButton: false,
                    timer: 2000
                });
            }

            setTimeout(recargarPaginaCartera, 2000);

        } else {
            if (datos === "null") {
                Swal.fire({
                    icon: 'error',
                    title: 'Reporte No Generado',
                    text: 'No Existen Consignaciones Recientemente Creadas',
                    footer: '<a href="">Why do I have this issue?</a>'
                });
            }

        }
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function cancelarConsignacion() {
    var idConsignacion = document.getElementById("txtIdConModal").value;
    Swal.fire({
        title: 'Estas Seguro?',
        text: "No Podras Revertir los Cambios!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, Cancelar Consignacion!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                method: "GET",
                url: "ServletControladorConsignaciones2?accion=cancelarConsignacion&idConsignacion=" + idConsignacion

            }).done(function (data) {
                var datos = data;
                if (datos > 0) {
                    $('#modalEditarConsignacion').modal('hide');
                    Swal.fire({
                        position: 'top-end',
                        icon: 'success',
                        title: 'Consignacion Cancelada Correctamente',
                        showConfirmButton: false,
                        timer: 2000
                    });
                    setTimeout(recargarPaginaCartera, 2000);

                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al Cancelar la Observacion',
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

function recargarPaginaCartera() {
    window.location.reload();
}







