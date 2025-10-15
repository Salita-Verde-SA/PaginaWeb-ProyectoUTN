// Navegación entre secciones
const botones = document.querySelectorAll('.button');
const secciones = document.querySelectorAll('.seccion');

function ocultarTodasLasSecciones() {
  secciones.forEach(seccion => seccion.classList.add('oculto'));
}

function normalizarTexto(texto) {
  return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

// Inicializa sección Envío con tarjetas de regalo
function inicializarSeccionEnvio() {
  const tarjetasContainer = document.getElementById("tarjetas-entradas");
  if (!tarjetasContainer) return;

  tarjetasContainer.innerHTML = "";

  const resumen = JSON.parse(localStorage.getItem("resumenCompra"));
  if (resumen && resumen.entradas) {
    resumen.entradas.forEach((entrada, index) => {
      for (let i = 0; i < entrada.cantidad; i++) {
        const id = `entrada-${index}-${i + 1}`;
        const wrapper = document.createElement("div");
        wrapper.classList.add("tarjeta-regalo");
        wrapper.id = id;

        wrapper.innerHTML = `
          <p><strong>${entrada.evento} - ${entrada.tipo} (Entrada ${i + 1})</strong></p>
          <button class="boton-regalo" data-id="${id}">
            <i class="fa-solid fa-gift"></i>
          </button>
          <div class="campos-extra oculto">
            <label>Correo destinatario:</label>
            <input type="email" name="correo-${id}" required>
            <label>Mensaje personalizado:</label>
            <textarea name="mensaje-${id}" rows="2" placeholder="¡Disfrutá el evento!"></textarea>
          </div>
        `;

        tarjetasContainer.appendChild(wrapper);

        const boton = wrapper.querySelector(".boton-regalo");
        const campos = wrapper.querySelector(".campos-extra");
        const icono = boton.querySelector("i");

        boton.addEventListener("click", () => {
          const activo = campos.classList.toggle("oculto") === false;
          boton.classList.toggle("activo", activo);
          boton.innerHTML = activo
            ? `<i class="fa-solid fa-xmark"></i>`
            : `<i class="fa-solid fa-gift"></i>`;
        });
      }
    });
  }

  const formEnvio = document.getElementById("form-envio");
  if (formEnvio) {
    formEnvio.addEventListener("submit", function (e) {
      e.preventDefault();

      const envioData = {
        correoPrincipal: document.getElementById("correo-principal").value,
        regalos: []
      };

      const botonesActivos = document.querySelectorAll(".boton-regalo.activo");
      botonesActivos.forEach(boton => {
        const id = boton.dataset.id;
        const correo = formEnvio[`correo-${id}`]?.value || "";
        const mensaje = formEnvio[`mensaje-${id}`]?.value || "";
        envioData.regalos.push({
          entrada: id,
          correo,
          mensaje
        });
      });

      localStorage.setItem("datosEnvio", JSON.stringify(envioData));
      console.log("Datos de envío guardados:", envioData);
    });
  }
}

// Activación de navegación
botones.forEach(boton => {
  boton.addEventListener('click', () => {
    const textoOriginal = boton.querySelector('.contenido-button').textContent.trim().toLowerCase();
    const textoNormalizado = normalizarTexto(textoOriginal);

    ocultarTodasLasSecciones();
    botones.forEach(b => b.classList.remove('activo'));
    boton.classList.add('activo');

    const seccionActiva = document.getElementById(textoNormalizado);
    if (seccionActiva) {
      seccionActiva.classList.remove('oculto');

      if (textoNormalizado === "envio") inicializarSeccionEnvio();
      if (textoNormalizado === "confirmacion" && typeof mostrarResumenConfirmacion === "function") {
        mostrarResumenConfirmacion();
      }
    }
  });
});

// Activar sección Orden por defecto
document.addEventListener("DOMContentLoaded", () => {
  const seccionOrden = document.getElementById("orden");
  if (seccionOrden) seccionOrden.classList.remove("oculto");

  const botonOrden = Array.from(botones).find(boton =>
    normalizarTexto(boton.querySelector(".contenido-button").textContent.trim().toLowerCase()) === "orden"
  );
  if (botonOrden) botonOrden.classList.add("activo");
});
