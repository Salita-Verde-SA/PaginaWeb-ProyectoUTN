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

@Document(collection = "administradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
    
    @Id
    private String id;
    
    // Datos personales del responsable
    @Field("dni")
    private String dni;
    
    @Field("nombre")
    private String nombre;
    
    @Field("apellido")
    private String apellido;
    
    @Field("fecha_nacimiento")
    private String fechaNacimiento;
    
    // Organización
    @Field("nombre_organizacion")
    private String nombreOrganizacion;
    
    @Field("cuit")
    private String cuit;
    
    @Field("rubro")
    private String rubro;
    
    // Contacto
    @Field("email")
    private String email;
    
    @Field("celular")
    private String celular;
    
    @Field("sitio_web")
    private String sitioWeb;
    
    // Credenciales
    @Field("username")
    @Size(max = 11)
    private String username;
    
    @Field("password")
    private String password;
    
    // Documentos de verificación
    @Field("dni_frente")
    private String dniFrente;
    
    @Field("dni_dorso")
    private String dniDorso;
    
    @Field("constancia_afip_img")
    private String constanciaAfipImg;
    
    // Estado
    @Field("verificado")
    private Boolean verificado = false;
    
    @Field("activo")
    private Boolean activo = true;
    
    @Field("localidad")
    private Localidad localidad;
    
    // Lista de IDs de lugares que administra
    @Field("lugares_administrados")
    private List<String> lugaresAdministrados = new ArrayList<>();
    
    @Field("token_version")
    private Integer tokenVersion = 0;
    
    @CreatedDate
    @Field("fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @LastModifiedDate
    @Field("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
