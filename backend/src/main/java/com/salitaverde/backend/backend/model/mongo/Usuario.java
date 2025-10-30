package com.salitaverde.backend.backend.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    private String id;

    @Field("dni")
    private String dni;

    @Field("nombre")
    private String nombre;

    @Field("apellido")
    private String apellido;

    @Field("email")
    private String email;

    @Field("username")
    @Size(max = 11)
    private String username;

    @Field("password")
    private String password;

    @Field("foto_perfil")
    private String fotoPerfil = "";

    @Field("activo")
    private Boolean activo = true;

    @Field("admin")
    private Boolean admin = false; // Campo para permisos de moderación

    @Field("localidad")
    private Localidad localidad;

    @Field("seguidores")
    private List<String> seguidores = new ArrayList<>();

    @Field("seguidos")
    private List<String> seguidos = new ArrayList<>();

    @Field("settings")
    private Settings settings = new Settings(); // por defecto

    @Field("carrito")
    private Carrito carrito = new Carrito();

    @Field("cantidad_eventos_asistidos")
    private Integer cantidadEventosAsistidos = 0;

    @Field("ultima_sesion")
    private LocalDateTime ultimaSesion;

    @Field("token_version")
    private Integer tokenVersion = 0; // Para invalidar tokens al cambiar contraseña

    @CreatedDate
    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Settings {
        private Boolean temaOscuro = true; // Cambiar a true por defecto (modo oscuro)

        // Ejemplos de otros parámetros útiles (descomentar/usar si se añaden al frontend/backend):
        // private Boolean notificaciones = true;   // recibir notificaciones push/email
        // private String idioma = "es";            // "es", "en", etc.
        // private Boolean mostrarEmail = false;    // privacidad: mostrar email en perfil
        // private Integer itemsPorPagina = 20;     // preferencia paginación
        //
        // Si necesitas campos adicionales, agrégalos aquí y actualiza el frontend
        // para enviar sólo los campos que quieres cambiar en el PATCH /{id}/settings
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Carrito {
        private List<ItemCarrito> items = new ArrayList<>();

        public boolean agregarPublicacion(Publicacion publicacion, int cantidad) {
            if (publicacion.getEntradasDisponibles() < cantidad) {
                return false; // No hay suficientes entradas disponibles
            }
            // Buscar si ya existe el item en el carrito
            ItemCarrito existente = items.stream()
                .filter(i -> i.getPublicacionId().equals(publicacion.getId()))
                .findFirst()
                .orElse(null);
            if (existente != null) {
                int nuevaCantidad = existente.getCantidad() + cantidad;
                if (publicacion.getEntradasDisponibles() < nuevaCantidad) {
                    return false;
                }
                existente.setCantidad(nuevaCantidad);
            } else {
                items.add(new ItemCarrito(publicacion.getId(), cantidad));
            }
            // Descontar las entradas disponibles
            publicacion.setEntradasDisponibles(publicacion.getEntradasDisponibles() - cantidad);
            return true;
        }

        public void quitarPublicacion(String publicacionId) {
            items.removeIf(i -> i.getPublicacionId().equals(publicacionId));
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ItemCarrito {
            private String publicacionId;
            private int cantidad;
        }
    }
}
