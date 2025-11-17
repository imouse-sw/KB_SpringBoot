package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuegoDto {
    private Integer juegoId;
    private Integer leccionId;
    private String nombreLeccion;
    private String nombreJuego;
    private String tipo;
    private String descripcion;
}
