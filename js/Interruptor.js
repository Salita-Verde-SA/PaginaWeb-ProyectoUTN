const toggle = document.querySelector(".interruptor input");
const sun = document.querySelector(".bx-sun");
const moon = document.querySelector(".bx-moon");
const boton = document.querySelector(".interruptor input");
const body = document.body;

toggle.addEventListener("change", () => {
  body.classList.toggle("claro");
  if (toggle.checked) {
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
});
