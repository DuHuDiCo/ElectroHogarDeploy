
function consignacionesMesCaja(){
    validarSession();
    obtenerNombreUsuario();
    consignacionesDiaCaja();
    consignacionesAplicadasCaja();
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesMesCaja"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conMesCaja").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function consignacionesDiaCaja(){
    validarSession();
    obtenerNombreUsuario(); 
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesDiaCaja"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conDiaCaja").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesAplicadasCaja(){
    validarSession();
    obtenerNombreUsuario(); 
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesAplicadasCaja"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conAplicadasCaja").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}