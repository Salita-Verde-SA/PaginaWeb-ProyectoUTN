package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends MongoRepository<Administrador, String> {
    Optional<Administrador> findByUsername(String username);
    Optional<Administrador> findByEmail(String email);
    Optional<Administrador> findByDni(String dni);
    Optional<Administrador> findByCuit(String cuit);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByDni(String dni);
    boolean existsByCuit(String cuit);
    List<Administrador> findByVerificadoTrue();
    List<Administrador> findByActivoTrue();
    List<Administrador> findByLocalidad(String localidad);
}
