document.addEventListener("DOMContentLoaded", function () {
    const contadores = document.querySelectorAll(".contador");

    contadores.forEach(contador => {
      const valor = contador.querySelector(".valor");
      const botonMas = contador.querySelector(".mas");
      const botonMenos = contador.querySelector(".menos");

      botonMas.addEventListener("click", () => {
        let cantidad = parseInt(valor.textContent);
        valor.textContent = cantidad + 1;
      });

      botonMenos.addEventListener("click", () => {
        let cantidad = parseInt(valor.textContent);
        if (cantidad > 0) {
          valor.textContent = cantidad - 1;
        }
      });
    });
  });