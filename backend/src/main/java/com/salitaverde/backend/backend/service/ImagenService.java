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
            System.out.println("Verificando bucket: " + bucketName);
            
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            
            if (!exists) {
                System.out.println("Bucket no existe. Creando: " + bucketName);
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
                System.out.println("Bucket creado exitosamente: " + bucketName);
            } else {
                System.out.println("Bucket ya existe: " + bucketName);
            }
        } catch (Exception e) {
            System.err.println("Error al crear/verificar bucket: " + e.getMessage());
            e.printStackTrace();
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
    
    public boolean existeImagen(String nombreArchivo) {
        try {
            String bucketName = minioConfig.getBucketName();
            minioClient.statObject(
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
    
    public String subirImagenConNombre(MultipartFile archivo, String nombrePersonalizado) {
        try {
            System.out.println("Iniciando subida de imagen: " + nombrePersonalizado);
            crearBucketSiNoExiste();
            String bucketName = minioConfig.getBucketName();
            
            System.out.println("Subiendo archivo a bucket: " + bucketName);
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombrePersonalizado)
                    .stream(archivo.getInputStream(), archivo.getSize(), -1)
                    .contentType(archivo.getContentType())
                    .build()
            );
            System.out.println("Imagen subida exitosamente: " + nombrePersonalizado);
            return nombrePersonalizado;
        } catch (Exception e) {
            System.err.println("Error al subir imagen: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al subir imagen: " + e.getMessage());
        }
    }
    
    public String renombrarImagen(String nombreAntiguo, String nombreNuevo) {
        if (!existeImagen(nombreAntiguo)) {
            throw new RuntimeException("La imagen '" + nombreAntiguo + "' no existe");
        }
        
        if (existeImagen(nombreNuevo)) {
            throw new RuntimeException("Ya existe una imagen con el nombre: " + nombreNuevo);
        }
        
        try {
            String bucketName = minioConfig.getBucketName();
            // Copiar el objeto con el nuevo nombre
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreNuevo)
                    .source(
                        CopySource.builder()
                            .bucket(bucketName)
                            .object(nombreAntiguo)
                            .build()
                    )
                    .build()
            );
            
            // Eliminar el objeto antiguo
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(nombreAntiguo)
                    .build()
            );
            
            return nombreNuevo;
        } catch (Exception e) {
            throw new RuntimeException("Error al renombrar imagen: " + e.getMessage());
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
