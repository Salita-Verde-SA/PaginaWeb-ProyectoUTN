package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ImagenService imagenService;
    private final PasswordEncoder passwordEncoder;

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

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
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
        // No actualizar fotoPerfil aquí, usar actualizarFotoPerfil
        if (usuario.getUsername() != null && !usuario.getUsername().equals(existente.getUsername())) {
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                throw new RuntimeException("El username ya está en uso");
            }
            existente.setUsername(usuario.getUsername());
        }
        existente.setLocalidad(usuario.getLocalidad());
        existente.setSettings(usuario.getSettings());
        return usuarioRepository.save(existente);
    }

    @Transactional
    public Usuario actualizarNombreUsuario(String id, String nuevoUsername) {
        if (nuevoUsername == null || nuevoUsername.trim().isEmpty()) {
            throw new RuntimeException("El nombre de usuario no puede estar vacío");
        }
        if (nuevoUsername.length() > 11) {
            throw new RuntimeException("El nombre de usuario no puede tener más de 11 caracteres");
        }
        
        Usuario existente = obtenerPorId(id);
        
        // Verificar que el nuevo username no esté en uso por otro usuario
        if (!existente.getUsername().equals(nuevoUsername) && usuarioRepository.existsByUsername(nuevoUsername)) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        existente.setUsername(nuevoUsername);
        return usuarioRepository.save(existente);
    }

    @Transactional
    public Usuario actualizarFotoPerfil(String id, MultipartFile archivo) {
        Usuario existente = obtenerPorId(id);
        
        // Obtener extensión del archivo
        String originalFilename = archivo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // Nombre del archivo: {id}_profile.extension
        String nombreArchivo = id + "_profile" + extension;
        
        // Eliminar foto anterior si existe
        if (existente.getFotoPerfil() != null && !existente.getFotoPerfil().trim().isEmpty()) {
            try {
                imagenService.eliminarImagen(existente.getFotoPerfil());
            } catch (Exception e) {
                // Ignorar error si la imagen no existe
                System.out.println("No se pudo eliminar la imagen anterior: " + e.getMessage());
            }
        }
        
        // Subir nueva foto
        imagenService.subirImagenConNombre(archivo, nombreArchivo);
        
        existente.setFotoPerfil(nombreArchivo);
        return usuarioRepository.save(existente);
    }

    public void cambiarContrasena(String id, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = obtenerPorId(id);
        
        if (!passwordEncoder.matches(contrasenaActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        usuario.setPassword(passwordEncoder.encode(contrasenaNueva));
        usuario.setTokenVersion(usuario.getTokenVersion() + 1);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(String id) {
        // Deshabilitar en lugar de eliminar permanentemente
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarSettings(String id, Usuario.Settings settings) {
        Usuario existente = obtenerPorId(id);
        Usuario.Settings settingsActuales = existente.getSettings();
        
        if (settingsActuales == null) {
            settingsActuales = new Usuario.Settings();
        }
        
        // Actualizar solo los campos que vienen en el objeto settings
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

    @Transactional
    public void deshabilitarCuenta(String id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void habilitarCuenta(String id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public Usuario eliminarSeguidor(String id, String seguidorId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Seguidor no encontrado"));

        // Remover de la lista de seguidores del usuario
        usuario.getSeguidores().remove(seguidorId);
        // Remover de la lista de seguidos del seguidor
        seguidor.getSeguidos().remove(id);

        usuarioRepository.save(seguidor);
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

    // NUEVOS MÉTODOS: Obtener datos completos de seguidores/seguidos
    public List<Usuario> obtenerSeguidoresDetalles(String id) {
        Usuario usuario = obtenerPorId(id);
        return usuario.getSeguidores().stream()
                .map(seguidorId -> {
                    try {
                        return obtenerPorId(seguidorId);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(u -> u != null)
                .toList();
    }

    public List<Usuario> obtenerSeguidosDetalles(String id) {
        Usuario usuario = obtenerPorId(id);
        return usuario.getSeguidos().stream()
                .map(seguidoId -> {
                    try {
                        return obtenerPorId(seguidoId);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(u -> u != null)
                .toList();
    }
}