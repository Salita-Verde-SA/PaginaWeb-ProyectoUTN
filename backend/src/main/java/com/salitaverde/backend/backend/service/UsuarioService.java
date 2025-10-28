package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage());
        }
    }

    public Usuario obtenerPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(String id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setFotoPerfil(usuario.getFotoPerfil());
        existente.setUsername(usuario.getUsername());
        existente.setLocalidad(usuario.getLocalidad());
        existente.setSettings(usuario.getSettings());
        return usuarioRepository.save(existente);
    }

    @Transactional
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public Usuario actualizarSettings(String id, Usuario.Settings settings) {
        Usuario existente = obtenerPorId(id);
        
        // Actualizar solo los campos no nulos del settings recibido
        Usuario.Settings settingsActuales = existente.getSettings();
        if (settingsActuales == null) {
            settingsActuales = new Usuario.Settings();
        }
        
        // Actualizar solo si el valor viene en el request
        if (settings.getTemaOscuro() != null) {
            settingsActuales.setTemaOscuro(settings.getTemaOscuro());
        }
        
        // Si agregas más campos a Settings en el futuro, añade más condiciones aquí:
        // if (settings.getNotificaciones() != null) {
        //     settingsActuales.setNotificaciones(settings.getNotificaciones());
        // }
        // if (settings.getIdioma() != null) {
        //     settingsActuales.setIdioma(settings.getIdioma());
        // }
        
        existente.setSettings(settingsActuales);
        return usuarioRepository.save(existente);
    }

    public Usuario seguirUsuario(String id, String seguidoId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuario a seguir no encontrado"));

        if (!usuario.getSeguidos().contains(seguidoId)) {
            usuario.getSeguidos().add(seguidoId);
        }
        if (!seguido.getSeguidores().contains(id)) {
            seguido.getSeguidores().add(id);
        }

        usuarioRepository.save(seguido);
        return usuarioRepository.save(usuario);
    }

    public Usuario dejarDeSeguirUsuario(String id, String seguidoId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuario a dejar de seguir no encontrado"));

        usuario.getSeguidos().remove(seguidoId);
        seguido.getSeguidores().remove(id);

        usuarioRepository.save(seguido);
        return usuarioRepository.save(usuario);
    }

    public boolean sigueA(String id, String seguidoId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getSeguidos().contains(seguidoId);
    }

    // Agregar al carrito
    public boolean agregarAlCarrito(String usuarioId, Publicacion publicacion, int cantidad) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        boolean agregado = usuario.getCarrito().agregarPublicacion(publicacion, cantidad);
        if (agregado) {
            usuarioRepository.save(usuario);
        }
        return agregado;
    }

    // Incrementar contador de eventos asistidos
    @Transactional
    public Usuario registrarAsistenciaEvento(String usuarioId) {
        Usuario usuario = obtenerPorId(usuarioId);
        Integer cantidadActual = usuario.getCantidadEventosAsistidos();
        if (cantidadActual == null) {
            cantidadActual = 0;
        }
        usuario.setCantidadEventosAsistidos(cantidadActual + 1);
        return usuarioRepository.save(usuario);
    }

    // Obtener cantidad de eventos asistidos
    public Integer obtenerCantidadEventosAsistidos(String usuarioId) {
        Usuario usuario = obtenerPorId(usuarioId);
        Integer cantidad = usuario.getCantidadEventosAsistidos();
        return cantidad != null ? cantidad : 0;
    }
}
