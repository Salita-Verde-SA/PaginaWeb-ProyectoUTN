package com.salitaverde.backend.backend.repository;

import com.salitaverde.backend.backend.model.mongo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    List<Usuario> findByAdminTrue();
    List<Usuario> findByGerenteTrue();
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
}