package com.salitaverde.backend.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

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


    // Seguir usuario
    @PostMapping("/{id}/seguir/{seguidoId}")
    public ResponseEntity<Usuario> seguirUsuario(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        Usuario actualizado = usuarioService.seguirUsuario(id, seguidoId);
        return ResponseEntity.ok(actualizado);
    }

    // Dejar de seguir usuario
    @PostMapping("/{id}/dejar-seguir/{seguidoId}")
    public ResponseEntity<Usuario> dejarDeSeguirUsuario(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        Usuario actualizado = usuarioService.dejarDeSeguirUsuario(id, seguidoId);
        return ResponseEntity.ok(actualizado);
    }

    // Consultar si un usuario sigue a otro
    @GetMapping("/{id}/sigue-a/{seguidoId}")
    public ResponseEntity<Boolean> sigueA(
            @PathVariable String id,
            @PathVariable String seguidoId) {
        boolean sigue = usuarioService.sigueA(id, seguidoId);
        return ResponseEntity.ok(sigue);
    }

    // Endpoint para actualizar solo las configuraciones del usuario
    @PatchMapping("/{id}/settings")
    public ResponseEntity<Usuario> actualizarSettings(
            @PathVariable String id,
            @RequestBody Usuario.Settings settings) {
        Usuario actualizado = usuarioService.actualizarSettings(id, settings);
        return ResponseEntity.ok(actualizado);
    }

    /*
      Ejemplo de payload para PATCH /api/usuarios/{id}/settings
      {
        "temaOscuro": true
        // "notificaciones": true,      // ejemplo: activar notificaciones
        // "idioma": "es",              // ejemplo: preferencia de idioma
        // "mostrarEmail": false        // ejemplo: preferencia de privacidad
      }
    */

}
