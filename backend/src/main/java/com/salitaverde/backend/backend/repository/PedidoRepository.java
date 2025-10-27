package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
    
    // Buscar pedidos por usuario
    List<Pedido> findByUsuarioId(String usuarioId);
    
    // Buscar pedidos por publicador
    List<Pedido> findByPublicadorId(String publicadorId);
    
    // Buscar pedidos por publicación
    List<Pedido> findByPublicacionId(String publicacionId);
    
    // Buscar por código de confirmación
    Optional<Pedido> findByCodigoConfirmacion(String codigoConfirmacion);
    
    // Buscar pedidos por estado
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
    
    // Buscar pedidos de un usuario por estado
    List<Pedido> findByUsuarioIdAndEstado(String usuarioId, Pedido.EstadoPedido estado);
    
    // Buscar pedidos en un rango de fechas
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar pedidos de un publicador por estado
    List<Pedido> findByPublicadorIdAndEstado(String publicadorId, Pedido.EstadoPedido estado);
}
