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

    ocultarTodasLasSecciones();

    // Quitar clase 'activo' de todos los botones
    botones.forEach(b => b.classList.remove('activo'));

    // Agregar clase 'activo' al botón actual
    boton.classList.add('activo');

    const seccionActiva = document.getElementById(textoNormalizado);
    if (seccionActiva) {
      seccionActiva.classList.remove('oculto');

      if (textoNormalizado === "confirmacion" && typeof mostrarResumenConfirmacion === "function") {
        mostrarResumenConfirmacion();
      }
    }
  });
});


/*DEJA LA SECCION ORDEN POR DEFECTO*/ 
document.addEventListener("DOMContentLoaded", () => {
  const seccionOrden = document.getElementById("orden");
  if (seccionOrden) {
    seccionOrden.classList.remove("oculto");
  }

  // Activar visualmente el botón "Orden"
  const botonOrden = Array.from(document.querySelectorAll(".button")).find(boton =>
    normalizarTexto(boton.querySelector(".contenido-button").textContent.trim().toLowerCase()) === "orden"
  );
  if (botonOrden) {
    botonOrden.classList.add("activo");
  }
});

