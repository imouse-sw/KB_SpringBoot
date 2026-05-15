package com.example.kidzbraindb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private Integer usuarioId;
    private String nombre;
    private String correo;
    private String password;
    private Integer edadHijo;
    private Instant fechaRegistro;
    private String fotoUrl;
    private String token;
}
