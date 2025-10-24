package com.salitaverde.backend.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.salitaverde.backend.backend.service.ImagenService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {
    
    private final ImagenService imagenService;
    
    @PostMapping("/subir")
    public ResponseEntity<?> subirImagen(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "nombre", required = false) String nombrePersonalizado) {
        try {
            if (archivo.isEmpty()) {
                throw new IllegalArgumentException("El archivo está vacío");
            }
            
            String nombreArchivo;
            if (nombrePersonalizado != null && !nombrePersonalizado.trim().isEmpty()) {
                nombreArchivo = imagenService.subirImagenConNombre(archivo, nombrePersonalizado);
            } else {
                nombreArchivo = imagenService.subirImagen(archivo);
            }
            
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("nombreArchivo", nombreArchivo);
            respuesta.put("url", "/api/imagenes/" + nombreArchivo);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
    
    @PutMapping("/{nombreAntiguo}/renombrar")
    public ResponseEntity<?> renombrarImagen(
            @PathVariable String nombreAntiguo,
            @RequestBody Map<String, String> body) {
        try {
            String nombreNuevo = body.get("nombreNuevo");
            if (nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre nuevo es requerido");
            }
            
            String nombreFinal = imagenService.renombrarImagen(nombreAntiguo, nombreNuevo);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("nombreArchivo", nombreFinal);
            respuesta.put("url", "/api/imagenes/" + nombreFinal);
            respuesta.put("mensaje", "Imagen renombrada exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
    
    @GetMapping("/{nombreArchivo}")
    public ResponseEntity<InputStreamResource> obtenerImagen(
            @PathVariable String nombreArchivo) {
        InputStream stream = imagenService.obtenerImagen(nombreArchivo);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "inline; filename=\"" + nombreArchivo + "\"")
                .body(new InputStreamResource(stream));
    }
    
    @DeleteMapping("/{nombreArchivo}")
    public ResponseEntity<?> eliminarImagen(@PathVariable String nombreArchivo) {
        try {
            imagenService.eliminarImagen(nombreArchivo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
