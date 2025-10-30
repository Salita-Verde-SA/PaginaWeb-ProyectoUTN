package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Publicacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends MongoRepository<Publicacion, String> {
    List<Publicacion> findByVisibleTrue();
    List<Publicacion> findByPublicadorIdAndVisibleTrue(String publicadorId);
}
