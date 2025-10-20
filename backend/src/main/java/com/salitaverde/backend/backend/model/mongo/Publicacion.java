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
    
    @Field("usuario_id")
    private Long usuarioId;
    
    @Field("username")
    private String username;
    
    @Field("titulo")
    private String titulo;
    
    @Field("contenido")
    private String contenido;
    
    @Field("imagenes")
    private List<String> imagenes;
    
    @Field("etiquetas")
    private List<String> etiquetas;
    
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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comentario {
        private String id;
        private Long usuarioId;
        private String username;
        private String texto;
        private LocalDateTime fecha = LocalDateTime.now();
    }
}