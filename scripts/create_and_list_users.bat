@echo off
REM filepath: /home/manu/Documentos/Obsidian/Universidad/Programación/3/Laboratorio (Web)/Trabajo integrador/PaginaWeb-ProyectoUTN/scripts/create_and_list_users.bat

REM Probar conexión al backend
echo Probando conexión al backend...
curl -X GET http://localhost:8090/actuator/health

REM Añadir un usuario
echo.
echo.
echo Creando usuario...
curl -X POST http://localhost:8090/api/usuarios -H "Content-Type: application/json" -d "{\"dni\":\"123456789\",\"nombre\":\"John\",\"apellido\":\"Doe\",\"email\":\"john@example.com\",\"username\":\"johnd\",\"password\":\"secreto\",\"localidad\":\"CAPITAL\"}"

REM Listar usuarios
echo.
echo.
echo Listando usuarios...
curl -X GET http://localhost:8090/api/usuarios

REM Obtener usuario por ID
echo.
echo.
echo Obteniendo usuario por ID...
curl -X GET http://localhost:8090/api/usuarios/123456789

REM Obtener usuario por username
echo.
echo.
echo Obteniendo usuario por username...
curl -X GET http://localhost:8090/api/usuarios/username/johnd

REM Actualizar usuario (PUT)
echo.
echo.
echo Actualizando usuario completo...
curl -X PUT http://localhost:8090/api/usuarios/123456789 -H "Content-Type: application/json" -d "{\"dni\":\"123456789\",\"nombre\":\"John\",\"apellido\":\"Doe Updated\",\"email\":\"john.updated@example.com\",\"username\":\"johnd\",\"localidad\":\"GUAYMALLEN\"}"

REM Prueba de subida de foto de perfil (ajusta la ruta de la imagen)
echo.
echo.
echo Probando subida de foto de perfil...
REM curl -X POST -F "archivo=@C:\ruta\a\tu\imagen.jpg" http://localhost:8090/api/usuarios/123456789/foto-perfil

REM Eliminar usuario
REM echo.
REM echo.
REM echo Eliminando usuario...
REM curl -X DELETE http://localhost:8090/api/usuarios/123456789

REM ---------------------------
REM SEGUIR/DEJAR DE SEGUIR USUARIOS
REM ---------------------------

REM Seguir usuario (necesitas crear otro usuario primero)
REM echo.
REM echo.
REM echo Creando segundo usuario...
REM curl -X POST http://localhost:8090/api/usuarios -H "Content-Type: application/json" -d "{\"dni\":\"12345678\",\"nombre\":\"Jane\",\"apellido\":\"Smith\",\"email\":\"jane@example.com\",\"username\":\"janes\",\"password\":\"secreto123\"}"

REM Usuario 123456789 sigue a 12345678
echo.
echo.
echo Usuario 123456789 sigue a 12345678...
curl -X POST http://localhost:8090/api/usuarios/123456789/seguir/12345678

REM Consultar si sigue
echo.
echo.
echo Consultando si 123456789 sigue a 12345678...
curl -X GET http://localhost:8090/api/usuarios/123456789/sigue-a/12345678

REM Dejar de seguir
echo.
echo.
echo Usuario 123456789 deja de seguir a 12345678...
curl -X POST http://localhost:8090/api/usuarios/123456789/dejar-seguir/12345678

REM Crear tercer usuario distinto
echo.
echo.
echo Creando tercer usuario...
curl -X POST http://localhost:8090/api/usuarios -H "Content-Type: application/json" -d "{\"dni\":\"98765432\",\"nombre\":\"Carlos\",\"apellido\":\"Rodriguez\",\"email\":\"carlos.rodriguez@example.com\",\"username\":\"carlosr\",\"password\":\"mipassword456\",\"localidad\":\"GUAYMALLEN\"}"

REM ---------------------------
REM ACTUALIZAR SETTINGS (PATCH)
REM ---------------------------

REM Cambiar tema a claro
echo.
echo.
echo Actualizando settings - tema claro...
curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings -H "Content-Type: application/json" -d "{\"temaOscuro\": false}"

REM Cambiar tema a oscuro
echo.
echo.
echo Actualizando settings - tema oscuro...
curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings -H "Content-Type: application/json" -d "{\"temaOscuro\": true}"

REM Si añades más campos a Settings, puedes enviar múltiples:
REM curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings -H "Content-Type: application/json" -d "{\"temaOscuro\": false,\"notificaciones\": true,\"idioma\": \"es\"}"

REM ---------------------------
REM PUBLICACIONES
REM ---------------------------

REM Crear una publicación (usuarioId es String/dni)
echo.
echo.
echo Creando publicación...
curl -X POST http://localhost:8090/api/publicaciones -H "Content-Type: application/json" -d "{\"titulo\": \"My First Post\",\"contenido\": \"Hello World!\",\"usuarioId\": \"123456789\"}"

REM Obtener publicaciones por usuario
echo.
echo.
echo Obteniendo publicaciones del usuario...
curl -X GET http://localhost:8090/api/publicaciones/usuario/123456789

REM Obtener todas las publicaciones
echo.
echo.
echo Obteniendo todas las publicaciones...
curl -X GET http://localhost:8090/api/publicaciones

REM ---------------------------
REM IMÁGENES
REM ---------------------------

REM Subir una imagen con nombre automático (UUID)
echo.
echo.
echo Subiendo imagen con nombre automático...
REM curl -X POST -F "archivo=@C:\path\to\image.jpg" http://localhost:8090/api/imagenes/subir

REM Subir una imagen con nombre personalizado
echo.
echo.
echo Subiendo imagen con nombre personalizado...
REM curl -X POST -F "archivo=@C:\path\to\image.jpg" -F "nombre=perfil-john.jpg" http://localhost:8090/api/imagenes/subir

REM Intentar subir imagen con nombre duplicado (debe fallar)
echo.
echo.
echo Intentando subir imagen con nombre duplicado...
REM curl -X POST -F "archivo=@C:\path\to\image.jpg" -F "nombre=perfil-john.jpg" http://localhost:8090/api/imagenes/subir

REM Renombrar una imagen
echo.
echo.
echo Renombrando imagen...
REM curl -X PUT http://localhost:8090/api/imagenes/perfil-john.jpg/renombrar -H "Content-Type: application/json" -d "{\"nombreNuevo\": \"avatar-john.jpg\"}"

REM Intentar renombrar a un nombre que ya existe (debe fallar)
echo.
echo.
echo Intentando renombrar a nombre existente...
REM curl -X PUT http://localhost:8090/api/imagenes/avatar-john.jpg/renombrar -H "Content-Type: application/json" -d "{\"nombreNuevo\": \"otro-nombre-existente.jpg\"}"

REM Obtener una imagen
echo.
echo.
echo Obteniendo imagen...
REM curl -X GET http://localhost:8090/api/imagenes/avatar-john.jpg

REM Eliminar una imagen
echo.
echo.
echo Eliminando imagen...
REM curl -X DELETE http://localhost:8090/api/imagenes/avatar-john.jpg

REM Intentar eliminar una imagen que no existe (debe fallar)
echo.
echo.
echo Intentando eliminar imagen inexistente...
REM curl -X DELETE http://localhost:8090/api/imagenes/imagen-inexistente.jpg

echo.
echo.
echo Script completado!
REM ---------------------------