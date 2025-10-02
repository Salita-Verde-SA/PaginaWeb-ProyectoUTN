document.addEventListener("DOMContentLoaded", function () {
  function obtenerPrecioDesdeTexto(texto) {
    // Extrae número desde "32.000,00 ars"
    const limpio = texto.replace(/[^\d,]/g, "").replace(".", "").replace(",", ".");
    return parseFloat(limpio);
  }

  function actualizarTotal() {
    let total = 0;
    const bloques = document.querySelectorAll(".opciones-tickets");

    bloques.forEach(bloque => {
      const precioTexto = bloque.querySelector(".precio-entrada").textContent;
      const cantidadTexto = bloque.querySelector(".valor").textContent;
      const precio = obtenerPrecioDesdeTexto(precioTexto);
      const cantidad = parseInt(cantidadTexto) || 0;
      total += precio * cantidad;
    });

    document.getElementById("total").textContent = total.toLocaleString("es-AR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }

  const contadores = document.querySelectorAll(".contador");

  contadores.forEach(contador => {
    const valor = contador.querySelector(".valor");
    const botonMas = contador.querySelector(".mas");
    const botonMenos = contador.querySelector(".menos");

    botonMas.addEventListener("click", () => {
      let cantidad = parseInt(valor.textContent);
      valor.textContent = cantidad + 1;
      actualizarTotal();
    });

    botonMenos.addEventListener("click", () => {
      let cantidad = parseInt(valor.textContent);
      if (cantidad > 0) {
        valor.textContent = cantidad - 1;
        actualizarTotal();
      }
    });
  });
});
