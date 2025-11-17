package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UsuarioDto {
    private Integer usuarioId;
    private String nombre;
    private String correo;
    private String password;
    private Integer edadHijo;
    private Instant fechaRegistro;
}
