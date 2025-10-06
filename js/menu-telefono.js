document.addEventListener("headerContentLoaded", function () {
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

  document
    .querySelector(".menu-hamburguesa")
    .addEventListener("click", abrirMenuHamburguesa);
  document
    .querySelector(".cerrar-menu")
    .addEventListener("click", cerrarMenuHamburguesa);

  document
    .querySelector(".menu-hamburguesa-contenedor")
    .addEventListener("click", function (e) {
      if (e.target === this) {
        cerrarMenuHamburguesa();
      }
    });

  document.addEventListener("keydown", function (e) {
    if (
      e.key === "Escape" &&
      document.querySelector(".menu-hamburguesa-contenedor").style.display ===
        "flex"
    ) {
      cerrarMenuHamburguesa();
    }
  });

  console.log("Script de menu hamburguesa cargado correctamente");
});
