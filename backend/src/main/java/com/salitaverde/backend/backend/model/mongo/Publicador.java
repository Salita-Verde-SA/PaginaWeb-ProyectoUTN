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

@Document(collection = "publicadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicador {
    
    @Id
    private String id; // CUIT del boliche
    
    @Field("nombre_responsable")
    private String nombreResponsable;
    
    @Field("apellido_responsable")
    private String apellidoResponsable;
    
    @Field("email")
    private String email;
    
    @Field("celular")
    private String celular;
    
    @Field("nombre_fantasia")
    private String nombreFantasia;
    
    @Field("razon_social")
    private String razonSocial;
    
    @Field("cuit")
    private String cuit;
    
    @Field("direccion")
    private String direccion;
    
    @Field("localidad")
    private String localidad;
    
    @Field("tipo_documento")
    private String tipoDocumento; // 'habilitacion', 'afip', 'servicio', 'otro'
    
    @Field("documento_verificacion")
    private String documentoVerificacion; // URL del documento
    
    @Field("username")
    private String username;
    
    @Field("password")
    private String password;
    
    @Field("logo_boliche")
    private String logoBoliche; // URL del logo
    
    @Field("verificado")
    private Boolean verificado = false;
    
    @Field("activo")
    private Boolean activo = true;
    
    @Field("publicaciones")
    private List<String> publicaciones = new ArrayList<>(); // IDs de publicaciones
    
    @Field("seguidores")
    private List<String> seguidores = new ArrayList<>(); // IDs de usuarios que siguen al boliche
    
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
