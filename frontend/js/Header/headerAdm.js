console.log("headerAdm.js");
console.log("Importando header...");
fetch("headerAdm.html")
  .then((res) => res.text())
  .then((data) => {
    document.getElementById("header").innerHTML = data;
    document.dispatchEvent(new Event("headerContentLoaded"));
  });
console.log("Header importado");

document.addEventListener("headerContentLoaded", async function () {
  // LÓGICA PARA LA SOMBRA DE LA FOTO DE PERFIL
  async function obtenerColorDeFoto() {
    const foto = document.querySelector(".foto-perfil");
    if (!foto) return;
    
    // ... (El resto del código para la sombra se mantiene igual)
  }

  obtenerColorDeFoto().then(() => {
    // ... (El resto del código para la sombra se mantiene igual)
  });

  // LÓGICA PARA EL MENÚ DESPLEGABLE
  const botonPerfil = document.querySelector(".foto-perfil");
  const menuUsuario = document.getElementById("userMenu");

  if (botonPerfil && menuUsuario) {
    botonPerfil.addEventListener("click", function (event) {
        event.stopPropagation();
        menuUsuario.style.display = menuUsuario.style.display === "block" ? "none" : "block";
    });
  }

  document.addEventListener("click", function () {
    if (menuUsuario) {
        menuUsuario.style.display = "none";
    }
  });

  // ✅ CORRECCIÓN: Se agregó la lógica del interruptor de tema aquí
  const interruptor = document.querySelector(".interruptor input");
  const sun = document.querySelector(".bx-sun");
  const moon = document.querySelector(".bx-moon");
  const body = document.body;
  const header = document.querySelector("header");

  if (interruptor && sun && moon && body && header) {
    interruptor.addEventListener("change", () => {
      body.classList.toggle("claro");
      header.classList.toggle("claro");

      // Animar íconos
      if (interruptor.checked) {
        sun.classList.add("girar");
        moon.classList.remove("mecer");
        setTimeout(() => sun.classList.remove("girar"), 1000);
      } else {
        moon.classList.add("mecer");
        sun.classList.remove("girar");
        setTimeout(() => moon.classList.remove("mecer"), 1000);
      }

      setTimeout(() => {
        sun.classList.toggle("oscuro");
        moon.classList.toggle("oscuro");
      }, 125);
    });
  }
});