




function consignacionesMesCartera(){
    validarSession();
    obtenerNombreUsuarioCartera();
    consignacionesDiaCartera();
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesMesCartera"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conMesCartera").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function obtenerNombreUsuarioCartera() {
    
    $.ajax({
        method: "POST",
        url: "ServletControlador?accion=obtenerNombreUsuario"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("username").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });

}

function consignacionesDiaCartera(){
    
    obtenerNombreUsuario(); 
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesDiaCartera"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conDiaCartera").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

