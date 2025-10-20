package com.salitaverde.backend.backend.service;


import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionService {
    
    private final PublicacionRepository publicacionRepository;
    
    public List<Publicacion> obtenerTodas() {
        return publicacionRepository.findByVisibleTrue();
    }
    
    public Publicacion obtenerPorId(String id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
    }
    
    public List<Publicacion> obtenerPorUsuario(Long usuarioId) {
        return publicacionRepository.findByUsuarioIdAndVisibleTrue(usuarioId);
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
        existente.setFechaActualizacion(LocalDateTime.now());
        return publicacionRepository.save(existente);
    }
    
    public void eliminar(String id) {
        Publicacion publicacion = obtenerPorId(id);
        publicacion.setVisible(false);
        publicacionRepository.save(publicacion);
    }
}