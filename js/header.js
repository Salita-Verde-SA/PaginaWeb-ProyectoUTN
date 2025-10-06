console.log("header.js");
console.log("Importando header...");
fetch("header.html")
  .then((res) => res.text())
  .then((data) => {
    document.getElementById("header").innerHTML = data;
    document.dispatchEvent(new Event("headerContentLoaded"));
  });
console.log("Header importado");

// Interruptor de tema claro/oscuro

// Espera a que el DOM esté completamente cargado antes de ejecutar
// el script para garantizar que todos los elementos HTML estén
// disponibles y listos para ser manipulados, evitando errores
// como elementos no encontrados o interacciones prematuras que podrían
// afectar la experiencia del usuario.
document.addEventListener("headerContentLoaded", function () {
  console.log("Interruptor.js");
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

  // EVENTO DE ESCUCHA DEL BOTÓN DE TEMA CLARO/OSCURO
  // Escucha cambios en el interruptor
  interruptor.addEventListener("change", () => {
    // Alterna la clase 'claro' en el body para cambiar el modo
    body.classList.toggle("claro");
    header.classList.toggle("claro");
    footer.classList.toggle("claro");

    if (
      contendorGeneros != null &&
      contenedorEventos != null &&
      contenedorViewAll != null
    ) {
      contendorGeneros.classList.toggle("claro");
      contenedorEventos.classList.toggle("claro");
      contenedorViewAll.classList.toggle("claro");
    }

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

  // Ajusta el JavaScript para el menú desplegable
  const botonCuentaUsuario = document.querySelector(".boton-cuenta-usuario");
  const menuDesplegable = document.querySelector(".menu-desplegable");

  // Muestra u oculta el menú desplegable al hacer clic en la foto de perfil
  botonCuentaUsuario.addEventListener("click", function (event) {
    event.stopPropagation(); // Evita que el evento se propague al documento
    menuDesplegable.style.display =
      menuDesplegable.style.display === "block" ? "none" : "block";
  });

  // Oculta el menú desplegable al hacer clic fuera de él
  document.addEventListener("click", function () {
    menuDesplegable.style.display = "none";
  });

  function abrirMenuHamburguesa() {
    document.querySelector(".menu-hamburguesa-contenedor").style.display =
      "flex";
    document.body.style.overflow = "hidden";
  }

  function cerrarMenuHamburguesa() {
    document.querySelector(".menu-hamburguesa-contenedor").style.display =
      "none";
    document.body.style.overflow = "";
  }

  // MENÚ DE HAMBURGUESA PARA LA VISTA DE TELÉFONO
  document
    .querySelector(".menu-hamburguesa")
    .addEventListener("click", abrirMenuHamburguesa);
  document
    .querySelector(".cerrar-menu")
    .addEventListener("click", cerrarMenuHamburguesa);

  // Cerrar el menú hamburguesa al hacer clic fuera de él
  document
    .querySelector(".menu-hamburguesa-contenedor")
    .addEventListener("click", function (e) {
      if (e.target === this) {
        cerrarMenuHamburguesa();
      }
    });

  // Cerrar el menú hamburguesa al presionar la tecla "Escape"
  document.addEventListener("keydown", function (e) {
    if (
      e.key === "Escape" &&
      document.querySelector(".menu-hamburguesa-contenedor").style.display ===
        "flex"
    ) {
      cerrarMenuHamburguesa();
    }
  });

  // Cerrar el menú hamburguesa al hacer clic en un enlace
  window.addEventListener("resize", function () {
    if (window.innerWidth > 800) {
      cerrarMenuHamburguesa();
    }
  });

  console.log("Script de menu hamburguesa cargado correctamente");
});
