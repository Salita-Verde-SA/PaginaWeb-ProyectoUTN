package com.salitaverde.backend.backend.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    private String dni;

    @Field("nombre")
    private String nombre;

    @Field("apellido")
    private String apellido;

    @Field("email")
    private String email;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("foto_perfil")
    private String fotoPerfil;

    @Field("activo")
    private Boolean activo = true;

    @Field("localidad")
    private Localidad localidad;

    @Field("settings")
    private Settings settings = new Settings(); // por defecto

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
        private Boolean temaOscuro = false;

        // Ejemplos de otros parámetros útiles (descomentar/usar si se añaden al frontend/backend):
        // private Boolean notificaciones = true;   // recibir notificaciones push/email
        // private String idioma = "es";            // "es", "en", etc.
        // private Boolean mostrarEmail = false;    // privacidad: mostrar email en perfil
        // private Integer itemsPorPagina = 20;     // preferencia paginación
        //
        // Si necesitas campos adicionales, agrégalos aquí y actualiza el frontend
        // para enviar sólo los campos que quieres cambiar en el PATCH /{id}/settings
    }
}
