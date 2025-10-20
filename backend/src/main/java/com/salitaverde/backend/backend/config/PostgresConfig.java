package com.salitaverde.backend.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.proyecto.repository")
@EnableJpaAuditing
public class PostgresConfig {
    // Configuración adicional si es necesaria
}