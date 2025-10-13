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

// Función para normalizar texto (elimina tildes y caracteres especiales)
function normalizarTexto(texto) {
  return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

// Asigna el evento a cada botón
botones.forEach(boton => {
  boton.addEventListener('click', () => {
    const textoOriginal = boton.querySelector('.contenido-button').textContent.trim().toLowerCase();
    const textoNormalizado = normalizarTexto(textoOriginal);

    ocultarTodasLasSecciones(); // Oculta todas las secciones

    // Muestra la sección correspondiente si existe
    const seccionActiva = document.getElementById(textoNormalizado);
    if (seccionActiva) {
      seccionActiva.classList.remove('oculto');

      // ✅ Ejecutar resumen si es la sección de confirmación
      if (textoNormalizado === "confirmacion" && typeof mostrarResumenConfirmacion === "function") {
        mostrarResumenConfirmacion();
      }
    }
  });
});
