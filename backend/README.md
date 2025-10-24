# API REST - Salita Verde Backend

## Índice
- [Usuarios](#usuarios)
- [Publicaciones](#publicaciones)
- [Imágenes](#imágenes)
- [Acceso a Imágenes desde Frontend](#acceso-a-imágenes-desde-frontend)

---

## Usuarios

### GET /api/usuarios
Lista todos los usuarios.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/usuarios
```

**Ejemplo React:**
```jsx
const obtenerUsuarios = async () => {
  try {
    const response = await fetch('http://localhost:8090/api/usuarios');
    const usuarios = await response.json();
    console.log(usuarios);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### GET /api/usuarios/{id}
Obtiene usuario por ID.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/usuarios/123456789
```

**Ejemplo React:**
```jsx
const obtenerUsuario = async (id) => {
  try {
    const response = await fetch(`http://localhost:8090/api/usuarios/${id}`);
    const usuario = await response.json();
    console.log(usuario);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### POST /api/usuarios
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

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8090/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "12345678",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@example.com",
    "username": "juanp",
    "password": "secreto",
    "localidad": "CAPITAL"
  }'
```

**Ejemplo React:**
```jsx
const crearUsuario = async (datosUsuario) => {
  try {
    const response = await fetch('http://localhost:8090/api/usuarios', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datosUsuario)
    });
    const nuevoUsuario = await response.json();
    console.log('Usuario creado:', nuevoUsuario);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### PUT /api/usuarios/{id}
Actualiza usuario por ID.

**Ejemplo curl:**
```bash
curl -X PUT http://localhost:8090/api/usuarios/123456789 \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "123456789",
    "nombre": "Juan",
    "apellido": "Pérez Actualizado",
    "email": "juan.updated@example.com",
    "username": "juanp",
    "localidad": "GUAYMALLEN"
  }'
```

**Ejemplo React:**
```jsx
const actualizarUsuario = async (id, datosActualizados) => {
  try {
    const response = await fetch(`http://localhost:8090/api/usuarios/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datosActualizados)
    });
    const usuarioActualizado = await response.json();
    console.log('Usuario actualizado:', usuarioActualizado);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### DELETE /api/usuarios/{id}
Elimina usuario por ID.

**Ejemplo curl:**
```bash
curl -X DELETE http://localhost:8090/api/usuarios/123456789
```

**Ejemplo React:**
```jsx
const eliminarUsuario = async (id) => {
  try {
    await fetch(`http://localhost:8090/api/usuarios/${id}`, {
      method: 'DELETE'
    });
    console.log('Usuario eliminado');
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### POST /api/usuarios/{id}/seguir/{seguidoId}
El usuario `{id}` sigue al usuario `{seguidoId}`.

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8090/api/usuarios/123456789/seguir/12345678
```

**Ejemplo React:**
```jsx
const seguirUsuario = async (usuarioId, seguidoId) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/usuarios/${usuarioId}/seguir/${seguidoId}`,
      { method: 'POST' }
    );
    const resultado = await response.json();
    console.log('Ahora sigues a:', resultado);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### POST /api/usuarios/{id}/dejar-seguir/{seguidoId}
El usuario `{id}` deja de seguir a `{seguidoId}`.

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8090/api/usuarios/123456789/dejar-seguir/12345678
```

**Ejemplo React:**
```jsx
const dejarDeSeguir = async (usuarioId, seguidoId) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/usuarios/${usuarioId}/dejar-seguir/${seguidoId}`,
      { method: 'POST' }
    );
    const resultado = await response.json();
    console.log('Dejaste de seguir a:', resultado);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### GET /api/usuarios/{id}/sigue-a/{seguidoId}
Devuelve `true` si `{id}` sigue a `{seguidoId}`.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/usuarios/123456789/sigue-a/12345678
```

**Ejemplo React:**
```jsx
const verificarSiSigue = async (usuarioId, seguidoId) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/usuarios/${usuarioId}/sigue-a/${seguidoId}`
    );
    const sigue = await response.json();
    console.log('¿Sigue al usuario?:', sigue);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### PATCH /api/usuarios/{id}/settings
Actualiza solo la configuración del usuario.

**Payload ejemplo:**
```json
{
  "temaOscuro": true
}
```

**Ejemplo curl:**
```bash
curl -X PATCH http://localhost:8090/api/usuarios/123456789/settings \
  -H "Content-Type: application/json" \
  -d '{"temaOscuro": true}'
```

**Ejemplo React:**
```jsx
const actualizarSettings = async (usuarioId, settings) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/usuarios/${usuarioId}/settings`,
      {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(settings)
      }
    );
    const resultado = await response.json();
    console.log('Settings actualizados:', resultado);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

## Publicaciones

### GET /api/publicaciones
Lista todas las publicaciones visibles.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/publicaciones
```

**Ejemplo React:**
```jsx
const obtenerPublicaciones = async () => {
  try {
    const response = await fetch('http://localhost:8090/api/publicaciones');
    const publicaciones = await response.json();
    console.log(publicaciones);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### GET /api/publicaciones/{id}
Obtiene publicación por ID.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/publicaciones/507f1f77bcf86cd799439011
```

**Ejemplo React:**
```jsx
const obtenerPublicacion = async (id) => {
  try {
    const response = await fetch(`http://localhost:8090/api/publicaciones/${id}`);
    const publicacion = await response.json();
    console.log(publicacion);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### GET /api/publicaciones/usuario/{usuarioId}
Lista publicaciones de un usuario.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/publicaciones/usuario/123456789
```

**Ejemplo React:**
```jsx
const obtenerPublicacionesUsuario = async (usuarioId) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/publicaciones/usuario/${usuarioId}`
    );
    const publicaciones = await response.json();
    console.log(publicaciones);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### POST /api/publicaciones
Crea una publicación.

**Payload ejemplo:**
```json
{
  "usuarioId": "123456789",
  "username": "juanp",
  "titulo": "Evento solidario",
  "contenido": "¡Participa!",
  "imagenes": ["img1.jpg"],
  "etiquetas": ["solidaridad", "evento"],
  "entradasTotales": 100,
  "entradasDisponibles": 100
}
```

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8090/api/publicaciones \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": "123456789",
    "username": "juanp",
    "titulo": "Evento solidario",
    "contenido": "¡Participa!",
    "imagenes": ["img1.jpg"],
    "etiquetas": ["solidaridad", "evento"],
    "entradasTotales": 100,
    "entradasDisponibles": 100
  }'
```

**Ejemplo React:**
```jsx
const crearPublicacion = async (datosPublicacion) => {
  try {
    const response = await fetch('http://localhost:8090/api/publicaciones', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datosPublicacion)
    });
    const nuevaPublicacion = await response.json();
    console.log('Publicación creada:', nuevaPublicacion);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### PUT /api/publicaciones/{id}
Actualiza publicación.

**Ejemplo curl:**
```bash
curl -X PUT http://localhost:8090/api/publicaciones/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Evento solidario ACTUALIZADO",
    "contenido": "¡Nueva descripción!",
    "imagenes": ["img1.jpg", "img2.jpg"],
    "etiquetas": ["solidaridad", "evento", "actualizado"]
  }'
```

**Ejemplo React:**
```jsx
const actualizarPublicacion = async (id, datosActualizados) => {
  try {
    const response = await fetch(`http://localhost:8090/api/publicaciones/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datosActualizados)
    });
    const publicacionActualizada = await response.json();
    console.log('Publicación actualizada:', publicacionActualizada);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

### DELETE /api/publicaciones/{id}
Elimina (oculta) publicación.

**Ejemplo curl:**
```bash
curl -X DELETE http://localhost:8090/api/publicaciones/507f1f77bcf86cd799439011
```

**Ejemplo React:**
```jsx
const eliminarPublicacion = async (id) => {
  try {
    await fetch(`http://localhost:8090/api/publicaciones/${id}`, {
      method: 'DELETE'
    });
    console.log('Publicación eliminada (oculta)');
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

## Imágenes

### POST /api/imagenes/subir
Sube una imagen con nombre automático (UUID) o personalizado.

**Form-data:**
- `archivo`: archivo de imagen (requerido)
- `nombre`: nombre personalizado para la imagen (opcional)

**Respuesta exitosa:**
```json
{
  "nombreArchivo": "uuid_img.jpg",
  "url": "/api/imagenes/uuid_img.jpg"
}
```

**Respuesta de error (409 Conflict):**
```json
{
  "error": "Ya existe una imagen con el nombre: imagen.jpg"
}
```

**Ejemplo curl (nombre automático):**
```bash
curl -X POST http://localhost:8090/api/imagenes/subir \
  -F "archivo=@/ruta/a/imagen.jpg"
```

**Ejemplo curl (nombre personalizado):**
```bash
curl -X POST http://localhost:8090/api/imagenes/subir \
  -F "archivo=@/ruta/a/imagen.jpg" \
  -F "nombre=perfil-juan.jpg"
```

**Ejemplo React (con nombre automático):**
```jsx
const subirImagen = async (archivo) => {
  const formData = new FormData();
  formData.append('archivo', archivo);
  
  try {
    const response = await fetch('http://localhost:8090/api/imagenes/subir', {
      method: 'POST',
      body: formData
    });
    const resultado = await response.json();
    console.log('Imagen subida:', resultado);
    return resultado.nombreArchivo;
  } catch (error) {
    console.error('Error:', error);
  }
};

// Uso en un componente
const handleFileChange = (e) => {
  const archivo = e.target.files[0];
  if (archivo) {
    subirImagen(archivo);
  }
};
```

**Ejemplo React (con nombre personalizado):**
```jsx
const subirImagenConNombre = async (archivo, nombrePersonalizado) => {
  const formData = new FormData();
  formData.append('archivo', archivo);
  formData.append('nombre', nombrePersonalizado);
  
  try {
    const response = await fetch('http://localhost:8090/api/imagenes/subir', {
      method: 'POST',
      body: formData
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error);
    }
    
    const resultado = await response.json();
    console.log('Imagen subida:', resultado);
    return resultado.nombreArchivo;
  } catch (error) {
    console.error('Error:', error);
    alert(error.message);
  }
};
```

---

### PUT /api/imagenes/{nombreAntiguo}/renombrar
Renombra una imagen existente.

**Payload:**
```json
{
  "nombreNuevo": "nuevo-nombre.jpg"
}
```

**Respuesta exitosa:**
```json
{
  "nombreArchivo": "nuevo-nombre.jpg",
  "url": "/api/imagenes/nuevo-nombre.jpg",
  "mensaje": "Imagen renombrada exitosamente"
}
```

**Respuesta de error (409 Conflict):**
```json
{
  "error": "Ya existe una imagen con el nombre: nuevo-nombre.jpg"
}
```

**Ejemplo curl:**
```bash
curl -X PUT http://localhost:8090/api/imagenes/perfil-juan.jpg/renombrar \
  -H "Content-Type: application/json" \
  -d '{"nombreNuevo": "avatar-juan.jpg"}'
```

**Ejemplo React:**
```jsx
const renombrarImagen = async (nombreAntiguo, nombreNuevo) => {
  try {
    const response = await fetch(
      `http://localhost:8090/api/imagenes/${nombreAntiguo}/renombrar`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nombreNuevo })
      }
    );
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error);
    }
    
    const resultado = await response.json();
    console.log('Imagen renombrada:', resultado);
    return resultado.nombreArchivo;
  } catch (error) {
    console.error('Error:', error);
    alert(error.message);
  }
};
```

---

### GET /api/imagenes/{nombreArchivo}
Descarga/visualiza la imagen.

**Ejemplo curl:**
```bash
curl -X GET http://localhost:8090/api/imagenes/perfil-juan.jpg --output imagen.jpg
```

**Ejemplo React (mostrar imagen):**
```jsx
// Simplemente usa la URL en el atributo src
const MostrarImagen = ({ nombreArchivo }) => {
  return (
    <img 
      src={`http://localhost:8090/api/imagenes/${nombreArchivo}`}
