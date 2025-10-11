function mostrarCampoEditName() {
    var campoNombreContenedor = document.getElementById('campo-nombre-usuario');
    var nombreDisplayContenedor = document.querySelector('.nombre-usu-display');
    var nombreDisplay = document.getElementById('nombre-usuario-display').innerText;
    var inputElement = document.getElementById('nombre-usuario-input');

    // Lógica para alternar la visibilidad
    if (campoNombreContenedor.style.display === 'none' || campoNombreContenedor.style.display === '') {
        
        // 1. Mostrar el campo de edición
        campoNombreContenedor.style.display = 'flex';

        // 2. Establecer el valor actual del nombre en el input al editar
        inputElement.value = nombreDisplay.trim();

    } else {
        
        // 1. Ocultar el campo de edición
        campoNombreContenedor.style.display = 'none';
        
        // 2. Mostrar el nombre visible y el lápiz
        nombreDisplayContenedor.style.display = 'flex';
    }
}

function guardarNombre() {
    var inputElement = document.getElementById('nombre-usuario-input');
    var nuevoNombre = inputElement.value.trim();
    
    // Validación: No permitir guardar un nombre vacío
    if (nuevoNombre === "") {
        alert("El nombre de usuario no puede estar vacío.");
        return; 
    }

    // 1. Actualiza el nombre visible
    document.getElementById('nombre-usuario-display').innerText = nuevoNombre;
    
    // 2. Oculta el campo de edición y muestra el display (llamando al toggle)
    mostrarCampoEditName();
}