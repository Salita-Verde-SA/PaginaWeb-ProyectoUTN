document.addEventListener("DOMContentLoaded", function () {
  function obtenerPrecioDesdeTexto(texto) {
    const limpio = texto.replace(/[^\d,]/g, "").replace(".", "").replace(",", ".");
    return parseFloat(limpio);
  }

  function actualizarTotal() {
    let total = 0;
    let cantidadTotal = 0;
    let eventos = {};

    const boxes = document.querySelectorAll(".box-event");

    boxes.forEach(box => {
      const nombreEvento = box.querySelector(".name-event")?.textContent?.trim() || "Evento desconocido";
      const bloquesTickets = box.querySelectorAll(".opciones-tickets");

      bloquesTickets.forEach(bloque => {
        const tipo = bloque.querySelector(".tipo-ticket")?.textContent?.trim().split(" ")[0] || "-";
        const precioTexto = bloque.querySelector(".precio-entrada")?.textContent || "0";
        const cantidadTexto = bloque.querySelector(".valor")?.textContent || "0";

        const precio = obtenerPrecioDesdeTexto(precioTexto);
        const cantidad = parseInt(cantidadTexto);

        if (cantidad > 0) {
          const subtotal = precio * cantidad;
          cantidadTotal += cantidad;
          total += subtotal;

          if (!eventos[nombreEvento]) eventos[nombreEvento] = [];
          eventos[nombreEvento].push({ tipo, cantidad, subtotal });
        }
      });
    });

    const totalElement = document.getElementById("total");
    if (totalElement) {
      totalElement.textContent = `$${total.toLocaleString("es-AR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })}`;
    }

    const detalleContainer = document.getElementById("detalleFacturaEventos");
    if (detalleContainer) {
      detalleContainer.innerHTML = "";

      Object.entries(eventos).forEach(([evento, entradas]) => {
        let html = `<div class="evento-factura"><p class="nombre-evento"><strong>${evento}</strong></p>`;
        entradas.forEach(e => {
          html += `
            <p class="tipo-entrada">Tipo: ${e.tipo}</p>
            <p class="cantidad-entrada">Cantidad: ${e.cantidad}</p>
            <p class="subtotal-entrada">Subtotal: $${e.subtotal.toLocaleString("es-AR", {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2
            })}</p>`;
        });
        html += `</div>`;
        detalleContainer.innerHTML += html;
      });
    }

    const cantidadTotalElement = document.getElementById("cantidadTotal");
    if (cantidadTotalElement) {
      cantidadTotalElement.textContent = cantidadTotal;
    }

    const totalFacturaElement = document.getElementById("totalFactura");
    if (totalFacturaElement) {
      totalFacturaElement.textContent = `$${total.toLocaleString("es-AR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })}`;
    }
    const resumenCompra = {
      entradas: [],
      total: total
    };

    Object.entries(eventos).forEach(([evento, entradas]) => {
      entradas.forEach(e => {
        resumenCompra.entradas.push({
          evento,
          tipo: e.tipo,
          cantidad: e.cantidad,
          precio: e.subtotal / e.cantidad
        });
      });
    });

    // Guardar en localStorage
    localStorage.setItem("resumenCompra", JSON.stringify(resumenCompra));

  }

  // Activar botones de todos los contadores
  const contadores = document.querySelectorAll(".contador");

  contadores.forEach(contador => {
    const cantidadSpan = contador.querySelector(".valor");
    const botonMas = contador.querySelector(".mas");
    const botonMenos = contador.querySelector(".menos");

    if (!cantidadSpan || !botonMas || !botonMenos) return;

    botonMas.addEventListener("click", () => {
      let cantidad = parseInt(cantidadSpan.textContent) || 0;
      cantidadSpan.textContent = cantidad + 1;
      actualizarTotal();
    });

    botonMenos.addEventListener("click", () => {
      let cantidad = parseInt(cantidadSpan.textContent) || 0;
      if (cantidad > 0) {
        cantidadSpan.textContent = cantidad - 1;
        actualizarTotal();
      }
    });
  });

  actualizarTotal(); // Ejecutar al cargar
});
