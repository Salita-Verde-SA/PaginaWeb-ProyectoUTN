package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Lugar;
import com.salitaverde.backend.backend.service.LugarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lugares")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LugarController {
    
    private final LugarService lugarService;
    
    @GetMapping
    public ResponseEntity<List<Lugar>> obtenerTodos() {
        return ResponseEntity.ok(lugarService.obtenerTodos());
    }
    
    @GetMapping("/verificados")
    public ResponseEntity<List<Lugar>> obtenerVerificados() {
        return ResponseEntity.ok(lugarService.obtenerVerificados());
    }
    
    @GetMapping("/administrador/{administradorId}")
    public ResponseEntity<List<Lugar>> obtenerPorAdministrador(@PathVariable String administradorId) {
        return ResponseEntity.ok(lugarService.obtenerPorAdministrador(administradorId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Lugar> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(lugarService.obtenerPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<Lugar> crear(@RequestBody Lugar lugar) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lugarService.crear(lugar));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Lugar> actualizar(@PathVariable String id, @RequestBody Lugar lugar) {
        return ResponseEntity.ok(lugarService.actualizar(id, lugar));
    }
    
    @PatchMapping("/{id}/verificar")
    public ResponseEntity<Lugar> verificar(@PathVariable String id) {
        return ResponseEntity.ok(lugarService.verificar(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        lugarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/comprobante-domicilio")
    public ResponseEntity<?> subirComprobanteDomicilio(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            lugarService.subirComprobanteDomicilio(id, archivo);
            return ResponseEntity.ok(Map.of("mensaje", "Comprobante subido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
