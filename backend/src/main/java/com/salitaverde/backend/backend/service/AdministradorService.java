package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Administrador;
import com.salitaverde.backend.backend.repository.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {
    
    private final AdministradorRepository administradorRepository;
    private final DocumentoVerificacionService documentoVerificacionService;
    private final PasswordEncoder passwordEncoder;
    
    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }
    
    public Administrador obtenerPorId(String id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
    }
    
    public Administrador obtenerPorUsername(String username) {
        return administradorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
    }
    
    public List<Administrador> obtenerVerificados() {
        return administradorRepository.findByVerificadoTrue();
    }
    
    @Transactional
    public Administrador crear(Administrador administrador) {
        // Validaciones
        if (administradorRepository.existsByEmail(administrador.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (administradorRepository.existsByUsername(administrador.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (administradorRepository.existsByDni(administrador.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (administradorRepository.existsByCuit(administrador.getCuit())) {
            throw new RuntimeException("El CUIT ya está registrado");
        }
        
        // Encriptar contraseña
        administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
        
        // Establecer valores por defecto
        administrador.setVerificado(false);
        administrador.setActivo(false);
        
        return administradorRepository.save(administrador);
    }
    
    @Transactional
    public Administrador verificar(String id) {
        Administrador admin = obtenerPorId(id);
        admin.setVerificado(true);
        return administradorRepository.save(admin);
    }
    
    @Transactional
    public Administrador subirDocumento(String id, String tipoDocumento, MultipartFile archivo) {
        Administrador admin = obtenerPorId(id);
        
        String extension = "";
        String originalFilename = archivo.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String nombreArchivo = id + "_" + tipoDocumento + extension;
        
        // Eliminar documento anterior si existe
        String documentoAnterior = switch (tipoDocumento) {
            case "dni-frente" -> admin.getDniFrente();
            case "dni-dorso" -> admin.getDniDorso();
            case "constancia-afip-img" -> admin.getConstanciaAfipImg();
            default -> null;
        };
        
        if (documentoAnterior != null && !documentoAnterior.trim().isEmpty()) {
            try {
                documentoVerificacionService.eliminarDocumento(documentoAnterior);
            } catch (Exception e) {
                System.out.println("No se pudo eliminar el documento anterior: " + e.getMessage());
            }
        }
        
        // Subir nuevo documento
        documentoVerificacionService.subirDocumento(archivo, nombreArchivo);
        
        // Actualizar campo correspondiente
        switch (tipoDocumento) {
            case "dni-frente" -> admin.setDniFrente(nombreArchivo);
            case "dni-dorso" -> admin.setDniDorso(nombreArchivo);
            case "constancia-afip-img" -> admin.setConstanciaAfipImg(nombreArchivo);
        }
        
        return administradorRepository.save(admin);
    }
    
    public void guardar(Administrador administrador) {
        administradorRepository.save(administrador);
    }
}
