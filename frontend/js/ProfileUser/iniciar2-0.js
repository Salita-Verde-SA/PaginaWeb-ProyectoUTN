document.addEventListener('DOMContentLoaded', function() {

    // --- LÓGICA PARA LOS MENÚS DESPLEGABLES (ACORDEÓN) ---
    const acordeones = document.querySelectorAll('.acordeon-trigger');

    acordeones.forEach(acordeon => {
        acordeon.addEventListener('click', function() {
            // Cierra otros acordeones que puedan estar abiertos
            acordeones.forEach(item => {
                if (item !== this) {
                    item.classList.remove('active');
                    item.nextElementSibling.style.maxHeight = null;
                }
            });

            // Abre o cierra el acordeón actual
            this.classList.toggle('active');
            const panel = this.nextElementSibling;
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null; // Cierra el acordeón
            } else {
                // Abre el acordeón calculando su altura real
                panel.style.maxHeight = panel.scrollHeight + "px"; 
            }
        });
    });

    // --- LÓGICA PARA LOS AVATARES DE AMIGOS (CONSERVADA) ---
    const contenedoresAmigos = document.querySelectorAll('.Amigos-que-asistiran');
    const limiteAmigos = 4; // Mostrar solo 4 perfiles

    contenedoresAmigos.forEach(contenedor => {
        const amigos = contenedor.querySelectorAll('img');

        if (amigos.length > limiteAmigos) {
            // Oculta los perfiles que exceden el límite
            for (let i = limiteAmigos; i < amigos.length; i++) {
                amigos[i].style.display = 'none';
            }

            // Crea y añade el círculo con el contador
            const adicionales = amigos.length - limiteAmigos;
            const circulo = document.createElement('div');
            circulo.className = 'circulo-amigos-adicionales';
            circulo.textContent = `+${adicionales}`;
            contenedor.appendChild(circulo);
        }
    });

    // Aquí puedes agregar cualquier otra funcionalidad JS que necesites.
});