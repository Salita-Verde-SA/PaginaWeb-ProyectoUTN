#!/bin/bash

# Test backend connection
echo "Testing backend connection..."
curl -X GET http://localhost:8090/actuator/health

# Add a user
curl -X POST http://localhost:8090/api/usuarios -H "Content-Type: application/json" -d '{"dni":"12345678","nombre": "John","apellido":"Doe","email": "john@example.com","username":"johnd","password":"secreto"}'

# List users
curl -X GET http://localhost:8090/api/usuarios

# Create a publication (nota: ahora usuarioId es string/dni)
curl -X POST http://localhost:8090/api/publicaciones -H "Content-Type: application/json" -d '{"titulo": "My First Post", "contenido": "Hello World!", "usuarioId": "12345678"}'

# Get publications by user
curl -X GET http://localhost:8090/api/publicaciones/usuario/12345678

# Upload an image
curl -X POST -F "archivo=@/path/to/image.jpg" http://localhost:8090/api/imagenes/subir

# Get an image
curl -X GET http://localhost:8090/api/imagenes/image.jpg



# ---------------------------
# EJEMPLOS (comentados) de actualización de SETTINGS mediante PATCH
# ---------------------------
# Cambiar sólo el tema oscuro (PATCH settings)
# curl -X PATCH http://localhost/api/usuarios/12345678/settings \
#   -H "Content-Type: application/json" \
#   -d '{"temaOscuro": true}'

# Cambiar múltiples settings al mismo tiempo (ejemplos de campos adicionales)
# curl -X PATCH http://localhost/api/usuarios/12345678/settings \
#   -H "Content-Type: application/json" \
#   -d '{
#     "temaOscuro": false,
#     "notificaciones": true,
#     "idioma": "es"
#   }'

# Actualizar localidad (se utiliza PUT para actualizar el objeto usuario completo)
# curl -X PUT http://localhost/api/usuarios/12345678 \
#   -H "Content-Type: application/json" \
#   -d '{
#     "dni":"12345678",
#     "nombre":"John",
#     "apellido":"Doe",
#     "email":"john@example.com",
#     "username":"johnd",
#     "fotoPerfil":"/img/usuarios/john.jpg",
#     "localidad":"GUAYMALLEN",
#     "settings": {"temaOscuro": true}
#   }'
