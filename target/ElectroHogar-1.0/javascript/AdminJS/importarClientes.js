
/* global Swal */

const $formC = document.querySelector('#formObligaciones');

$formC.addEventListener('submit', (event) => {
    validarSession();
    event.preventDefault();
    const formData = new FormData(event.currentTarget);



    $.ajax({
        method: "POST",
        url: "ServletControladorFiles?accion=guardartxt",
        data: formData,
        processData: false,
        contentType: false

    }).done(function (data) {

        var datos = data;
      
        document.getElementById('labelInput').outerHTML = "";


        if (datos !== null) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Inicio Exitoso',
                showConfirmButton: false,
                timer: 6500

            });
          
        }


        // imprimimos la respuesta
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
});


function listarFiles(){
    validarSession();
    obtenerNombreUsuario();
    $.ajax({
        method: "GET",
        url: "ServletControladorFiles?accion=listarFiles"
        
    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
        var boton = "";
        
        var contador = 1;

        $.each(json, function (key, value) {
            boton = "";                                
            $("#tblFiles").append('<tr> <td>' + contador + '</td><td>' + value.nombre_archivo + '</td><td>' + value.fecha + '</td><td>' + value.nombre_usuario + '</td><td><a href="#" onclick="eliminarFile('+value.idFile+','+value.idUsuario+')" class="btn btn-danger"><i class="fas fa-trash"></i></a></td></tr>');
            contador = contador + 1;
        });

        
        
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
    
}