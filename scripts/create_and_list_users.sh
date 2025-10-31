#!/bin/bash

# Probar conexión al backend
echo "Probando conexión al backend..."
curl -X GET http://localhost:8090/actuator/health

# Añadir un usuario
echo -e "\n\nCreando usuario..."
RESPONSE=$(curl -s -X POST http://localhost:8090/api/usuarios \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"123456789",
    "nombre":"John",
    "apellido":"Doe",
    "email":"john@example.com",
    "username":"johnd",
    "password":"secreto",
    "localidad":"CAPITAL"
  }')

echo "$RESPONSE"

# Extraer el ID del usuario creado (requiere jq)
# Si no tienes jq instalado: sudo apt-get install jq
USER_ID=$(echo "$RESPONSE" | jq -r '.id')
echo -e "\n\nID del usuario creado: $USER_ID"

# Listar usuarios
echo -e "\n\nListando usuarios..."
curl -X GET http://localhost:8090/api/usuarios

# Obtener usuario por ID (usando el ID de MongoDB)
echo -e "\n\nObteniendo usuario por ID..."
curl -X GET "http://localhost:8090/api/usuarios/$USER_ID"

# Obtener usuario por username
echo -e "\n\nObteniendo usuario por username..."
curl -X GET "http://localhost:8090/api/usuarios/username/johnd"

# Actualizar usuario (PUT) usando el ID de MongoDB
echo -e "\n\nActualizando usuario completo..."
curl -X PUT "http://localhost:8090/api/usuarios/$USER_ID" \
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

# Eliminar seguidor (12345678 deja de seguir a 123456789)
echo -e "\n\nUsuario 123456789 elimina a 12345678 de sus seguidores..."
curl -X POST http://localhost:8090/api/usuarios/123456789/eliminar-seguidor/12345678

# Dejar de seguir
echo -e "\n\nUsuario 123456789 deja de seguir a 12345678..."
curl -X POST http://localhost:8090/api/usuarios/123456789/dejar-seguir/12345678

# Crear tercer usuario distinto
echo -e "\n\nCreando tercer usuario..."
RESPONSE_USER3=$(curl -s -X POST http://localhost:8090/api/usuarios \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"98765432",
    "nombre":"Carlos",
    "apellido":"Rodriguez",
    "email":"carlos.rodriguez@example.com",
    "username":"carlosr",
    "password":"mipassword456",
    "localidad":"GUAYMALLEN"
  }')

echo "$RESPONSE_USER3"

# ---------------------------
# ACTUALIZAR SETTINGS (PATCH) - Usando ID de MongoDB
# ---------------------------

# Cambiar el tema oscuro a false (modo claro)
echo -e "\n\nActualizando settings - tema claro..."
curl -X PATCH "http://localhost:8090/api/usuarios/$USER_ID/settings" \
	-H "Content-Type: application/json" \
	-d '{"temaOscuro": false}'

# Cambiar el tema oscuro a true (modo oscuro - valor por defecto)
echo -e "\n\nActualizando settings - tema oscuro..."
curl -X PATCH "http://localhost:8090/api/usuarios/$USER_ID/settings" \
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

# Subir una imagen con nombre automático (UUID)
echo -e "\n\nSubiendo imagen con nombre automático..."
# curl -X POST -F "archivo=@/path/to/image.jpg" http://localhost:8090/api/imagenes/subir

# Subir una imagen con nombre personalizado
echo -e "\n\nSubiendo imagen con nombre personalizado..."
# curl -X POST -F "archivo=@/path/to/image.jpg" -F "nombre=perfil-john.jpg" http://localhost:8090/api/imagenes/subir

# Intentar subir imagen con nombre duplicado (debe fallar)
echo -e "\n\nIntentando subir imagen con nombre duplicado..."
# curl -X POST -F "archivo=@/path/to/image.jpg" -F "nombre=perfil-john.jpg" http://localhost:8090/api/imagenes/subir

# Renombrar una imagen
echo -e "\n\nRenombrando imagen..."
# curl -X PUT http://localhost:8090/api/imagenes/perfil-john.jpg/renombrar \
#   -H "Content-Type: application/json" \
#   -d '{"nombreNuevo": "avatar-john.jpg"}'

