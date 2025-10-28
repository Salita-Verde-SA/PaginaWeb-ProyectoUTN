package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.dto.AuthResponse;
import com.salitaverde.backend.backend.dto.LoginRequest;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);
            
            // Crear cookie HTTP-only con el token
            Cookie cookie = new Cookie("authToken", authResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Cambiar a true en producción con HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 días
            response.addCookie(cookie);
            
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody Usuario usuario,
            HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.register(usuario);
            
            // Crear cookie HTTP-only con el token
            Cookie cookie = new Cookie("authToken", authResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Cambiar a true en producción con HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 días
            response.addCookie(cookie);
            
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valido", false, "mensaje", "No hay sesión activa"));
        }
        
        try {
            Usuario usuario = authService.validateToken(token);
            return ResponseEntity.ok(Map.of("valido", true, "usuario", usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valido", false, "mensaje", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "authToken", required = false) String token,
            HttpServletResponse response) {
        try {
            if (token != null) {
                String username = authService.validateToken(token).getUsername();
                Usuario usuario = authService.validateToken(token);
                authService.logout(usuario.getId());
            }
            
            // Eliminar cookie
            Cookie cookie = new Cookie("authToken", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            
            return ResponseEntity.ok(Map.of("mensaje", "Logout exitoso"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
        }
    }
}
