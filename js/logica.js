function IniciarSesion (){
    var usuario = document.getElementById("usuario").value;
    var contrasena = document.getElementById("contrasena").value;

    if (usuario === "admin" && contrasena === "admin123") {
        alert("Inicio de sesión exitoso");
        // Redirigir a la página principal o dashboard
        window.location.href = "dashboard.html"; // Cambia esto a la URL deseada
    } else {
        alert("Usuario o contraseña incorrectos");
    }

}