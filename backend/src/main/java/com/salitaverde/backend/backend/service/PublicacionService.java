package com.salitaverde.backend.backend.service;


import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionService {
    
    private final PublicacionRepository publicacionRepository;
    private final ImagenService imagenService;
    
    public List<Publicacion> obtenerTodas() {
        return publicacionRepository.findByVisibleTrue();
    }
    
    public Publicacion obtenerPorId(String id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
    }
    
    public List<Publicacion> obtenerPorPublicador(String publicadorId) {
        return publicacionRepository.findByPublicadorIdAndVisibleTrue(publicadorId);
    }
    
    public Publicacion crear(Publicacion publicacion) {
        publicacion.setFechaCreacion(LocalDateTime.now());
        publicacion.setFechaActualizacion(LocalDateTime.now());
        return publicacionRepository.save(publicacion);
    }
    
    public Publicacion actualizar(String id, Publicacion publicacion) {
        Publicacion existente = obtenerPorId(id);
        existente.setTitulo(publicacion.getTitulo());
        existente.setContenido(publicacion.getContenido());
        existente.setImagenes(publicacion.getImagenes());
        existente.setEtiquetas(publicacion.getEtiquetas());
        existente.setFechaEvento(publicacion.getFechaEvento());
        existente.setHoraEvento(publicacion.getHoraEvento());
        existente.setFechaActualizacion(LocalDateTime.now());
        return publicacionRepository.save(existente);
    }
    
    public void eliminar(String id) {
        Publicacion publicacion = obtenerPorId(id);
        publicacion.setVisible(false);
        publicacionRepository.save(publicacion);
    }
    
    @Transactional
    public Publicacion actualizarPortada(String id, MultipartFile archivo) {
        Publicacion existente = obtenerPorId(id);
        
        // Obtener extensión del archivo
        String originalFilename = archivo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // Nombre del archivo: {id}_portada.extension
        String nombreArchivo = id + "_portada.jpg"; // Siempre JPG
        
        // Eliminar portada anterior si existe
        if (existente.getImagenPortada() != null && !existente.getImagenPortada().trim().isEmpty()) {
            try {
                imagenService.eliminarImagen(existente.getImagenPortada());
            } catch (Exception e) {
                System.out.println("No se pudo eliminar la portada anterior: " + e.getMessage());
            }
        }
        
        // Subir nueva portada
        imagenService.subirImagenConNombre(archivo, nombreArchivo);
        
        existente.setImagenPortada(nombreArchivo);
        existente.setFechaActualizacion(LocalDateTime.now());
        return publicacionRepository.save(existente);
    }
}