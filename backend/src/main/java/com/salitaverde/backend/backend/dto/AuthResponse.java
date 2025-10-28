package com.salitaverde.backend.backend.dto;

import com.salitaverde.backend.backend.model.mongo.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Usuario usuario;
    private String mensaje;
}
