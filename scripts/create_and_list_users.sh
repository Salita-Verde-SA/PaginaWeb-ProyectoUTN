#!/bin/bash

# Test backend connection
echo "Testing backend connection..."
curl -X GET http://localhost:8090/actuator/health

# Add a user
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

# List users
echo -e "\n\nListando usuarios..."
curl -X GET http://localhost:8090/api/usuarios

# Get user by ID
echo -e "\n\nObteniendo usuario por ID..."
curl -X GET http://localhost:8090/api/usuarios/123456789

# Update user (PUT)
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

# Delete user
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

# Create a publication (usuarioId es String/dni)
echo -e "\n\nCreando publicación..."
curl -X POST http://localhost:8090/api/publicaciones \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "My First Post",
    "contenido": "Hello World!",
    "usuarioId": "123456789"
  }'

# Get publications by user
echo -e "\n\nObteniendo publicaciones del usuario..."
curl -X GET http://localhost:8090/api/publicaciones/usuario/123456789

# Get all publications
echo -e "\n\nObteniendo todas las publicaciones..."
curl -X GET http://localhost:8090/api/publicaciones

# ---------------------------
# IMÁGENES
# ---------------------------

# Upload an image (actualiza la ruta de la imagen)
# echo -e "\n\nSubiendo imagen..."
# curl -X POST -F "archivo=@/path/to/image.jpg" http://localhost:8090/api/imagenes/subir

# Get an image
# echo -e "\n\nObteniendo imagen..."
# curl -X GET http://localhost:8090/api/imagenes/image.jpg

# Delete an image
# echo -e "\n\nEliminando imagen..."
# curl -X DELETE http://localhost:8090/api/imagenes/image.jpg

echo -e "\n\nScript completado!"
# ---------------------------
