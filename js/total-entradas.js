document.addEventListener("DOMContentLoaded", function () {
  function actualizarTotal() {
    let total = 0;
    const bloques = document.querySelectorAll(".opciones-tickets");

    bloques.forEach(bloque => {
      const precio = parseFloat(bloque.querySelector(".tipo-ticket").dataset.precio);
      const cantidad = parseInt(bloque.querySelector(".valor").textContent) || 0;
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

    // Eliminar listeners previos si existen
    botonMas.replaceWith(botonMas.cloneNode(true));
    botonMenos.replaceWith(botonMenos.cloneNode(true));

    const nuevoMas = contador.querySelector(".mas");
    const nuevoMenos = contador.querySelector(".menos");

    nuevoMas.addEventListener("click", () => {
      let cantidad = parseInt(valor.textContent);
      valor.textContent = cantidad + 1;
      actualizarTotal();
    });

    nuevoMenos.addEventListener("click", () => {
      let cantidad = parseInt(valor.textContent);
      if (cantidad > 0) {
        valor.textContent = cantidad - 1;
        actualizarTotal();
      }
    });
  });
});
