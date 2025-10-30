package com.salitaverde.backend.backend.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {
    
    @Id
    private String id;
    
    @Field("publicador_id")
    private String publicadorId;
    
    @Field("imagen_portada")
    private String imagenPortada = ""; // Nueva imagen de portada
    
    @Field("titulo")
    private String titulo;
    
    @Field("contenido")
    private String contenido;
    
    @Field("imagenes")
    private List<String> imagenes;
    
    @Field("etiquetas")
    private List<String> etiquetas;
    
    @Field("fecha_evento")
    private LocalDateTime fechaEvento;
    
    @Field("hora_evento")
    private String horaEvento; // Formato "HH:mm" (ej: "21:00")
    
    @Field("likes")
    private Integer likes = 0;
    
    @Field("comentarios")
    private List<Comentario> comentarios;
    
    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    @Field("visible")
    private Boolean visible = true;
    
    @Field("entradas_totales")
    private Integer entradasTotales = 0;
    
    @Field("entradas_disponibles")
    private Integer entradasDisponibles = 0;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comentario {
        private String id;
        private String usuarioId;
        private String username;
        private String texto;
        private LocalDateTime fecha = LocalDateTime.now();
    }
}