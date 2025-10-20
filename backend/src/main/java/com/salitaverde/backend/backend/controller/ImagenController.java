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

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {
    
    private final ImagenService imagenService;
    
    @PostMapping("/subir")
    public ResponseEntity<Map<String, String>> subirImagen(
            @RequestParam("archivo") MultipartFile archivo) {
        String nombreArchivo = imagenService.subirImagen(archivo);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("nombreArchivo", nombreArchivo);
        respuesta.put("url", "/api/imagenes/" + nombreArchivo);
        return ResponseEntity.ok(respuesta);
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
    public ResponseEntity<Void> eliminarImagen(@PathVariable String nombreArchivo) {
        imagenService.eliminarImagen(nombreArchivo);
        return ResponseEntity.noContent().build();
    }
}
