function validarSession() {
    
   
    $.ajax({
        method: "GET",
        url: "ServletControlador?accion=sesion"
       

    }).done(function (data) {
        var datos = data;
        
        if(datos === "null"){
            window.location.replace("login.html");
        }

    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}