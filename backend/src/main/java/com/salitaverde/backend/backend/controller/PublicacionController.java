package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.service.PublicacionService;
import com.salitaverde.backend.backend.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {
    
    private final PublicacionService publicacionService;
    private final ImagenService imagenService;
    
    @GetMapping
    public ResponseEntity<List<Publicacion>> obtenerTodas() {
        return ResponseEntity.ok(publicacionService.obtenerTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(publicacionService.obtenerPorId(id));
    }
    
    @GetMapping("/publicador/{publicadorId}")
    public ResponseEntity<List<Publicacion>> obtenerPorPublicador(@PathVariable String publicadorId) {
        return ResponseEntity.ok(publicacionService.obtenerPorPublicador(publicadorId));
    }
    
    @PostMapping
    public ResponseEntity<Publicacion> crear(@RequestBody Publicacion publicacion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicacionService.crear(publicacion));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizar(
            @PathVariable String id,
            @RequestBody Publicacion publicacion) {
        return ResponseEntity.ok(publicacionService.actualizar(id, publicacion));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/portada")
    public ResponseEntity<Publicacion> actualizarPortada(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(publicacionService.actualizarPortada(id, archivo));
    }
    
    @GetMapping("/{id}/portada")
    public ResponseEntity<byte[]> obtenerPortada(@PathVariable String id) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);
        
        if (publicacion.getImagenPortada() == null || publicacion.getImagenPortada().trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            InputStream imagen = imagenService.obtenerImagen(publicacion.getImagenPortada());
            byte[] bytes = imagen.readAllBytes();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            System.out.println("Error al obtener portada: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}

