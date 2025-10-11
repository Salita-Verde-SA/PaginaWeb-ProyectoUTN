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
    
    // 1. Validación: No permitir guardar un nombre vacío
    if (nuevoNombre === "") {
        alert("El nombre de usuario no puede estar vacío.");
        return; 
    }

    // 2. VALIDACIÓN AÑADIDA: No permitir nombres mayores a 11 caracteres
    if (nuevoNombre.length > 11) {
        alert("El nombre no puede tener más de 11 caracteres.");
        return; // Detiene la ejecución si falla la validación
    }

    // Si pasa ambas validaciones, procede a guardar:
    
    // 3. Actualiza el nombre visible
    document.getElementById('nombre-usuario-display').innerText = nuevoNombre;
    
    // 4. Oculta el campo de edición y muestra el display (llamando al toggle)
    mostrarCampoEditName();
}

function compartirPerfil() {
    const perfilURL = window.location.href; // Obtiene la URL actual del perfil
    const perfilTitulo = document.title;
    
    // 1. Intentar usar la Web Share API (ideal para móvil)
    if (navigator.share) {
        navigator.share({
            title: perfilTitulo,
            url: perfilURL
        }).then(() => {
            console.log('Perfil compartido con éxito.');
        }).catch((error) => {
            console.error('Error al compartir:', error);
        });
        
    } else {
        // 2. Opción de fallback: Copiar al portapapeles
        navigator.clipboard.writeText(perfilURL)
            .then(() => {
                alert('¡Enlace del perfil copiado al portapapeles!');
            })
            .catch(err => {
                console.error('Error al copiar:', err);
                alert('No se pudo copiar el enlace automáticamente. URL: ' + perfilURL);
            });
    }
}