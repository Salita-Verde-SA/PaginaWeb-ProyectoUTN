package com.salitaverde.backend.backend.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    private String id;
    
    @Field("usuario_id")
    private String usuarioId; // ID del usuario que compra
    
    @Field("username_comprador")
    private String usernameComprador; // Username del comprador
    
    @Field("publicador_id")
    private String publicadorId; // ID del publicador/boliche que vende
    
    @Field("nombre_publicador")
    private String nombrePublicador; // Nombre del publicador para facilitar búsquedas
    
    @Field("publicacion_id")
    private String publicacionId; // ID de la publicación/evento
    
    @Field("titulo_evento")
    private String tituloEvento; // Título del evento
    
    @Field("cantidad")
    private Integer cantidad; // Cantidad de entradas compradas
    
    @Field("precio_unitario")
    private Double precioUnitario; // Precio por entrada al momento de la compra
    
    @Field("precio_total")
    private Double precioTotal; // Total pagado (cantidad * precioUnitario)
    
    @Field("estado")
    private EstadoPedido estado = EstadoPedido.PENDIENTE; // Estado del pedido
    
    @Field("metodo_pago")
    private String metodoPago; // "tarjeta", "efectivo", "transferencia", etc.
    
    @Field("correo_envio")
    private String correoEnvio; // Correo donde se envían las entradas
    
    @Field("es_regalo")
    private Boolean esRegalo = false; // Si es un regalo
    
    @Field("mensaje_regalo")
    private String mensajeRegalo; // Mensaje personalizado si es regalo
    
    @Field("codigo_confirmacion")
    private String codigoConfirmacion; // Código único de confirmación
    
    @CreatedDate
    @Field("fecha_pedido")
    private LocalDateTime fechaPedido;
    
    @Field("fecha_evento")
    private LocalDateTime fechaEvento; // Fecha del evento (copiada de la publicación)
    
    public enum EstadoPedido {
        PENDIENTE,      // Pedido creado, pago pendiente
        CONFIRMADO,     // Pago confirmado
        ENTREGADO,      // Entradas enviadas
        CANCELADO,      // Pedido cancelado
        REEMBOLSADO     // Pedido reembolsado
    }
}
