package com.salitaverde.backend.backend.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioInitializer implements CommandLineRunner {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public void run(String... args) {
        int maxReintentos = 10;
        int reintentoActual = 0;
        
        while (reintentoActual < maxReintentos) {
            try {
                String bucketName = minioConfig.getBucketName();
                System.out.println("===========================================");
                System.out.println("Inicializando MinIO... (Intento " + (reintentoActual + 1) + "/" + maxReintentos + ")");
                System.out.println("URL: " + minioConfig.getUrl());
                System.out.println("Bucket: " + bucketName);
                
                boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                
                if (!exists) {
                    System.out.println("El bucket '" + bucketName + "' no existe. Creándolo...");
                    minioClient.makeBucket(
                        MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
                    );
                    System.out.println("✓ Bucket '" + bucketName + "' creado exitosamente");
                } else {
                    System.out.println("✓ El bucket '" + bucketName + "' ya existe");
                }
                
                System.out.println("MinIO inicializado correctamente");
                System.out.println("===========================================");
                return; // Éxito, salir
                
            } catch (Exception e) {
                reintentoActual++;
                if (reintentoActual >= maxReintentos) {
                    System.err.println("===========================================");
                    System.err.println("✗ ERROR: No se pudo inicializar MinIO después de " + maxReintentos + " intentos");
                    System.err.println("Mensaje: " + e.getMessage());
                    e.printStackTrace();
                    System.err.println("===========================================");
                    System.err.println("ADVERTENCIA: El sistema continuará sin MinIO. Las funciones de imágenes no estarán disponibles.");
                    return; // No lanzar excepción para que el backend continúe
                } else {
                    System.err.println("Error al conectar con MinIO (intento " + reintentoActual + "/" + maxReintentos + "): " + e.getMessage());
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
