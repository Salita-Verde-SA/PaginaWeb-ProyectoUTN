#!/bin/bash
# Add a user
curl -X POST http://localhost/api/usuarios -H "Content-Type: application/json" -d '{"nombre": "John Doe", "email": "john@example.com"}'

# Listen to the user creation event
curl -X GET http://localhost/api/usuarios

# Create a publication
curl -X POST http://localhost/api/publicaciones -H "Content-Type: application/json" -d '{"titulo": "My First Post", "contenido": "Hello World!", "usuarioId": 1}'

# Get publications by user
curl -X GET http://localhost/api/publicaciones/usuario/1

# Upload an image
curl -X POST -F "archivo=@/path/to/image.jpg" http://localhost/api/imagenes/subir

# Get an image
curl -X GET http://localhost/api/imagenes/image.jpg
