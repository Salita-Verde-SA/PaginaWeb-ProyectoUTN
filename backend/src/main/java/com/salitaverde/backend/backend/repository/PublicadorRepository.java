package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Publicador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicadorRepository extends MongoRepository<Publicador, String> {
    Optional<Publicador> findByCuit(String cuit);
    Optional<Publicador> findByUsername(String username);
    Optional<Publicador> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByCuit(String cuit);
    List<Publicador> findByVerificadoTrue();
    List<Publicador> findByActivoTrue();
    List<Publicador> findByLocalidad(String localidad);
}