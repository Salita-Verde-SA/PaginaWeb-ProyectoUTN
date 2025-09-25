// Espera a que el DOM esté completamente cargado antes de ejecutar
// el script para garantizar que todos los elementos HTML estén
// disponibles y listos para ser manipulados, evitando errores
// como elementos no encontrados o interacciones prematuras que podrían
// afectar la experiencia del usuario.
document.addEventListener("headerContentLoaded", function () {
  // Selecciona el interruptor de modo claro/oscuro
  const interruptor = document.querySelector(".interruptor input");
  // Selecciona el ícono del sol
  const sun = document.querySelector(".bx-sun");
  // Selecciona el ícono de la luna
  const moon = document.querySelector(".bx-moon");
  // Selecciona el elemento body
  const body = document.body;
  const header = document.querySelector("header");
  const footer = document.querySelector("footer");
  const localidad = document.querySelector(".barra-navegacion-izquierda");
  const contendorGeneros = document.querySelector(".barra-lateral-izquierda");
  const contenedorEventos = document.querySelector(".barra-lateral-derecha");
  const contenedorViewAll = document.querySelector(".contenido-principal");
  const barrabusqueda = document.querySelector(".barra-busqueda");

  // Verifica que todos los elementos necesarios existan antes de continuar
  if (!interruptor || !sun || !moon || !body || !header) {
    console.error("No se pudieron encontrar todos los elementos necesarios");
    return;
  }

  // Escucha cambios en el interruptor
  interruptor.addEventListener("change", () => {
    // Alterna la clase 'claro' en el body para cambiar el modo
    body.classList.toggle("claro");
    header.classList.toggle("claro");
    footer.classList.toggle("claro");
    contendorGeneros.classList.toggle("claro");
    contenedorEventos.classList.toggle("claro");
    contenedorViewAll.classList.toggle("claro");

    if (barrabusqueda != null) {
      barrabusqueda.classList.toggle("claro");
    }
    if (localidad != null) {
      localidad.classList.toggle("claro");
    }
    if (interruptor.checked) {
      // Activa modo claro → gira el sol
      sun.classList.add("girar");
      moon.classList.remove("mecer");
      setTimeout(() => sun.classList.remove("girar"), 1000);
    } else {
      // Activa modo oscuro → mece la luna
      moon.classList.add("mecer");
      sun.classList.remove("girar");
      setTimeout(() => moon.classList.remove("mecer"), 1000);
    }

    // Cambia el estado de los íconos después de un pequeño retraso
    setTimeout(() => {
      sun.classList.toggle("oscuro");
      moon.classList.toggle("oscuro");
    }, 125);
  });
});

// TODO : Eliminar lo siguiebte si no se usa o si se implementa de otra forma más adelante, agregar condicional para que no falle en páginas sin estos elementos

// document.querySelector('.menos').addEventListener('click', () => {
//   let valor = parseInt(document.querySelector('.valor').textContent);
//   if (valor > 0) {
//     document.querySelector('.valor').textContent = valor - 1;
//   }
// });

// document.querySelector('.mas').addEventListener('click', () => {
//   let valor = parseInt(document.querySelector('.valor').textContent);
//   document.querySelector('.valor').textContent = valor + 1;
// });
// /**
//  * Función para alternar la visibilidad del menú de usuario.
//  * Si el menú está oculto, lo muestra, y viceversa.
//  */
// function toggleMenu() {
//   // Selecciona el elemento del menú de usuario por su ID
//   const userMenu = document.getElementById("userMenu");
//   // Alterna la clase 'active' en el menú de usuario.
//   // Si la clase está presente, la quita (ocultando el menú).
//   // Si no está presente, la añade (mostrando el menú).
//   userMenu.classList.toggle("active");
// }

// // Escucha los clics en cualquier parte del documento
// document.addEventListener("click", function(event) {
//   // Selecciona el elemento del menú de usuario por su ID
//   const userMenu = document.getElementById("userMenu");
//   // Selecciona el elemento del encabezado del perfil por su clase
//   const perfilHeader = document.querySelector(".perfil-header");

//   // Verifica si el clic ocurrió fuera del área del perfil
//   if (!perfilHeader.contains(event.target)) {
//     // Si el clic fue fuera del área del perfil, remueve la clase 'active'
//     // del menú de usuario, ocultándolo
//     userMenu.classList.remove("active");
//   }
// });

