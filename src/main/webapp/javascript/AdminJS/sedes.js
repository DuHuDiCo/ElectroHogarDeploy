
/* global Swal */
function guardarSede(){
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
                timer: 6500
            });
            

            
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


function listarSedes(){
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
            var accion = "<td><a href='#' class='btn btn-primary'><i class='fas fa-pen'></i></a></td>";
            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_sede + '</td><td>' + value.municipio + '</td><td>' + value.telefono + '</td>'+accion+'</tr>');
            contador = contador + 1;
        });




        console.log(json);



    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}





