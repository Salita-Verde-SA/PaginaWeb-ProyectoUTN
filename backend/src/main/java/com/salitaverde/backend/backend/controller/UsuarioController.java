package com.salitaverde.backend.backend.controller;

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

    @GetMapping("/{id}/foto-perfil")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable String id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            InputStream imagen = imagenService.obtenerImagen(usuario.getFotoPerfil());
            byte[] bytes = imagen.readAllBytes();
            
            HttpHeaders headers = new HttpHeaders();
            String extension = usuario.getFotoPerfil().substring(usuario.getFotoPerfil().lastIndexOf(".") + 1);
            headers.setContentType(MediaType.parseMediaType("image/" + extension));

            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}