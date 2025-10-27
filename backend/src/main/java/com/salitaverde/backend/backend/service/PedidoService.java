package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.model.mongo.Pedido;
import com.salitaverde.backend.backend.model.mongo.Publicacion;
import com.salitaverde.backend.backend.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final PublicacionService publicacionService;
    
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }
    
    public Pedido obtenerPorId(String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    
    public Pedido obtenerPorCodigoConfirmacion(String codigo) {
        return pedidoRepository.findByCodigoConfirmacion(codigo)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ese código"));
    }
    
    public List<Pedido> obtenerPorUsuario(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
    
    public List<Pedido> obtenerPorPublicador(String publicadorId) {
        return pedidoRepository.findByPublicadorId(publicadorId);
    }
    
    public List<Pedido> obtenerPorPublicacion(String publicacionId) {
        return pedidoRepository.findByPublicacionId(publicacionId);
    }
    
    public List<Pedido> obtenerPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }
    
    public List<Pedido> obtenerPorUsuarioYEstado(String usuarioId, Pedido.EstadoPedido estado) {
        return pedidoRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }
    
    public List<Pedido> obtenerPorPublicadorYEstado(String publicadorId, Pedido.EstadoPedido estado) {
        return pedidoRepository.findByPublicadorIdAndEstado(publicadorId, estado);
    }
    
    public List<Pedido> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaPedidoBetween(inicio, fin);
    }
    
    @Transactional
    public Pedido crear(Pedido pedido) {
        // Validar que la publicación existe y tiene entradas disponibles
        Publicacion publicacion = publicacionService.obtenerPorId(pedido.getPublicacionId());
        
        if (publicacion.getEntradasDisponibles() < pedido.getCantidad()) {
            throw new RuntimeException("No hay suficientes entradas disponibles");
        }
        
        // Generar código de confirmación único
        pedido.setCodigoConfirmacion(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Calcular precio total
        if (pedido.getPrecioUnitario() == null) {
            pedido.setPrecioUnitario(100.0); // Precio por defecto o obtenerlo de la publicación
        }
        pedido.setPrecioTotal(pedido.getPrecioUnitario() * pedido.getCantidad());
        
        // Establecer fecha del pedido
        pedido.setFechaPedido(LocalDateTime.now());
        
        // Reducir entradas disponibles
        publicacion.setEntradasDisponibles(
            publicacion.getEntradasDisponibles() - pedido.getCantidad()
        );
        publicacionService.actualizar(publicacion.getId(), publicacion);
        
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public Pedido actualizarEstado(String id, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        Pedido.EstadoPedido estadoAnterior = pedido.getEstado();
        pedido.setEstado(nuevoEstado);
        
        // Si se cancela o reembolsa, devolver entradas
        if ((nuevoEstado == Pedido.EstadoPedido.CANCELADO || 
             nuevoEstado == Pedido.EstadoPedido.REEMBOLSADO) &&
            (estadoAnterior == Pedido.EstadoPedido.PENDIENTE || 
             estadoAnterior == Pedido.EstadoPedido.CONFIRMADO)) {
            
            Publicacion publicacion = publicacionService.obtenerPorId(pedido.getPublicacionId());
            publicacion.setEntradasDisponibles(
                publicacion.getEntradasDisponibles() + pedido.getCantidad()
            );
            publicacionService.actualizar(publicacion.getId(), publicacion);
        }
        
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public Pedido actualizar(String id, Pedido pedidoActualizado) {
        Pedido existente = obtenerPorId(id);
        
        // Solo permitir actualizar ciertos campos
        if (pedidoActualizado.getCorreoEnvio() != null) {
            existente.setCorreoEnvio(pedidoActualizado.getCorreoEnvio());
        }
        if (pedidoActualizado.getMensajeRegalo() != null) {
            existente.setMensajeRegalo(pedidoActualizado.getMensajeRegalo());
        }
        if (pedidoActualizado.getMetodoPago() != null) {
            existente.setMetodoPago(pedidoActualizado.getMetodoPago());
        }
        
        return pedidoRepository.save(existente);
    }
    
    @Transactional
    public void eliminar(String id) {
        Pedido pedido = obtenerPorId(id);
        
        // Solo permitir eliminar pedidos pendientes
        if (pedido.getEstado() == Pedido.EstadoPedido.PENDIENTE) {
            // Devolver entradas
            Publicacion publicacion = publicacionService.obtenerPorId(pedido.getPublicacionId());
            publicacion.setEntradasDisponibles(
                publicacion.getEntradasDisponibles() + pedido.getCantidad()
            );
            publicacionService.actualizar(publicacion.getId(), publicacion);
            
            pedidoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Solo se pueden eliminar pedidos pendientes. Use cancelar o reembolsar.");
        }
    }
}
