/*function actualizarTotal() {
  let total = 0;
  let cantidadTotal = 0;
  let tipos = [];

  const bloques = document.querySelectorAll(".opciones-tickets");

  bloques.forEach(bloque => {
    const tipo = bloque.querySelector(".tipo-ticket select").value; // VIP o General
    const precio = parseFloat(bloque.querySelector(".tipo-ticket").dataset.precio);
    const cantidad = parseInt(bloque.querySelector(".valor").textContent) || 0;

    if (cantidad > 0) {
      tipos.push(tipo);
      cantidadTotal += cantidad;
      total += precio * cantidad;
    }
  });

  // Actualiza el total en el resumen principal
  document.getElementById("total").textContent = total.toLocaleString("es-AR", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });

  // Actualiza el resumen de factura si existe
  const tipoEntrada = tipos.length === 1 ? tipos[0] : tipos.length > 1 ? "Mixto" : "-";
  const resumen = document.getElementById("resumenFactura");
  if (resumen) {
    document.getElementById("tipoEntrada").textContent = tipoEntrada;
    document.getElementById("cantidadTotal").textContent = cantidadTotal;
    document.getElementById("totalFactura").textContent = `$${total.toLocaleString("es-AR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })}`;
  }
}
