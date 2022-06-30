function obtenerNombreUsuario() {
    cargarDatosInicio();
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

function obtenerNombreUsuarioAdmin() {
    validarSession();
    cargarDatosInicio();
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


function cargarDatosInicio(){
    
    consignacionesMes();
    consignacionesDia();
    consignacionesDevuletas();
    consignacionesPendientes();
    consignacionesComprobadas();
    consignacionesAplicadas();
    
}

function consignacionesMes(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesMes"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conMes").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesDia(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesDia"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conDia").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}


function consignacionesDevuletas(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesDevueltas"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conDevueltas").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesPendientes(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesPendientes"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conPendientes").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesComprobadas(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesComprobadas"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conComprobadas").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}

function consignacionesAplicadas(){
    
     $.ajax({
        method: "GET",
        url: "ServletControladorConsignaciones?accion=consignacionesAplicadas"
       

    }).done(function (data) {

        var datos = data;
        
        
        document.getElementById("conAplicadas").innerHTML = datos;
      


     
    }).fail(function () {

        window.location.replace("login.html");
    }).always(function () {

    });
}