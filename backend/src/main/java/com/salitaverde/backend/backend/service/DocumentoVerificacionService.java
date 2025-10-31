package com.salitaverde.backend.backend.service;

import com.salitaverde.backend.backend.config.MinioVerificacionConfig;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class DocumentoVerificacionService {
    
    @Qualifier("minioVerificacionClient")
    private final MinioClient minioVerificacionClient;
    private final MinioVerificacionConfig minioVerificacionConfig;
    
    public void crearBucketSiNoExiste() {
        try {
            String bucketName = minioVerificacionConfig.getBucketName();
            System.out.println("Verificando bucket de verificación: " + bucketName);
            
            boolean exists = minioVerificacionClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            
            if (!exists) {
                System.out.println("Bucket de verificación no existe. Creando: " + bucketName);
                minioVerificacionClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
                System.out.println("Bucket de verificación creado: " + bucketName);
            } else {
                System.out.println("Bucket de verificación ya existe: " + bucketName);
            }
        } catch (Exception e) {
            System.err.println("Error al crear bucket de verificación: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear bucket de verificación: " + e.getMessage());
        }
    }
    
    public String subirDocumento(MultipartFile archivo, String nombreArchivo) {
        try {
            System.out.println("Subiendo documento de verificación: " + nombreArchivo);
            crearBucketSiNoExiste();
            String bucketName = minioVerificacionConfig.getBucketName();
            
            minioVerificacionClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .stream(archivo.getInputStream(), archivo.getSize(), -1)
                    .contentType(archivo.getContentType())
                    .build()
            );
            System.out.println("Documento subido: " + nombreArchivo);
            return nombreArchivo;
        } catch (Exception e) {
            System.err.println("Error al subir documento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al subir documento: " + e.getMessage());
        }
    }
    
    public InputStream obtenerDocumento(String nombreArchivo) {
        try {
            String bucketName = minioVerificacionConfig.getBucketName();
            return minioVerificacionClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener documento: " + e.getMessage());
        }
    }
    
    public boolean existeDocumento(String nombreArchivo) {
        try {
            String bucketName = minioVerificacionConfig.getBucketName();
            minioVerificacionClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void eliminarDocumento(String nombreArchivo) {
        try {
            String bucketName = minioVerificacionConfig.getBucketName();
            minioVerificacionClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar documento: " + e.getMessage());
        }
    }
}
