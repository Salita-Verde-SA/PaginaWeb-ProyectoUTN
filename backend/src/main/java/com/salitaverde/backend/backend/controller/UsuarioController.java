package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Localidad;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.service.UsuarioService;
import com.salitaverde.backend.backend.service.AuthService;
import com.salitaverde.backend.backend.service.ImagenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final ImagenService imagenService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> obtenerPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.obtenerPorUsername(username));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable String id,
            @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/seguir/{seguidoId}")
    public ResponseEntity<Usuario> seguirUsuario(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        Usuario actualizado = usuarioService.seguirUsuario(id, seguidoId);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/dejar-seguir/{seguidoId}")
    public ResponseEntity<Usuario> dejarDeSeguirUsuario(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        Usuario actualizado = usuarioService.dejarDeSeguirUsuario(id, seguidoId);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/eliminar-seguidor/{seguidorId}")
    public ResponseEntity<Usuario> eliminarSeguidor(
            @PathVariable String id,
            @PathVariable String seguidorId) {
        Usuario actualizado = usuarioService.eliminarSeguidor(id, seguidorId);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}/sigue-a/{seguidoId}")
    public ResponseEntity<Boolean> sigueA(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        boolean sigue = usuarioService.sigueA(id, seguidoId);
        return ResponseEntity.ok(sigue);
    }

    @PatchMapping("/{id}/settings")
    public ResponseEntity<Usuario> actualizarSettings(
            @PathVariable String id,
            @RequestBody Usuario.Settings settings) {
        Usuario actualizado = usuarioService.actualizarSettings(id, settings);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/registrar-asistencia")
    public ResponseEntity<Usuario> registrarAsistenciaEvento(@PathVariable String id) {
        Usuario actualizado = usuarioService.registrarAsistenciaEvento(id);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}/eventos-asistidos")
    public ResponseEntity<Integer> obtenerCantidadEventosAsistidos(@PathVariable String id) {
        Integer cantidad = usuarioService.obtenerCantidadEventosAsistidos(id);
        return ResponseEntity.ok(cantidad);
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<?> actualizarUsername(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            HttpServletResponse response) {
        try {
            String nuevoUsername = body.get("username");
            if (nuevoUsername == null || nuevoUsername.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "El username no puede estar vacío"));
            }

            Usuario usuario = usuarioService.actualizarNombreUsuario(id, nuevoUsername);

            String nuevoToken = authService.regenerarToken(id);

            Cookie cookie = new Cookie("authToken", nuevoToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60);
            response.addCookie(cookie);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("mensaje", "Username actualizado correctamente");
            responseBody.put("token", nuevoToken);
            responseBody.put("usuario", usuario);

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/{id}/foto-perfil")
    public ResponseEntity<Usuario> actualizarFotoPerfil(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(usuarioService.actualizarFotoPerfil(id, archivo));
    }

    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            String contrasenaActual = body.get("contrasenaActual");
            String contrasenaNueva = body.get("contrasenaNueva");
            
            if (contrasenaActual == null || contrasenaActual.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "La contraseña actual es requerida"));
            }
            if (contrasenaNueva == null || contrasenaNueva.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "La nueva contraseña es requerida"));
            }
            if (contrasenaNueva.length() < 8) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "La nueva contraseña debe tener al menos 8 caracteres"));
            }
            
            usuarioService.cambiarContrasena(id, contrasenaActual, contrasenaNueva);
            
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/deshabilitar")
    public ResponseEntity<?> deshabilitarCuenta(@PathVariable String id) {
        try {
            usuarioService.deshabilitarCuenta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta deshabilitada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/habilitar")
    public ResponseEntity<?> habilitarCuenta(@PathVariable String id) {
        try {
            usuarioService.habilitarCuenta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta habilitada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}/foto-perfil")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable String id) {
        Usuario usuario = usuarioService.obtenerPorId(id);

        // Verificar si el usuario tiene foto de perfil
        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            InputStream imagen = imagenService.obtenerImagen(usuario.getFotoPerfil());
            byte[] bytes = imagen.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            String extension = usuario.getFotoPerfil().substring(usuario.getFotoPerfil().lastIndexOf(".") + 1);
            headers.setContentType(MediaType.parseMediaType("image/" + extension));
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            System.out.println("Error al obtener foto de perfil: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/localidad-actual")
    public ResponseEntity<?> obtenerLocalidadActual(
            @CookieValue(value = "authToken", required = false) String token) {
        
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "No autenticado"));
        }

        try {
            String usuarioId = authService.validarToken(token);
            if (usuarioId == null) {
                return ResponseEntity.status(401).body(Map.of("mensaje", "Token inválido"));
            }

            Usuario usuario = usuarioService.obtenerPorId(usuarioId);
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of("mensaje", "Usuario no encontrado"));
            }

            Localidad localidad = usuario.getLocalidad() != null ? usuario.getLocalidad() : Localidad.CAPITAL;
            
            return ResponseEntity.ok(Map.of(
                "localidad", localidad.name(),
                "nombre", localidad.getNombre()
            ));
        } catch (Exception e) {
            System.out.println("Error al obtener localidad: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener localidad"));
        }
    }

    @PutMapping("/actualizar-localidad")
    public ResponseEntity<?> actualizarLocalidad(
            @RequestBody Map<String, String> requestBody,
            @CookieValue(value = "authToken", required = false) String token) {

        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "No autenticado"));
        }

        try {
            String usuarioId = authService.validarToken(token);
            if (usuarioId == null) {
                return ResponseEntity.status(401).body(Map.of("mensaje", "Token inválido"));
            }

            String localidadStr = requestBody.get("localidad");
            if (localidadStr == null || localidadStr.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Localidad no puede estar vacía"));
            }

            Localidad localidad = Localidad.valueOf(localidadStr);

            Usuario usuario = usuarioService.obtenerPorId(usuarioId);
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of("mensaje", "Usuario no encontrado"));
            }

            usuario.setLocalidad(localidad);
            usuarioService.guardar(usuario);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Localidad actualizada exitosamente",
                    "localidad", localidad.name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Localidad no válida: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("Error al actualizar localidad: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al actualizar localidad: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/seguidores")
    public ResponseEntity<List<String>> obtenerSeguidores(@PathVariable String id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario.getSeguidores());
    }

    @GetMapping("/{id}/seguidos")
    public ResponseEntity<List<String>> obtenerSeguidos(@PathVariable String id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario.getSeguidos());
    }

    // NUEVOS ENDPOINTS: Obtener datos completos de seguidores/seguidos
    @GetMapping("/{id}/seguidores/detalles")
    public ResponseEntity<List<Usuario>> obtenerSeguidoresDetalles(@PathVariable String id) {
        List<Usuario> seguidores = usuarioService.obtenerSeguidoresDetalles(id);
        return ResponseEntity.ok(seguidores);
    }

    @GetMapping("/{id}/seguidos/detalles")
    public ResponseEntity<List<Usuario>> obtenerSeguidosDetalles(@PathVariable String id) {
        List<Usuario> seguidos = usuarioService.obtenerSeguidosDetalles(id);
        return ResponseEntity.ok(seguidos);
    }

    @PatchMapping("/{id}/toggle-gerente")
    public ResponseEntity<Usuario> toggleGerente(@PathVariable String id) {
        Usuario usuario = usuarioService.toggleGerente(id);
        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/{id}/set-gerente")
    public ResponseEntity<Usuario> setGerente(
            @PathVariable String id,
            @RequestBody Map<String, Boolean> body) {
        boolean esGerente = body.getOrDefault("gerente", false);
        Usuario usuario = usuarioService.setGerente(id, esGerente);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/gerentes")
    public ResponseEntity<List<Usuario>> obtenerGerentes() {
        return ResponseEntity.ok(usuarioService.obtenerGerentes());
    }
}