function conectarWebSocket(){
    
    var uriWs = "ws://localhost:8080/ElectroHogar/socket";
    var miWebsocket = new WebSocket(uriWs);
    console.log(miWebsocket);
    
    miWebsocket.onopen = function (evento){
      console.log("abierto");
    };
    
    miWebsocket.onmessage=function (evento){
      console.log(evento.data);  
    };
}


function consignacionesMesContabilidad(){
    
    obtenerNombreUsuario();
    
    consignacionesDiaContabilidad();
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesMesContabilidad"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conMesContabilidad").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesDiaContabilidad(){
    
    
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesDiaContabilidad"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conDiaContabilidad").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesComprobadas(){
    
    
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones2?accion=consignacionesComprobadas"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conComprobadasContabilidad").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}
