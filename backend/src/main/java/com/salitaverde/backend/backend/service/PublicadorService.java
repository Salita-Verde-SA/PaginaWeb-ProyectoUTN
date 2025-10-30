package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Publicador;
import com.salitaverde.backend.backend.repository.PublicadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicadorService {
    
    private final PublicadorRepository publicadorRepository;
    private final ImagenService imagenService;
    
    public List<Publicador> obtenerTodos() {
        return publicadorRepository.findAll();
    }
    
    public List<Publicador> obtenerVerificados() {
        return publicadorRepository.findByVerificadoTrue();
    }
    
    public List<Publicador> obtenerActivos() {
        return publicadorRepository.findByActivoTrue();
    }
    
    public Publicador obtenerPorId(String id) {
        return publicadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicador no encontrado"));
    }
    
    public Publicador obtenerPorCuit(String cuit) {
        return publicadorRepository.findByCuit(cuit)
                .orElseThrow(() -> new RuntimeException("Publicador no encontrado"));
    }
    
    public Publicador obtenerPorUsername(String username) {
        return publicadorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Publicador no encontrado"));
    }
    
    public List<Publicador> obtenerPorLocalidad(String localidad) {
        return publicadorRepository.findByLocalidad(localidad);
    }
    
    @Transactional
    public Publicador crear(Publicador publicador) {
        if (publicadorRepository.existsByEmail(publicador.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (publicadorRepository.existsByUsername(publicador.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (publicadorRepository.existsByCuit(publicador.getCuit())) {
            throw new RuntimeException("El CUIT ya está registrado");
        }
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador actualizar(String id, Publicador publicador) {
        Publicador existente = obtenerPorId(id);
        existente.setNombreResponsable(publicador.getNombreResponsable());
        existente.setApellidoResponsable(publicador.getApellidoResponsable());
        existente.setEmail(publicador.getEmail());
        existente.setCelular(publicador.getCelular());
        existente.setNombreFantasia(publicador.getNombreFantasia());
        existente.setRazonSocial(publicador.getRazonSocial());
        existente.setDireccion(publicador.getDireccion());
        existente.setLocalidad(publicador.getLocalidad());
        existente.setLogoBoliche(publicador.getLogoBoliche());
        return publicadorRepository.save(existente);
    }
    
    @Transactional
    public Publicador verificar(String id) {
        Publicador publicador = obtenerPorId(id);
        publicador.setVerificado(true);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador desactivar(String id) {
        Publicador publicador = obtenerPorId(id);
        publicador.setActivo(false);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador activar(String id) {
        Publicador publicador = obtenerPorId(id);
        publicador.setActivo(true);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public void eliminar(String id) {
        publicadorRepository.deleteById(id);
    }
    
    @Transactional
    public Publicador agregarPublicacion(String id, String publicacionId) {
        Publicador publicador = obtenerPorId(id);
        if (!publicador.getPublicaciones().contains(publicacionId)) {
            publicador.getPublicaciones().add(publicacionId);
            publicador.getEstadisticas().setTotalPublicaciones(
                publicador.getPublicaciones().size()
            );
        }
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador quitarPublicacion(String id, String publicacionId) {
        Publicador publicador = obtenerPorId(id);
        publicador.getPublicaciones().remove(publicacionId);
        publicador.getEstadisticas().setTotalPublicaciones(
            publicador.getPublicaciones().size()
        );
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador agregarSeguidor(String id, String usuarioId) {
        Publicador publicador = obtenerPorId(id);
        if (!publicador.getSeguidores().contains(usuarioId)) {
            publicador.getSeguidores().add(usuarioId);
            publicador.getEstadisticas().setTotalSeguidores(
                publicador.getSeguidores().size()
            );
        }
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador quitarSeguidor(String id, String usuarioId) {
        Publicador publicador = obtenerPorId(id);
        publicador.getSeguidores().remove(usuarioId);
        publicador.getEstadisticas().setTotalSeguidores(
            publicador.getSeguidores().size()
        );
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador actualizarLogo(String id, MultipartFile archivo) {
        Publicador existente = obtenerPorId(id);
        
        // Nombre del archivo: {id}_logo.jpg (siempre JPG)
        String nombreArchivo = id + "_logo.jpg";
        
        // Eliminar logo anterior si existe
        if (existente.getLogoBoliche() != null && !existente.getLogoBoliche().trim().isEmpty()) {
            try {
                imagenService.eliminarImagen(existente.getLogoBoliche());
            } catch (Exception e) {
                System.out.println("No se pudo eliminar el logo anterior: " + e.getMessage());
            }
        }
        
        // Subir nuevo logo
        imagenService.subirImagenConNombre(archivo, nombreArchivo);
        
        existente.setLogoBoliche(nombreArchivo);
        return publicadorRepository.save(existente);
    }
}