package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Lugar;
import com.salitaverde.backend.backend.repository.LugarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LugarService {
    
    private final LugarRepository lugarRepository;
    private final VerificacionService verificacionService;
    
    public List<Lugar> obtenerTodos() {
        return lugarRepository.findAll();
    }
    
    public List<Lugar> obtenerVerificados() {
        return lugarRepository.findByVerificadoTrue();
    }
    
    public List<Lugar> obtenerPorAdministrador(String administradorId) {
        return lugarRepository.findByAdministradorId(administradorId);
    }
    
    public Lugar obtenerPorId(String id) {
        return lugarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
    }
    
    @Transactional
    public Lugar crear(Lugar lugar) {
        lugar.setVerificado(false);
        lugar.setActivo(true);
        return lugarRepository.save(lugar);
    }
    
    @Transactional
    public Lugar actualizar(String id, Lugar lugar) {
        Lugar existente = obtenerPorId(id);
        existente.setNombre(lugar.getNombre());
        existente.setDireccion(lugar.getDireccion());
        existente.setLocalidad(lugar.getLocalidad());
        existente.setDescripcion(lugar.getDescripcion());
        existente.setTelefono(lugar.getTelefono());
        existente.setEmail(lugar.getEmail());
        existente.setSitioWeb(lugar.getSitioWeb());
        return lugarRepository.save(existente);
    }
    
    @Transactional
    public Lugar verificar(String id) {
        Lugar lugar = obtenerPorId(id);
        lugar.setVerificado(true);
        return lugarRepository.save(lugar);
    }
    
    @Transactional
    public void eliminar(String id) {
        Lugar lugar = obtenerPorId(id);
        if (lugar.getEsPrincipal()) {
            throw new RuntimeException("No se puede eliminar el lugar principal");
        }
        lugarRepository.deleteById(id);
    }
    
    @Transactional
    public Lugar subirComprobanteDomicilio(String id, MultipartFile archivo) {
        Lugar lugar = obtenerPorId(id);
        
        String extension = archivo.getContentType().contains("pdf") ? ".pdf" : ".jpg";
        String nombreArchivo = id + "_comprobante_domicilio" + extension;
        
        if (lugar.getComprobanteDomicilio() != null) {
            try {
                verificacionService.eliminarDocumento(lugar.getComprobanteDomicilio());
            } catch (Exception e) {
                log.warn("No se pudo eliminar comprobante anterior: {}", e.getMessage());
            }
        }
        
        verificacionService.subirDocumentoConNombre(archivo, nombreArchivo);
        lugar.setComprobanteDomicilio(nombreArchivo);
        return lugarRepository.save(lugar);
    }
}
