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
  const localidad = document.querySelector(".barra-navegacion-izquierda");
  const barrabusqueda = document.querySelector(".barra-busqueda");

  // Verifica que todos los elementos necesarios existan antes de continuar
  if (!interruptor || !sun || !moon || !body || !header) {
    console.error("No se pudieron encontrar todos los elementos necesarios");
    return;
  }

  console.log("Variables declaradas:");
  console.log("interruptor:", interruptor);
  console.log("sun:", sun);
  console.log("moon:", moon);
  console.log("body:", body);
  console.log("header:", header);
  console.log("localidad:", localidad);

  // Escucha cambios en el interruptor
  interruptor.addEventListener("change", () => {
    // Alterna la clase 'claro' en el body para cambiar el modo
    body.classList.toggle("claro");
    header.classList.toggle("claro");
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
