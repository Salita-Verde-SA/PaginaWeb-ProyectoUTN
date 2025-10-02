
// TODO : Eliminar lo siguiebte si no se usa o si se implementa de otra forma más adelante, agregar condicional para que no falle en páginas sin estos elemento
 document.querySelector('.menos').addEventListener('click', () => {
   let valor = parseInt(document.querySelector('.valor').textContent);
   if (valor > 0) {
     document.querySelector('.valor').textContent = valor - 1;
   }
 })
 document.querySelector('.mas').addEventListener('click', () => {
   let valor = parseInt(document.querySelector('.valor').textContent);
   document.querySelector('.valor').textContent = valor + 1;
 });
 /**
  * Función para alternar la visibilidad del menú de usuario.
  * Si el menú está oculto, lo muestra, y viceversa.
  */
 function toggleMenu() {
   // Selecciona el elemento del menú de usuario por su ID
   const userMenu = document.getElementById("userMenu");
   // Alterna la clase 'active' en el menú de usuario.
   // Si la clase está presente, la quita (ocultando el menú).
   // Si no está presente, la añade (mostrando el menú).
   userMenu.classList.toggle("active");
 
 // Escucha los clics en cualquier parte del documento
 document.addEventListener("click", function(event) {
   // Selecciona el elemento del menú de usuario por su ID
   const userMenu = document.getElementById("userMenu");
   // Selecciona el elemento del encabezado del perfil por su clase
   const perfilHeader = document.querySelector(".perfil-header")
   // Verifica si el clic ocurrió fuera del área del perfil if (!perfilHeader.contains(event.target))    // Si el clic fue fuera del área del perfil, remueve la clase 'active'
     // del menú de usuario, ocultándolo
     userMenu.classList.remove("active");
 });
 }
