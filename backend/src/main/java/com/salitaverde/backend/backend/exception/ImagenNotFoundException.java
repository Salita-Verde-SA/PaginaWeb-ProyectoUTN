package com.salitaverde.backend.backend.exception;

public class ImagenNotFoundException extends RuntimeException {
    public ImagenNotFoundException(String mensaje) {
        super(mensaje);
    }
}
