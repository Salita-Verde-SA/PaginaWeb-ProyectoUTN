package com.salitaverde.backend.backend.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio.verificacion")
@Data
public class MinioVerificacionConfig {

    @Value("${MINIO_VERIFICACION_ENDPOINT:http://minio-verification:9000}")
    private String endpoint;

    @Value("${MINIO_VERIFICACION_ACCESS_KEY:minioadmin}")
    private String accessKey;

    @Value("${MINIO_VERIFICACION_SECRET_KEY:minioadmin}")
    private String secretKey;

    @Value("${MINIO_VERIFICACION_BUCKET_NAME:verificacion-documentos}")
    private String bucketName;

    @Bean(name = "minioVerificacionClient")
    public MinioClient minioVerificacionClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String getBucketName() {
        return bucketName;
    }
    
    public String getUrl() {
        return endpoint;
    }
}
