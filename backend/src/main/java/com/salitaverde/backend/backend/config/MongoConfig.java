package com.salitaverde.backend.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort.Direction;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableMongoRepositories(basePackages = "com.salitaverde.backend.backend.repository")
@EnableMongoAuditing
@Slf4j
@RequiredArgsConstructor
public class MongoConfig {
    
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    
    private final MongoClient mongoClient;
    
    @Bean
    CommandLineRunner initDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                log.info("=======================================");
                log.info("  Inicializando base de datos MongoDB");
                log.info("=======================================");
                
                // Crear la base de datos explícitamente
                MongoDatabase database = mongoClient.getDatabase(databaseName);
                log.info("✓ Conectado a la base de datos '{}'", databaseName);
                
                // Verificar si la base de datos existe
                boolean dbExists = mongoClient.listDatabaseNames()
                    .into(new java.util.ArrayList<>())
                    .contains(databaseName);
                
                if (!dbExists) {
                    log.info("○ Base de datos '{}' no existe, creando...", databaseName);
                } else {
                    log.info("○ Base de datos '{}' ya existe", databaseName);
                }
                
                // Definir colecciones a crear
                String[] collections = {"usuarios", "publicaciones", "lugares", "pedidos", "administradores"};
                
                // Crear colecciones
                for (String collectionName : collections) {
                    if (!mongoTemplate.collectionExists(collectionName)) {
                        mongoTemplate.createCollection(collectionName);
                        log.info("✓ Colección '{}' creada", collectionName);
                    } else {
                        log.info("○ Colección '{}' ya existe", collectionName);
                    }
                }
                
                // Crear índices
                log.info("\nCreando índices...");
                
                // Índices para usuarios
                mongoTemplate.indexOps("usuarios").ensureIndex(
                    new Index().on("email", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("usuarios").ensureIndex(
                    new Index().on("username", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("usuarios").ensureIndex(
                    new Index().on("dni", Direction.ASC).unique()
                );
                log.info("✓ Índices de 'usuarios' creados");
                
                // Índices para publicadores
                mongoTemplate.indexOps("publicadores").ensureIndex(
                    new Index().on("email", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("publicadores").ensureIndex(
                    new Index().on("username", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("publicadores").ensureIndex(
                    new Index().on("cuit", Direction.ASC).unique()
                );
                log.info("✓ Índices de 'publicadores' creados");
                
                // Índices para publicaciones
                mongoTemplate.indexOps("publicaciones").ensureIndex(
                    new Index().on("usuarioId", Direction.ASC)
                );
                mongoTemplate.indexOps("publicaciones").ensureIndex(
                    new Index().on("fechaCreacion", Direction.DESC)
                );
                mongoTemplate.indexOps("publicaciones").ensureIndex(
                    new Index().on("visible", Direction.ASC)
                );
                log.info("✓ Índices de 'publicaciones' creados");
                
                // Índices para pedidos
                mongoTemplate.indexOps("pedidos").ensureIndex(
                    new Index().on("usuarioId", Direction.ASC)
                );
                mongoTemplate.indexOps("pedidos").ensureIndex(
                    new Index().on("publicadorId", Direction.ASC)
                );
                mongoTemplate.indexOps("pedidos").ensureIndex(
                    new Index().on("codigoConfirmacion", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("pedidos").ensureIndex(
                    new Index().on("estado", Direction.ASC)
                );
                log.info("✓ Índices de 'pedidos' creados");
                
                // Índices para administradores
                mongoTemplate.indexOps("administradores").ensureIndex(
                    new Index().on("email", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("administradores").ensureIndex(
                    new Index().on("username", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("administradores").ensureIndex(
                    new Index().on("dni", Direction.ASC).unique()
                );
                mongoTemplate.indexOps("administradores").ensureIndex(
                    new Index().on("cuit", Direction.ASC).unique()
                );
                log.info("✓ Índices de 'administradores' creados");
                
                // Índices para lugares
                mongoTemplate.indexOps("lugares").ensureIndex(
                    new Index().on("administrador_id", Direction.ASC)
                );
                mongoTemplate.indexOps("lugares").ensureIndex(
                    new Index().on("verificado", Direction.ASC)
                );
                log.info("✓ Índices de 'lugares' creados");
                
                // Forzar la creación física de la base de datos
                if (!dbExists) {
                    log.info("\nForzando creación física de la base de datos...");
                    mongoTemplate.getCollection("usuarios").insertOne(
                        new org.bson.Document("_temp", true)
                    );
                    mongoTemplate.getCollection("usuarios").deleteOne(
                        new org.bson.Document("_temp", true)
                    );
                    log.info("✓ Base de datos '{}' creada físicamente", databaseName);
                }
                
                // Verificación final
                boolean finalDbExists = mongoClient.listDatabaseNames()
                    .into(new java.util.ArrayList<>())
                    .contains(databaseName);
                
                log.info("\n======================================");
                if (finalDbExists) {
                    log.info("✓ Base de datos '{}' inicializada correctamente", databaseName);
                } else {
                    log.warn("⚠ Base de datos '{}' no está visible aún (se creará con el primer documento)", databaseName);
                }
                log.info("Colecciones disponibles: {}", String.join(", ", collections));
                log.info("Total de índices creados: {}", 
                    4 + 4 + 4 + 4 + 4); // usuarios + publicadores + publicaciones + pedidos + administradores
                log.info("======================================\n");
                
            } catch (Exception e) {
                log.error("\n======================================");
                log.error("✗ Error al inicializar la base de datos: {}", e.getMessage());
                log.error("======================================\n");
                throw new RuntimeException("No se pudo inicializar la base de datos MongoDB", e);
            }
        };
    }
}