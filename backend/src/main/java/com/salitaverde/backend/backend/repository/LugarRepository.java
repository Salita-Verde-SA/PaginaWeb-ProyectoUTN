package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Lugar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LugarRepository extends MongoRepository<Lugar, String> {
    List<Lugar> findByAdministradorId(String administradorId);
    List<Lugar> findByVerificadoTrue();
    List<Lugar> findByActivoTrue();
    List<Lugar> findByLocalidad(String localidad);
    Optional<Lugar> findByAdministradorIdAndEsPrincipalTrue(String administradorId);
}
