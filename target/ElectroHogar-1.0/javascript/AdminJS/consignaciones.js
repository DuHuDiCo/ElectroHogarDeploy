/* global Swal */



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


