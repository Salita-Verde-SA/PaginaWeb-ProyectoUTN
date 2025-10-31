package com.salitaverde.backend.backend.service;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class VerificacionService {

    private final MinioClient minioClient;

    @Value("${minio.verificacion.bucket-name}")
    private String bucketName;

    public VerificacionService(
            @Value("${minio.verificacion.url}") String url,
            @Value("${minio.verificacion.access-key}") String accessKey,
            @Value("${minio.verificacion.secret-key}") String secretKey
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public void subirDocumentoConNombre(MultipartFile archivo, String nombreArchivo) {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nombreArchivo)
                            .stream(archivo.getInputStream(), archivo.getSize(), -1)
                            .contentType(archivo.getContentType())
                            .build()
            );

            log.info("Documento de verificación subido: {}", nombreArchivo);

        } catch (Exception e) {
            log.error("Error al subir documento de verificación: {}", e.getMessage());
            throw new RuntimeException("Error al subir documento de verificación: " + e.getMessage(), e);
        }
    }

    public void eliminarDocumento(String nombreArchivo) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nombreArchivo)
                            .build()
            );
            log.info("Documento de verificación eliminado: {}", nombreArchivo);
        } catch (Exception e) {
            log.error("Error al eliminar documento: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar documento: " + e.getMessage(), e);
        }
    }
}
            log.info("Documento de verificación eliminado: {}", nombreArchivo);
        } catch (Exception e) {
            log.error("Error al eliminar documento: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar documento: " + e.getMessage(), e);
        }
    }
}
