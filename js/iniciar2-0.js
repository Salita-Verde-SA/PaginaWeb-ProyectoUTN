document.addEventListener('DOMContentLoaded', function() {
  const contenedoresAmigos = document.querySelectorAll('.Amigos-que-asistiran');
  const limiteAmigos = 4; // Mostrar solo 4 perfiles, el resto se indica en el círculo

  contenedoresAmigos.forEach(contenedor => {
    const amigos = contenedor.querySelectorAll('img');

    // Si hay más de 4 amigos
    if (amigos.length > limiteAmigos) {
      // Oculta los perfiles adicionales (a partir del quinto)
      for (let i = limiteAmigos; i < amigos.length; i++) {
        amigos[i].style.display = 'none';
      }

      // Crea el círculo con el número de amigos adicionales
      const adicionales = amigos.length - limiteAmigos;
      const circulo = document.createElement('div');
      circulo.className = 'circulo-amigos-adicionales';
      circulo.textContent = `+${adicionales}`;

      // Agrega el círculo al contenedor
      contenedor.appendChild(circulo);
    }
  });
});
