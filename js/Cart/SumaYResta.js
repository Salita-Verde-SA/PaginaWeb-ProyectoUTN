// Manejo seguro de botones de cantidad
document.querySelectorAll('.menos').forEach(btn => {
  btn.addEventListener('click', () => {
    const valorSpan = btn.parentElement.querySelector('.valor');
    if (valorSpan) {
      let valor = parseInt(valorSpan.textContent);
      if (valor > 0) {
        valorSpan.textContent = valor - 1;
        actualizarFactura(); // Si tenés esta función activa
      }
    }
  });
});

document.querySelectorAll('.mas').forEach(btn => {
  btn.addEventListener('click', () => {
    const valorSpan = btn.parentElement.querySelector('.valor');
    if (valorSpan) {
      let valor = parseInt(valorSpan.textContent);
      valorSpan.textContent = valor + 1;
      actualizarFactura(); // Si tenés esta función activa
    }
  });
});

// Función para alternar el menú de usuario
function toggleMenu() {
  const userMenu = document.getElementById("userMenu");
  if (!userMenu) return;

  userMenu.classList.toggle("active");

  document.addEventListener("click", function(event) {
    const perfilHeader = document.querySelector(".perfil-header");
    if (!perfilHeader || !userMenu) return;

    if (!perfilHeader.contains(event.target)) {
      userMenu.classList.remove("active");
    }
  });
}
