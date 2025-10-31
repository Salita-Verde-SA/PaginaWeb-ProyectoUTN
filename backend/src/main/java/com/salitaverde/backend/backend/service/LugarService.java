package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Lugar;
import com.salitaverde.backend.backend.repository.LugarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LugarService {
    
    private final LugarRepository lugarRepository;
    private final ImagenService imagenService;
    private final DocumentoVerificacionService documentoVerificacionService;
    
    public List<Lugar> obtenerTodos() {
        return lugarRepository.findAll();
    }
    
    public Lugar obtenerPorId(String id) {
        return lugarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
    }
    
    public List<Lugar> obtenerPorAdministrador(String administradorId) {
        return lugarRepository.findByAdministradorId(administradorId);
    }
    
    public List<Lugar> obtenerVerificados() {
        return lugarRepository.findByVerificadoTrue();
    }
    
    @Transactional
    public Lugar crear(Lugar lugar) {
        return lugarRepository.save(lugar);
    }
    
    @Transactional
    public Lugar verificar(String id) {
        Lugar lugar = obtenerPorId(id);
        lugar.setVerificado(true);
        return lugarRepository.save(lugar);
    }
    
    @Transactional
    public Lugar subirComprobante(String id, MultipartFile archivo) {
        Lugar lugar = obtenerPorId(id);
        
        String extension = "";
        String originalFilename = archivo.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String nombreArchivo = id + "_comprobante" + extension;
        
        if (lugar.getComprobanteDomicilio() != null && !lugar.getComprobanteDomicilio().trim().isEmpty()) {
            try {
                documentoVerificacionService.eliminarDocumento(lugar.getComprobanteDomicilio());
            } catch (Exception e) {
                System.out.println("No se pudo eliminar comprobante anterior: " + e.getMessage());
            }
        }
        
        documentoVerificacionService.subirDocumento(archivo, nombreArchivo);
        lugar.setComprobanteDomicilio(nombreArchivo);
        
        return lugarRepository.save(lugar);
    }
    
    @Transactional
    public Lugar actualizarLogo(String id, MultipartFile archivo) {
        Lugar lugar = obtenerPorId(id);
        
        String nombreArchivo = id + "_logo.jpg";
        
        if (lugar.getLogo() != null && !lugar.getLogo().trim().isEmpty()) {
            try {
                imagenService.eliminarImagen(lugar.getLogo());
            } catch (Exception e) {
                System.out.println("No se pudo eliminar logo anterior: " + e.getMessage());
            }
        }
        
        imagenService.subirImagenConNombre(archivo, nombreArchivo);
        lugar.setLogo(nombreArchivo);
        
        return lugarRepository.save(lugar);
    }
    
    public void guardar(Lugar lugar) {
        lugarRepository.save(lugar);
    }
}
