console.log("header.js");
console.log("Importando header...");

// Comprobación de los elementos del header, sirve para que no genere errores
// al tratar de importar un header si ya existe, como en las páginas de inicio
// de sesión o registro.
const header = document.querySelector("header");
console.log(
  "Evaluando header:",
  header,
  "y header.children.length:",
  header ? header.children.length : "header no existe"
);
if (header && header.children.length === 0) {
  console.warn(
    "El header no tiene elementos adentro.\n" + "Se cargará dinámicamente."
  );
  fetch("header.html")
    .then((res) => res.text())
    .then((data) => {
      document.getElementById("header").innerHTML = data;
      document.dispatchEvent(new Event("headerContentLoaded"));
    });
  console.log("Header importado.");
} else if (header) {
  console.warn(
    "El header ya tiene elementos dentro.\nDebe tratarse de una página de inicio de sesión o relacionada."
  );
}
// --- LÓGICA PARA EL PANEL DE NOTIFICACIONES ---

// Se ejecuta después de que el contenido del header (incluido el panel) se haya cargado
document.addEventListener('headerContentLoaded', function () {
  const bellIcon = document.getElementById('bell-icon');
  const notificationPanel = document.getElementById('notification-panel');

  if (bellIcon && notificationPanel) {
    console.log("Elementos de notificación encontrados. Inicializando...");

    bellIcon.addEventListener('click', function (event) {
      // Detiene la propagación para que el clic no cierre el panel inmediatamente
      event.stopPropagation();
      
      // Muestra u oculta el panel
      const isVisible = notificationPanel.style.display === 'block';
      notificationPanel.style.display = isVisible ? 'none' : 'block';
    });

    // Cierra el panel si se hace clic en cualquier otro lugar de la página
    document.addEventListener('click', function (event) {
      if (notificationPanel.style.display === 'block' && !notificationPanel.contains(event.target)) {
        notificationPanel.style.display = 'none';
      }
    });

  } else {
    console.warn("No se encontraron los elementos para el panel de notificaciones.");
  }
});
function checkMenuTelefono() {
  // Comprobación de la existencia del elemento correspondiente al menu
  // de hamburguesa que se usaría en teléfono. Ya que en las páginas de inicio
  // de sesión y registro no está presente genera errores.
  if (!document.getElementById("menu-telefono")) {
    console.warn(
      "El elemento con id 'menu-telefono' no existe, " +
        "no se usará. \nEs normal que pase en una página de " +
        "inicio de sesión o relacionada."
    );
    document.dispatchEvent(new Event("sinMenuTelefono"));
  } else {
    console.log("El elemento con id 'menu-telefono' existe, se usará.");
    document.addEventListener("headerContentLoaded", function () {
      console.log("Importando menú de teléfono...");
      fetch("menu-telefono.html")
        .then((res) => res.text())
        .then((data) => {
          document.getElementById("menu-telefono").innerHTML = data;
          document.dispatchEvent(new Event("menuTelefonoContentLoaded"));
        });
      console.log("Menú de teléfono importado.");
    });
  }
}

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOMContentLoaded");
  checkMenuTelefono();
  document.dispatchEvent(new Event("headerContentLoaded"));
});

// Espera a que el DOM esté completamente cargado antes de ejecutar
// el script para garantizar que todos los elementos HTML, sobre todo
// los que se cargan dinámicamente mediante la primera parte del script,
// estén disponibles y listos para ser manipulados, evitando errores
// como elementos no encontrados o interacciones prematuras que podrían
// afectar la experiencia del usuario.

// Esta parte se ejecuta después de que se han cargado los contenidos
// del header y el menú de teléfono, asegurando que todos los elementos
// necesarios estén disponibles para su manipulación.
document.addEventListener("headerContentLoaded", function () {
  console.log("Script del header iniciado...");
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

  // MENÚ DESPLEGABLE DE USUARIO

  // Ajusta el JavaScript para el menú desplegable
  const botonCuentaUsuario = document.querySelector(".boton-cuenta-usuario");
  const menuDesplegable = document.querySelector(".menu-desplegable");

  if (botonCuentaUsuario && menuDesplegable) {
    console.log("Elementos del menú desplegable encontrados, inicializando...");

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

    console.log("Menú desplegable inicializado correctamente");
  } else {
    console.warn(
      "No se encontraron todos los elementos necesarios para el menú desplegable. " +
        "Elementos faltantes: " +
        (botonCuentaUsuario ? "" : ".boton-cuenta-usuario ") +
        (menuDesplegable ? "" : ".menu-desplegable")
    );
  }

  // MENÚ DE HAMBURGUESA PARA LA VISTA DE TELÉFONO

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

  document.addEventListener("menuTelefonoContentLoaded", function () {
    if (document.getElementById("menu-telefono")) {
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
          document.querySelector(".menu-hamburguesa-contenedor").style
            .display === "flex"
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
    }
  });
});
