package com.salitaverde.backend.backend.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
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

    // NUEVO: referencia al lugar
    @Field("lugar_id")
    private String lugarId;

    // NUEVO: genero musical del evento
    @Field("genero")
    private String genero;

    @Field("imagen_portada")
    private String imagenPortada = ""; // URL en MinIO (bucket de eventos)

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
    private String horaEvento; // "HH:mm"

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

    // --- NUEVOS CAMPOS DE CREAR-EVENTO ---

    @Field("cobra_entrada")
    private Boolean cobraEntrada = false;

    @Field("tipos_entrada")
    private List<TipoEntrada> tiposEntrada; // puede ser null o lista vacía

    @Field("verificacion_edad")
    private Boolean verificacionEdad = false;

    @Field("edad_minima")
    private Integer edadMinima; // null si no aplica

    @Field("capacidad_maxima_habilitada")
    private Integer capacidadMaximaHabilitada; // requerido por frontend

    @Field("link_ubicacion")
    private String linkUbicacion; // opcional

    @Field("contacto_publico")
    private String contactoPublico; // requerido por frontend

    @Field("es_accesible")
    private Boolean esAccesible = false;

    @Field("acepta_mercado_pago")
    private Boolean aceptaMercadoPago = false;

    @Field("genera_qr_unico")
    private Boolean generaQrUnico = false;

    @Field("tiene_colaboradores")
    private Boolean tieneColaboradores = false;

    @Field("colaboradores_texto")
    private String colaboradoresTexto;

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

    // NUEVO: estructura para tipos de entrada
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TipoEntrada {
        private String nombre;
        private BigDecimal precio; // null si gratis
        private Integer cupo;      // null si sin cupo por categoría
    }
}