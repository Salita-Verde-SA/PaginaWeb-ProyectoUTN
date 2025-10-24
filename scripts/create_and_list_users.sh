#!/bin/bash

# Probar conexión al backend
echo "Probando conexión al backend..."
curl -X GET http://localhost:8090/actuator/health

# Añadir un usuario
echo -e "\n\nCreando usuario..."
curl -X POST http://localhost:8090/api/usuarios \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"123456789",
    "nombre":"John",
    "apellido":"Doe",
    "email":"john@example.com",
    "username":"johnd",
    "password":"secreto"
  }'

# Listar usuarios
echo -e "\n\nListando usuarios..."
curl -X GET http://localhost:8090/api/usuarios

# Obtener usuario por ID
echo -e "\n\nObteniendo usuario por ID..."
curl -X GET http://localhost:8090/api/usuarios/123456789

# Actualizar usuario (PUT)
echo -e "\n\nActualizando usuario completo..."
curl -X PUT http://localhost:8090/api/usuarios/123456789 \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"123456789",
    "nombre":"John",
    "apellido":"Doe Updated",
    "email":"john.updated@example.com",
    "username":"johnd",
    "fotoPerfil":"/img/usuarios/john.jpg",
    "localidad":"GUAYMALLEN"
  }'

# Eliminar usuario
# echo -e "\n\nEliminando usuario..."
# curl -X DELETE http://localhost:8090/api/usuarios/123456789

# ---------------------------
# SEGUIR/DEJAR DE SEGUIR USUARIOS
# ---------------------------

# Seguir usuario (necesitas crear otro usuario primero)
# echo -e "\n\nCreando segundo usuario..."
# curl -X POST http://localhost:8090/api/usuarios \
#   -H "Content-Type: application/json" \
#   -d '{
#     "dni":"12345678",
#     "nombre":"Jane",
#     "apellido":"Smith",
#     "email":"jane@example.com",
#     "username":"janes",
#     "password":"secreto123"
#   }'

# Usuario 123456789 sigue a 12345678
echo -e "\n\nUsuario 123456789 sigue a 12345678..."
curl -X POST http://localhost:8090/api/usuarios/123456789/seguir/12345678

# Consultar si sigue
echo -e "\n\nConsultando si 123456789 sigue a 12345678..."
curl -X GET http://localhost:8090/api/usuarios/123456789/sigue-a/12345678

# Dejar de seguir
echo -e "\n\nUsuario 123456789 deja de seguir a 12345678..."
curl -X POST http://localhost:8090/api/usuarios/123456789/dejar-seguir/12345678

# Crear tercer usuario distinto
echo -e "\n\nCreando tercer usuario..."
curl -X POST http://localhost:8090/api/usuarios \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"98765432",
    "nombre":"Carlos",
    "apellido":"Rodriguez",
    "email":"carlos.rodriguez@example.com",
    "username":"carlosr",
    "password":"mipassword456"
  }'

# ---------------------------
# ACTUALIZAR SETTINGS (PATCH)
# ---------------------------

# Cambiar sólo el tema oscuro
echo -e "\n\nActualizando settings - tema oscuro..."
curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings \
	-H "Content-Type: application/json" \
	-d '{"temaOscuro": true}'

# Si añades más campos a Settings, puedes enviar múltiples:
# curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings \
#   -H "Content-Type: application/json" \
#   -d '{
#     "temaOscuro": false,
#     "notificaciones": true,
#     "idioma": "es"
#   }'

# ---------------------------
# PUBLICACIONES
# ---------------------------

# Crear una publicación (usuarioId es String/dni)
echo -e "\n\nCreando publicación..."
curl -X POST http://localhost:8090/api/publicaciones \
	-H "Content-Type: application/json" \
	-d '{
    "titulo": "My First Post",
    "contenido": "Hello World!",
    "usuarioId": "123456789"
  }'

# Obtener publicaciones por usuario
echo -e "\n\nObteniendo publicaciones del usuario..."
curl -X GET http://localhost:8090/api/publicaciones/usuario/123456789

# Obtener todas las publicaciones
echo -e "\n\nObteniendo todas las publicaciones..."
curl -X GET http://localhost:8090/api/publicaciones

# ---------------------------
# IMÁGENES
# ---------------------------

# Subir una imagen (actualiza la ruta de la imagen)
# echo -e "\n\nSubiendo imagen..."
# curl -X POST -F "archivo=@/path/to/image.jpg" http://localhost:8090/api/imagenes/subir

# Obtener una imagen
# echo -e "\n\nObteniendo imagen..."
# curl -X GET http://localhost:8090/api/imagenes/image.jpg

# Eliminar una imagen
# echo -e "\n\nEliminando imagen..."
# curl -X DELETE http://localhost:8090/api/imagenes/image.jpg

echo -e "\n\nScript completado!"
# ---------------------------