# Intentar renombrar a un nombre que ya existe (debe fallar)
echo -e "\n\nIntentando renombrar a nombre existente..."
# curl -X PUT http://localhost:8090/api/imagenes/avatar-john.jpg/renombrar \
#   -H "Content-Type: application/json" \
#   -d '{"nombreNuevo": "otro-nombre-existente.jpg"}'

# Obtener una imagen
echo -e "\n\nObteniendo imagen..."
# curl -X GET http://localhost:8090/api/imagenes/avatar-john.jpg

# Eliminar una imagen
echo -e "\n\nEliminando imagen..."
# curl -X DELETE http://localhost:8090/api/imagenes/avatar-john.jpg

# Intentar eliminar una imagen que no existe (debe fallar)
echo -e "\n\nIntentando eliminar imagen inexistente..."
# curl -X DELETE http://localhost:8090/api/imagenes/imagen-inexistente.jpg

echo -e "\n\nScript completado!"

# ---------------------------
# AUTENTICACIÓN
# ---------------------------

echo -e "\n\n=== PROBANDO AUTENTICACIÓN ==="

# Registrar un nuevo usuario
echo -e "\n\nRegistrando nuevo usuario..."
curl -X POST http://localhost:8090/api/auth/register \
	-H "Content-Type: application/json" \
	-d '{
    "dni":"87654321",
    "nombre":"Maria",
    "apellido":"Garcia",
    "email":"maria@example.com",
    "username":"mariag",
    "password":"password123",
    "localidad":"GODOY_CRUZ"
  }' \
	-c cookies.txt

# Login
echo -e "\n\nIniciando sesión..."
curl -X POST http://localhost:8090/api/auth/login \
	-H "Content-Type: application/json" \
	-d '{
    "username":"mariag",
    "password":"password123"
  }' \
	-c cookies.txt

# Validar token
echo -e "\n\nValidando token..."
curl -X GET http://localhost:8090/api/auth/validate \
	-b cookies.txt

# Logout
echo -e "\n\nCerrando sesión..."
curl -X POST http://localhost:8090/api/auth/logout \
	-b cookies.txt \
	-c cookies.txt

# Intentar validar después del logout (debería fallar)
echo -e "\n\nIntentando validar después del logout..."
curl -X GET http://localhost:8090/api/auth/validate \
	-b cookies.txt

echo -e "\n\n=== FIN DE PRUEBAS DE AUTENTICACIÓN ==="
echo -e "\n\n=== FIN DE PRUEBAS DE AUTENTICACIÓN ==="

echo -e "\n\n=== PROBANDO ADMINISTRADORES ==="

# Registrar un nuevo administrador
echo -e "\n\nRegistrando nuevo administrador..."
ADMIN_RESPONSE=$(curl -s -X POST http://localhost:8090/api/auth/register-admin \
  -H "Content-Type: application/json" \
  -d '{
    "dni":"87654321",
    "nombre":"Carlos",
    "apellido":"Mendez",
    "fechaNacimiento":"1985-05-15",
    "nombreOrganizacion":"Eventos Premium SRL",
    "cuit":"20876543219",
    "rubro":"Entretenimiento",
    "direccionLugar":"Av. Principal 1000, Mendoza",
    "sitioWeb":"www.eventospremium.com",
    "email":"carlos@eventospremium.com",
    "celular":"+54 261 555-1234",
    "username":"carlospremium",
    "password":"admin123",
    "localidad":"CAPITAL"
  }' \
  -c admin_cookies.txt \
  -w "\nHTTP Status: %{http_code}\n")

echo "$ADMIN_RESPONSE"

ADMIN_ID=$(echo "$ADMIN_RESPONSE" | jq -r '.id')
echo -e "\n\nID del administrador creado: $ADMIN_ID"

# Subir documentos
echo -e "\n\nSimulando subida de documentos..."
# curl -X POST http://localhost:8090/api/administradores/$ADMIN_ID/dni-frente \
#   -F "archivo=@/path/to/dni-frente.jpg" \
#   -b admin_cookies.txt

echo -e "\n\n=== FIN DE PRUEBAS DE ADMINISTRADORES ==="
