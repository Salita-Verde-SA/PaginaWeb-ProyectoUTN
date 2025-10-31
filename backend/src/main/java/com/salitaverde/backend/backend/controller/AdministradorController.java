package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Administrador;
import com.salitaverde.backend.backend.service.AdministradorService;
import com.salitaverde.backend.backend.service.DocumentoVerificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {
    
    private final AdministradorService administradorService;
    private final DocumentoVerificacionService documentoVerificacionService;
    
    @GetMapping
    public List<Administrador> obtenerTodos() {
        return administradorService.obtenerTodos();
    }
    
    @GetMapping("/{id}")
    public Administrador obtenerPorId(@PathVariable String id) {
        return administradorService.obtenerPorId(id);
    }
    
    @GetMapping("/username/{username}")
    public Administrador obtenerPorUsername(@PathVariable String username) {
        return administradorService.obtenerPorUsername(username);
    }
    
    @GetMapping("/verificados")
    public List<Administrador> obtenerVerificados() {
        return administradorService.obtenerVerificados();
    }
    
    @PostMapping("/{id}/verificar")
    public Administrador verificar(@PathVariable String id) {
        return administradorService.verificar(id);
    }
    
    @PostMapping("/{id}/{tipoDocumento}")
    public ResponseEntity<?> subirDocumento(
            @PathVariable String id,
            @PathVariable String tipoDocumento,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            Administrador admin = administradorService.subirDocumento(id, tipoDocumento, archivo);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/documento/{tipoDocumento}")
    public ResponseEntity<byte[]> obtenerDocumento(
            @PathVariable String id,
            @PathVariable String tipoDocumento
    ) {
        try {
            Administrador admin = administradorService.obtenerPorId(id);
            
            String nombreDocumento = switch (tipoDocumento) {
                case "dni-frente" -> admin.getDniFrente();
                case "dni-dorso" -> admin.getDniDorso();
                case "constancia-afip-img" -> admin.getConstanciaAfipImg();
                default -> null;
            };
            
            if (nombreDocumento == null || nombreDocumento.trim().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            InputStream documento = documentoVerificacionService.obtenerDocumento(nombreDocumento);
            byte[] bytes = documento.readAllBytes();
            
            MediaType mediaType = nombreDocumento.toLowerCase().endsWith(".pdf") 
                ? MediaType.APPLICATION_PDF 
                : MediaType.IMAGE_JPEG;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            
            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            System.out.println("Error al obtener documento: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
