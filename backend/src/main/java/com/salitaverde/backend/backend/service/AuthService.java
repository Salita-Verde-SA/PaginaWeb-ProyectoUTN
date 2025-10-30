package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.config.JwtConfig;
import com.salitaverde.backend.backend.dto.AuthResponse;
import com.salitaverde.backend.backend.dto.LoginRequest;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("La cuenta está desactivada");
        }
        
        // Actualizar última sesión
        usuario.setUltimaSesion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        // Generar token
        String token = jwtConfig.generateToken(
            usuario.getId(), 
            usuario.getUsername(), 
            usuario.getTokenVersion()
        );
        
        // No devolvemos la contraseña
        usuario.setPassword(null);
        
        return new AuthResponse(token, usuario, "Login exitoso");
    }

    @Transactional
    public AuthResponse register(Usuario usuario) {
        // Validaciones
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        // Validar que la localidad no sea null y sea válida
        if (usuario.getLocalidad() == null) {
            throw new RuntimeException("La localidad es requerida");
        }
        
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        usuario.setTokenVersion(0);
        usuario.setUltimaSesion(LocalDateTime.now());
        
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        
        // Generar token
        String token = jwtConfig.generateToken(
            nuevoUsuario.getId(), 
            nuevoUsuario.getUsername(), 
            nuevoUsuario.getTokenVersion()
        );
        
        nuevoUsuario.setPassword(null);
        
        return new AuthResponse(token, nuevoUsuario, "Registro exitoso");
    }

    public Usuario validateToken(String token) {
        try {
            String username = jwtConfig.extractUsername(token);
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
            
            if (usuarioOpt.isEmpty()) {
                throw new RuntimeException("Token inválido");
            }
            
            Usuario usuario = usuarioOpt.get();
            
            if (!jwtConfig.validateToken(token, usuario.getUsername(), usuario.getTokenVersion())) {
                throw new RuntimeException("Token inválido o expirado");
            }
            
            usuario.setPassword(null);
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Token inválido: " + e.getMessage());
        }
    }

    @Transactional
    public void logout(String userId) {
        Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Incrementar versión del token para invalidar todos los tokens anteriores
        usuario.setTokenVersion(usuario.getTokenVersion() + 1);
        usuarioRepository.save(usuario);
    }

    // Nuevo método para regenerar token después de actualizar username
    public String regenerarToken(String userId) {
        Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return jwtConfig.generateToken(
            usuario.getId(), 
            usuario.getUsername(), 
            usuario.getTokenVersion()
        );
    }
}
