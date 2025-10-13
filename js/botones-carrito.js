// Selecciona todos los botones de navegación
const botones = document.querySelectorAll('.button');

// Selecciona todas las secciones dinámicas
const secciones = document.querySelectorAll('.seccion');

// Función para ocultar todas las secciones
function ocultarTodasLasSecciones() {
  secciones.forEach(seccion => {
    seccion.classList.add('oculto');
  });
}

// Asigna el evento a cada botón
botones.forEach(boton => {
  boton.addEventListener('click', () => {
    const texto = boton.querySelector('.contenido-button').textContent.trim().toLowerCase();

    ocultarTodasLasSecciones(); // Oculta todas las secciones

    // Muestra la sección correspondiente si existe
    const seccionActiva = document.getElementById(texto);
    if (seccionActiva) {
      seccionActiva.classList.remove('oculto');
    }
  });
});
