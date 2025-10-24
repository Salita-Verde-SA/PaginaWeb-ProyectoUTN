# API REST - Salita Verde Backend

## Usuarios

- **GET /api/usuarios**  
  Lista todos los usuarios.

- **GET /api/usuarios/{id}**  
  Obtiene usuario por ID.

- **POST /api/usuarios**  
  Crea un usuario.  
  **Payload ejemplo:**  

  ```json
  {
    "dni": "12345678",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@example.com",
    "username": "juanp",
    "password": "secreto",
    "localidad": "CAPITAL"
  }
  ```

- **PUT /api/usuarios/{id}**  
  Actualiza usuario por ID.

- **DELETE /api/usuarios/{id}**  
  Elimina usuario por ID.

- **POST /api/usuarios/{id}/seguir/{seguidoId}**  
  El usuario `{id}` sigue al usuario `{seguidoId}`.

- **POST /api/usuarios/{id}/dejar-seguir/{seguidoId}**  
  El usuario `{id}` deja de seguir a `{seguidoId}`.

- **GET /api/usuarios/{id}/sigue-a/{seguidoId}**  
  Devuelve `true` si `{id}` sigue a `{seguidoId}`.

- **PATCH /api/usuarios/{id}/settings**  
  Actualiza solo la configuración del usuario.  
  **Payload ejemplo:**  

  ```json
  {
    "temaOscuro": true
  }
  ```

## Publicaciones

- **GET /api/publicaciones**  
  Lista todas las publicaciones visibles.

- **GET /api/publicaciones/{id}**  
  Obtiene publicación por ID.

- **GET /api/publicaciones/usuario/{usuarioId}**  
  Lista publicaciones de un usuario.

- **POST /api/publicaciones**  
  Crea una publicación.  
  **Payload ejemplo:**  

  ```json
  {
    "usuarioId": "1",
    "username": "juanp",
    "titulo": "Evento solidario",
    "contenido": "¡Participa!",
    "imagenes": ["img1.jpg"],
    "etiquetas": ["solidaridad", "evento"],
    "entradasTotales": 100,
    "entradasDisponibles": 100
  }
  ```

- **PUT /api/publicaciones/{id}**  
  Actualiza publicación.

- **DELETE /api/publicaciones/{id}**  
  Elimina (oculta) publicación.

## Imágenes

- **POST /api/imagenes/subir**  
  Sube una imagen.  
  **Form-data:**  
  - `archivo`: archivo de imagen  
  **Respuesta:**  

  ```json
  {
    "nombreArchivo": "uuid_img.jpg",
    "url": "/api/imagenes/uuid_img.jpg"
  }
  ```

- **GET /api/imagenes/{nombreArchivo}**  
  Descarga la imagen.

- **DELETE /api/imagenes/{nombreArchivo}**  
  Elimina la imagen.

---

## Notas

- Todas las rutas son relativas a `/`.
- El backend usa MongoDB y MinIO para almacenamiento.
- Los endpoints pueden requerir autenticación en producción.
