package com.salitaverde.backend.backend.controller;

import com.salitaverde.backend.backend.model.mongo.Pedido;
import com.salitaverde.backend.backend.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Pedido> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(pedidoService.obtenerPorCodigoConfirmacion(codigo));
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(pedidoService.obtenerPorUsuario(usuarioId));
    }
    
    @GetMapping("/publicador/{publicadorId}")
    public ResponseEntity<List<Pedido>> obtenerPorPublicador(@PathVariable String publicadorId) {
        return ResponseEntity.ok(pedidoService.obtenerPorPublicador(publicadorId));
    }
    
    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<Pedido>> obtenerPorPublicacion(@PathVariable String publicacionId) {
        return ResponseEntity.ok(pedidoService.obtenerPorPublicacion(publicacionId));
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorEstado(@PathVariable Pedido.EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.obtenerPorEstado(estado));
    }
    
    @GetMapping("/usuario/{usuarioId}/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuarioYEstado(
            @PathVariable String usuarioId,
            @PathVariable Pedido.EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.obtenerPorUsuarioYEstado(usuarioId, estado));
    }
    
    @GetMapping("/publicador/{publicadorId}/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorPublicadorYEstado(
            @PathVariable String publicadorId,
            @PathVariable Pedido.EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.obtenerPorPublicadorYEstado(publicadorId, estado));
    }
    
    @GetMapping("/fechas")
    public ResponseEntity<List<Pedido>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pedidoService.obtenerPorRangoFechas(inicio, fin));
    }
    
    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crear(pedido));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(
            @PathVariable String id,
            @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.actualizar(id, pedido));
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable String id,
            @RequestBody Pedido.EstadoPedido nuevoEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, nuevoEstado));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
