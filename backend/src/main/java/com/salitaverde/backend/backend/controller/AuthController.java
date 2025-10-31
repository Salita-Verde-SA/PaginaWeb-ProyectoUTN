package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.dto.AuthResponse;
import com.salitaverde.backend.backend.dto.LoginRequest;
import com.salitaverde.backend.backend.model.mongo.Administrador;
import com.salitaverde.backend.backend.model.mongo.Localidad;
import com.salitaverde.backend.backend.model.mongo.Lugar;
import com.salitaverde.backend.backend.model.mongo.Usuario;
import com.salitaverde.backend.backend.service.AdministradorService;
import com.salitaverde.backend.backend.service.AuthService;
import com.salitaverde.backend.backend.service.LugarService;
import com.salitaverde.backend.backend.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;
    private final LugarService lugarService;
    private final PasswordEncoder passwordEncoder;

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

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(
            @RequestBody Map<String, Object> datos,
            HttpServletResponse response) {
        try {
            // 1. Crear Usuario
            Usuario usuario = new Usuario();
            usuario.setDni((String) datos.get("dni"));
            usuario.setNombre((String) datos.get("nombre"));
            usuario.setApellido((String) datos.get("apellido"));
            usuario.setEmail((String) datos.get("email"));
            usuario.setUsername((String) datos.get("username"));
            usuario.setPassword(passwordEncoder.encode((String) datos.get("password")));
            usuario.setLocalidad(Localidad.valueOf((String) datos.get("localidad")));
            usuario.setAdmin(true);
            
            Usuario usuarioCreado = usuarioService.crear(usuario);
            
            // 2. Crear Administrador
            Administrador admin = new Administrador();
            admin.setId(usuarioCreado.getId());
            admin.setNombre((String) datos.get("nombre"));
            admin.setApellido((String) datos.get("apellido"));
            admin.setDni((String) datos.get("dni"));
            admin.setFechaNacimiento((String) datos.get("fechaNacimiento"));
            admin.setNombreOrganizacion((String) datos.get("nombreOrganizacion"));
            admin.setCuit((String) datos.get("cuit"));
            admin.setRubro((String) datos.get("rubro"));
            admin.setSitioWeb((String) datos.get("sitioWeb"));
            admin.setEmail((String) datos.get("email"));
            admin.setCelular((String) datos.get("celular"));
            admin.setUsername((String) datos.get("username"));
            admin.setPassword(passwordEncoder.encode((String) datos.get("password")));
            admin.setLocalidad(Localidad.valueOf((String) datos.get("localidad")));
            
            Administrador adminCreado = administradorService.crear(admin);
            
            // 3. Crear Lugar Principal
            Lugar lugarPrincipal = new Lugar();
            lugarPrincipal.setAdministradorId(adminCreado.getId());
            lugarPrincipal.setNombre((String) datos.get("nombreOrganizacion"));
            lugarPrincipal.setDireccion((String) datos.get("direccionLugar"));
            lugarPrincipal.setLocalidad((String) datos.get("localidad"));
            lugarPrincipal.setEsPrincipal(true);
            lugarPrincipal.setVerificado(false);
            
            Lugar lugarCreado = lugarService.crear(lugarPrincipal);
            
            // 4. Actualizar relaciones
            usuarioCreado.setAdministradorId(adminCreado.getId());
            usuarioService.guardar(usuarioCreado);
            
            adminCreado.getLugaresAdministrados().add(lugarCreado.getId());
            administradorService.guardar(adminCreado);
            
            // 5. Crear token JWT
            String token = authService.generateToken(usuarioCreado);
            
            // 6. Crear cookie de sesión
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60);
            response.addCookie(cookie);
            
            return ResponseEntity.ok(Map.of(
                "exito", true,
                "mensaje", "Registro exitoso",
                "usuario", Map.of(
                    "id", usuarioCreado.getId(),
                    "administradorId", adminCreado.getId(),
                    "lugarPrincipalId", lugarCreado.getId()
                )
            ));
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "exito", false,
                        "mensaje", e.getMessage()
                    ));
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

    // --- NUEVO: Devuelve el usuario autenticado (para frontend) ---
    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "No autenticado"));
        }
        try {
            Usuario usuario = authService.validateToken(token);
            boolean isAdmin = Boolean.TRUE.equals(usuario.getAdmin());
            String rol = isAdmin ? "ADMIN" : "USER";
            List<String> roles = isAdmin ? List.of("ADMIN") : List.of("USER");
            return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "username", usuario.getUsername(),
                "email", usuario.getEmail(),
                "admin", isAdmin,
                "rol", rol,
                "roles", roles
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}
