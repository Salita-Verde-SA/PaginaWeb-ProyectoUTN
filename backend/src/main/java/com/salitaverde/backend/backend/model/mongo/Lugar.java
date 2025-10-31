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
import java.util.ArrayList;
import java.util.List;

@Document(collection = "lugares")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lugar {
    
    @Id
    private String id;
    
    // NUEVO: ID del administrador responsable
    @Field("administrador_id")
    private String administradorId;
    
    @Field("nombre")
    private String nombre;
    
    @Field("direccion")
    private String direccion;
    
    @Field("localidad")
    private String localidad;
    
    // Documento de verificación del lugar
    @Field("comprobante_domicilio")
    private String comprobanteDomicilio;
    
    @Field("logo")
    private String logo;
    
    @Field("descripcion")
    private String descripcion;
    
    @Field("telefono")
    private String telefono;
    
    @Field("email")
    private String email;
    
    @Field("sitio_web")
    private String sitioWeb;
    
    @Field("verificado")
    private Boolean verificado = false;
    
    @Field("activo")
    private Boolean activo = true;
    
    @Field("es_principal")
    private Boolean esPrincipal = false; // El primer lugar del admin
    
    // Publicaciones del lugar
    @Field("publicaciones")
    private List<String> publicaciones = new ArrayList<>();
    
    // Seguidores del lugar
    @Field("seguidores")
    private List<String> seguidores = new ArrayList<>();
    
    @CreatedDate
    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @LastModifiedDate
    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Estadisticas {
        private Integer totalPublicaciones = 0;
        private Integer totalSeguidores = 0;
        private Integer entradasVendidas = 0;
    }
    
    @Field("estadisticas")
    private Estadisticas estadisticas = new Estadisticas();
}
