package com.salitaverde.backend.backend.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // Se ejecuta después del inicializador de MinIO principal
@RequiredArgsConstructor
public class MinioVerificacionInitializer implements CommandLineRunner {

    @Qualifier("minioVerificacionClient")
    private final MinioClient minioVerificacionClient;
    private final MinioVerificacionConfig minioVerificacionConfig;

    @Override
    public void run(String... args) {
        int maxReintentos = 10;
        int reintentoActual = 0;
        
        while (reintentoActual < maxReintentos) {
            try {
                String bucketName = minioVerificacionConfig.getBucketName();
                System.out.println("===========================================");
                System.out.println("Inicializando MinIO Verificación... (Intento " + (reintentoActual + 1) + "/" + maxReintentos + ")");
                System.out.println("URL: " + minioVerificacionConfig.getUrl());
                System.out.println("Bucket: " + bucketName);
                
                boolean exists = minioVerificacionClient.bucketExists(
                    BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                
                if (!exists) {
                    System.out.println("El bucket '" + bucketName + "' no existe. Creándolo...");
                    minioVerificacionClient.makeBucket(
                        MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
                    );
                    System.out.println("✓ Bucket '" + bucketName + "' creado exitosamente");
                } else {
                    System.out.println("✓ El bucket '" + bucketName + "' ya existe");
                }
                
                System.out.println("MinIO Verificación inicializado correctamente");
                System.out.println("===========================================");
                return;
                
            } catch (Exception e) {
                reintentoActual++;
                if (reintentoActual >= maxReintentos) {
                    System.err.println("===========================================");
                    System.err.println("✗ ERROR: No se pudo inicializar MinIO Verificación después de " + maxReintentos + " intentos");
                    System.err.println("Mensaje: " + e.getMessage());
                    e.printStackTrace();
                    System.err.println("===========================================");
                    System.err.println("ADVERTENCIA: El sistema continuará sin MinIO Verificación.");
                    return;
                } else {
                    System.err.println("Error al conectar con MinIO Verificación (intento " + reintentoActual + "/" + maxReintentos + "): " + e.getMessage());
                    System.err.println("Reintentando en 3 segundos...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }
}
