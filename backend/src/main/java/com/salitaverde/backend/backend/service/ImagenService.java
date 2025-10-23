package com.salitaverde.backend.backend.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagenService {
    
    private final MinioClient minioClient;
    private static final String BUCKET_NAME = "imagenes";
    
    public void crearBucketSiNoExiste() {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(BUCKET_NAME).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(BUCKET_NAME).build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear bucket: " + e.getMessage());
        }
    }
    
    public String subirImagen(MultipartFile archivo) {
        try {
            crearBucketSiNoExiste();
            
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
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
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener imagen: " + e.getMessage());
        }
    }
    
    public void eliminarImagen(String nombreArchivo) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(nombreArchivo)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage());
        }
    }
}
