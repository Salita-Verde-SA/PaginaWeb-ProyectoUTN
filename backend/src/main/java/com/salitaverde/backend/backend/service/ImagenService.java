package com.salitaverde.backend.backend.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.salitaverde.backend.backend.config.MinioConfig;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagenService {
    
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    
    public void crearBucketSiNoExiste() {
        try {
            String bucketName = minioConfig.getBucketName();
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear bucket: " + e.getMessage());
        }
    }
    
    public String subirImagen(MultipartFile archivo) {
        try {
            crearBucketSiNoExiste();
            String bucketName = minioConfig.getBucketName();
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .stream(archivo.getInputStream(), archivo.getSize(), -1)
                    .contentType(archivo.getContentType())
                    .build()
            );
            return nombreArchivo;
        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen: " + e.getMessage());
        }
    }
    
    public InputStream obtenerImagen(String nombreArchivo) {
        try {
            String bucketName = minioConfig.getBucketName();
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener imagen: " + e.getMessage());
        }
    }
    
    public void eliminarImagen(String nombreArchivo) {
        try {
            String bucketName = minioConfig.getBucketName();
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage());
        }
    }
}
