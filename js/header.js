console.log("header.js");
console.log("Importando header...");
fetch("header.html")
  .then((res) => res.text())
  .then((data) => {
    document.getElementById("header").innerHTML = data;
    document.dispatchEvent(new Event("headerContentLoaded"));
  });
console.log("Header importado");

document.addEventListener("headerContentLoaded", async function () {
  async function obtenerColorDeFoto() {
    const foto = document.querySelector(".foto-perfil");
    if (!foto) return;
    console.log("foto:", foto);

    const canvas = document.createElement("canvas");
    const ctx = canvas.getContext("2d");
    canvas.width = foto.naturalWidth || foto.width;
    canvas.height = foto.naturalHeight || foto.height;
    ctx.drawImage(foto, 0, 0, canvas.width, canvas.height);

    const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height).data;
    let r = 0,
      g = 0,
      b = 0,
      count = 0;

    for (let i = 0; i < imageData.length; i += 4) {
      r += imageData[i];
      g += imageData[i + 1];
      b += imageData[i + 2];
      count++;
    }

    r = Math.floor(r / count);
    g = Math.floor(g / count);
    b = Math.floor(b / count);
    console.log("r:", r, "g:", g, "b:", b);

    resultado = `#${((1 << 24) + (r << 16) + (g << 8) + b)
      .toString(16)
      .slice(1)}`;
    console.log("resultado:", resultado);
  }

  obtenerColorDeFoto().then(() => {
    document.querySelector(
      ".foto-perfil"
    ).style.boxShadow = `0 0 10px ${resultado}`;
  });
});

// Ajusta el JavaScript para el menú desplegable
document.addEventListener('DOMContentLoaded', function() {
  const botonCuentaUsuario = document.querySelector('.boton-cuenta-usuario');
  const menuDesplegable = document.querySelector('.menu-desplegable');

  // Muestra u oculta el menú desplegable al hacer clic en la foto de perfil
  botonCuentaUsuario.addEventListener('click', function(event) {
    event.stopPropagation(); // Evita que el evento se propague al documento
    menuDesplegable.style.display = menuDesplegable.style.display === 'block' ? 'none' : 'block';
  });

  // Oculta el menú desplegable al hacer clic fuera de él
  document.addEventListener('click', function() {
    menuDesplegable.style.display = 'none';
  });
});
