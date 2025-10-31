package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Publicador;
import com.salitaverde.backend.backend.service.PublicadorService;
import com.salitaverde.backend.backend.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publicadores")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PublicadorController {
    
    private final PublicadorService publicadorService;
    private final ImagenService imagenService;
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Publicador publicador) {
        try {
            // La encriptación se hace en PublicadorService.crear()
            Publicador nuevo = publicadorService.crear(publicador);
            
            Map<String, Object> response = new HashMap<>();
            response.put("exito", true);
            response.put("mensaje", "Solicitud de registro enviada. Pendiente de verificación.");
            response.put("publicador", Map.of("id", nuevo.getId()));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/{id}/dni-frente")
    public ResponseEntity<?> subirDniFrente(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            publicadorService.subirDniFrente(id, archivo);
            return ResponseEntity.ok(Map.of("mensaje", "DNI frente subido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/dni-dorso")
    public ResponseEntity<?> subirDniDorso(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            publicadorService.subirDniDorso(id, archivo);
            return ResponseEntity.ok(Map.of("mensaje", "DNI dorso subido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/constancia-afip")
    public ResponseEntity<?> subirConstanciaAfip(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            publicadorService.subirConstanciaAfip(id, archivo);
            return ResponseEntity.ok(Map.of("mensaje", "Constancia AFIP subida correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/comprobante-lugar")
    public ResponseEntity<?> subirComprobanteLugar(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        try {
            publicadorService.subirComprobanteLugar(id, archivo);
            return ResponseEntity.ok(Map.of("mensaje", "Comprobante de lugar subido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
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
    
    @PostMapping("/{id}/logo")
    public ResponseEntity<Publicador> actualizarLogo(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(publicadorService.actualizarLogo(id, archivo));
    }
    
    @GetMapping("/{id}/logo")
    public ResponseEntity<byte[]> obtenerLogo(@PathVariable String id) {
        Publicador publicador = publicadorService.obtenerPorId(id);
        
        if (publicador.getLogoBoliche() == null || publicador.getLogoBoliche().trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            InputStream imagen = imagenService.obtenerImagen(publicador.getLogoBoliche());
            byte[] bytes = imagen.readAllBytes();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            System.out.println("Error al obtener logo: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}