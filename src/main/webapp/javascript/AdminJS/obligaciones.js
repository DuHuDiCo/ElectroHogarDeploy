function listarObligaciones(){
    validarSession();
    obtenerNombreUsuario();
    
     $.ajax({
        method: "GET",
        url: "ServletControladorObligaciones?accion=listarObligaciones"

    }).done(function (data) {
        var datos = JSON.stringify(data);
        var json = JSON.parse(datos);
       
        
        $("#dataTable tbody").empty();

        var contador = 1;

        $.each(json, function (key, value) {

            $("#dataTable").append('<tr> <td>' + contador + '</td><td>' + value.nombre_titular + '</td><td>' + value.n_documento + '</td><td>' + value.telefono_titular + '</td><td>' + value.email + '</td><td>' + value.direccion + '</td><td>' + value.codigo_cliente + '</td><td>' + value.valor_cuota + '</td><td>' + value.saldo_capital + '</td><td>' + value.fecha_obligacion + '</td><td>' + value.saldo_mora + '</td><td>' + value.dias_mora + '</td><td>' + value.nombre_sede + '</td></tr>');
            contador = contador + 1;
        });




        console.log(json);

    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}