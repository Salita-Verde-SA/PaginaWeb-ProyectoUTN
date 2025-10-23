package com.salitaverde.backend.backend.service;

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
        return usuarioRepository.findAll();
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
        // Mezclar/actualizar solo settings: puedes elegir merge o reemplazo completo
        existente.setSettings(settings);
        return usuarioRepository.save(existente);
    }
}
