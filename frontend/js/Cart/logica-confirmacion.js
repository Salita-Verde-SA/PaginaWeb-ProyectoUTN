// Simulación de datos (estos deberían venir de tu lógica real)
const datosCompra = {
  entradas: [
    { evento: "SANTIAGO MOTORIZADO", tipo: "VIP", cantidad: 2, precio: 32000 },
    { evento: "TEATRO SELECTRO", tipo: "General", cantidad: 1, precio: 10000 }
  ],
  metodoPago: "Tarjeta de crédito",
  nombreTarjeta: "Uriel Rojas",
  total: 76000
};

// Función para renderizar el resumen
function mostrarResumenConfirmacion() {
  const contenedor = document.getElementById("resumen-confirmacion");
  contenedor.innerHTML = "";

  const datosGuardados = localStorage.getItem("resumenCompra");
  if (!datosGuardados) {
    contenedor.innerHTML = "<p>No hay datos disponibles para mostrar.</p>";
    return;
  }

  const datosCompra = JSON.parse(datosGuardados);

  datosCompra.entradas.forEach(entrada => {
    const div = document.createElement("div");
    div.innerHTML = `
      <p><strong>Evento:</strong> ${entrada.evento}</p>
      <p><strong>Tipo:</strong> ${entrada.tipo}</p>
      <p><strong>Cantidad:</strong> ${entrada.cantidad}</p>
      <p><strong>Subtotal:</strong> $${(entrada.precio * entrada.cantidad).toLocaleString("es-AR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })}</p>
      <hr/>
    `;
    contenedor.appendChild(div);
  });

  const pago = document.createElement("div");
  pago.innerHTML = `
    <p><strong>Total a pagar:</strong> $${datosCompra.total.toLocaleString("es-AR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })}</p>
  `;
  contenedor.appendChild(pago);
}



// Podés llamar esta función cuando el usuario navega a la sección "Confirmación"
