package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Publicador;
import com.salitaverde.backend.backend.repository.PublicadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicadorService {
    
    private final PublicadorRepository publicadorRepository;
    private final ImagenService imagenService;
    private final VerificacionService verificacionService;
    private final PasswordEncoder passwordEncoder; // Añadir este campo
    
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
        
        // Encriptar contraseña AQUÍ
        publicador.setPassword(passwordEncoder.encode(publicador.getPassword()));
        
        // Establecer valores por defecto
        publicador.setVerificado(false);
        publicador.setActivo(false);
        if (publicador.getRazonSocial() == null || publicador.getRazonSocial().isEmpty()) {
            publicador.setRazonSocial(publicador.getNombreOrganizacion());
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
    
    @Transactional
    public Publicador subirDniFrente(String id, MultipartFile archivo) {
        Publicador publicador = obtenerPorId(id);
        String nombreArchivo = id + "_dni_frente.jpg";
        
        // Eliminar anterior si existe
        if (publicador.getDniFrente() != null) {
            try {
                verificacionService.eliminarDocumento(publicador.getDniFrente());
            } catch (Exception e) {
                log.warn("No se pudo eliminar DNI frente anterior: {}", e.getMessage());
            }
        }
        
        verificacionService.subirDocumentoConNombre(archivo, nombreArchivo);
        publicador.setDniFrente(nombreArchivo);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador subirDniDorso(String id, MultipartFile archivo) {
        Publicador publicador = obtenerPorId(id);
        String nombreArchivo = id + "_dni_dorso.jpg";
        
        if (publicador.getDniDorso() != null) {
            try {
                verificacionService.eliminarDocumento(publicador.getDniDorso());
            } catch (Exception e) {
                log.warn("No se pudo eliminar DNI dorso anterior: {}", e.getMessage());
            }
        }
        
        verificacionService.subirDocumentoConNombre(archivo, nombreArchivo);
        publicador.setDniDorso(nombreArchivo);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador subirConstanciaAfip(String id, MultipartFile archivo) {
        Publicador publicador = obtenerPorId(id);
        String nombreArchivo = id + "_constancia_afip.jpg";
        
        if (publicador.getConstanciaAfipImg() != null) {
            try {
                verificacionService.eliminarDocumento(publicador.getConstanciaAfipImg());
            } catch (Exception e) {
                log.warn("No se pudo eliminar constancia anterior: {}", e.getMessage());
            }
        }
        
        verificacionService.subirDocumentoConNombre(archivo, nombreArchivo);
        publicador.setConstanciaAfipImg(nombreArchivo);
        return publicadorRepository.save(publicador);
    }
    
    @Transactional
    public Publicador subirComprobanteLugar(String id, MultipartFile archivo) {
        Publicador publicador = obtenerPorId(id);
        
        // Determinar extensión según tipo de archivo
        String extension = archivo.getContentType().contains("pdf") ? ".pdf" : ".jpg";
        String nombreArchivo = id + "_comprobante_lugar" + extension;
        
        if (publicador.getComprobanteLugar() != null) {
            try {
                verificacionService.eliminarDocumento(publicador.getComprobanteLugar());
            } catch (Exception e) {
                log.warn("No se pudo eliminar comprobante anterior: {}", e.getMessage());
            }
        }
        
        verificacionService.subirDocumentoConNombre(archivo, nombreArchivo);
        publicador.setComprobanteLugar(nombreArchivo);
        return publicadorRepository.save(publicador);
    }
}