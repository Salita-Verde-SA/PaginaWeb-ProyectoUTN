package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Publicador;
import com.salitaverde.backend.backend.service.PublicadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicadores")
@RequiredArgsConstructor
public class PublicadorController {
    
    private final PublicadorService publicadorService;
    
    @GetMapping
    public ResponseEntity<List<Publicador>> obtenerTodos() {
        return ResponseEntity.ok(publicadorService.obtenerTodos());
    }
    
    @GetMapping("/verificados")
    public ResponseEntity<List<Publicador>> obtenerVerificados() {
        return ResponseEntity.ok(publicadorService.obtenerVerificados());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Publicador>> obtenerActivos() {
        return ResponseEntity.ok(publicadorService.obtenerActivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Publicador> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(publicadorService.obtenerPorId(id));
    }
    
    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<Publicador> obtenerPorCuit(@PathVariable String cuit) {
        return ResponseEntity.ok(publicadorService.obtenerPorCuit(cuit));
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<Publicador> obtenerPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(publicadorService.obtenerPorUsername(username));
    }
    
    @GetMapping("/localidad/{localidad}")
    public ResponseEntity<List<Publicador>> obtenerPorLocalidad(@PathVariable String localidad) {
        return ResponseEntity.ok(publicadorService.obtenerPorLocalidad(localidad));
    }
    
    @PostMapping
    public ResponseEntity<Publicador> crear(@RequestBody Publicador publicador) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicadorService.crear(publicador));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Publicador> actualizar(
            @PathVariable String id,
            @RequestBody Publicador publicador) {
        return ResponseEntity.ok(publicadorService.actualizar(id, publicador));
    }
    
    @PatchMapping("/{id}/verificar")
    public ResponseEntity<Publicador> verificar(@PathVariable String id) {
        return ResponseEntity.ok(publicadorService.verificar(id));
    }
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Publicador> desactivar(@PathVariable String id) {
        return ResponseEntity.ok(publicadorService.desactivar(id));
    }
    
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Publicador> activar(@PathVariable String id) {
        return ResponseEntity.ok(publicadorService.activar(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        publicadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/publicaciones/{publicacionId}")
    public ResponseEntity<Publicador> agregarPublicacion(
            @PathVariable String id,
            @PathVariable String publicacionId) {
        return ResponseEntity.ok(publicadorService.agregarPublicacion(id, publicacionId));
    }
    
    @DeleteMapping("/{id}/publicaciones/{publicacionId}")
    public ResponseEntity<Publicador> quitarPublicacion(
            @PathVariable String id,
            @PathVariable String publicacionId) {
        return ResponseEntity.ok(publicadorService.quitarPublicacion(id, publicacionId));
    }
    
    @PostMapping("/{id}/seguidores/{usuarioId}")
    public ResponseEntity<Publicador> agregarSeguidor(
            @PathVariable String id,
            @PathVariable String usuarioId) {
        return ResponseEntity.ok(publicadorService.agregarSeguidor(id, usuarioId));
    }
    
    @DeleteMapping("/{id}/seguidores/{usuarioId}")
    public ResponseEntity<Publicador> quitarSeguidor(
            @PathVariable String id,
            @PathVariable String usuarioId) {
        return ResponseEntity.ok(publicadorService.quitarSeguidor(id, usuarioId));
    }
}