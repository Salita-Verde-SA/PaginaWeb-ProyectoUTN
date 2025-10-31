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
    private String id; // Se generará automáticamente
    
    @Field("nombre_responsable")
    private String nombreResponsable;
    
    @Field("apellido_responsable")
    private String apellidoResponsable;
    
    @Field("dni_responsable")
    private String dniResponsable;
    
    @Field("fecha_nacimiento_responsable")
    private String fechaNacimientoResponsable;
    
    @Field("dni_frente")
    private String dniFrente; // Nombre del archivo en MinIO verificación
    
    @Field("dni_dorso")
    private String dniDorso; // Nombre del archivo en MinIO verificación
    
    @Field("email")
    private String email;
    
    @Field("celular")
    private String celular;
    
    @Field("sitio_web")
    private String sitioWeb;
    
    @Field("nombre_organizacion")
    private String nombreOrganizacion; // antes nombreFantasia
    
    @Field("razon_social")
    private String razonSocial; // Se puede derivar del nombre organización
    
    @Field("cuit")
    private String cuit;
    
    @Field("rubro")
    private String rubro;
    
    @Field("constancia_afip_img")
    private String constanciaAfipImg; // Nombre del archivo en MinIO verificación
    
    @Field("direccion_lugar")
    private String direccionLugar; // Dirección del establecimiento principal
    
    @Field("comprobante_lugar")
    private String comprobanteLugar; // Comprobante de domicilio en MinIO verificación
    
    @Field("localidad")
    private String localidad;
    
    @Field("username")
    private String username;
    
    @Field("password")
    private String password;
    
    @Field("logo_boliche")
    private String logoBoliche; // URL del logo (MinIO público)
    
    @Field("verificado")
    private Boolean verificado = false;
    
    @Field("activo")
    private Boolean activo = false; // Inactivo hasta aprobación
    
    @Field("publicaciones")
    private List<String> publicaciones = new ArrayList<>();
    
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
