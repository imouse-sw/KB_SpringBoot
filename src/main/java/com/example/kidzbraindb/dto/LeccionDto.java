package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeccionDto {
    private Integer leccionId;
    private Integer materiaId;
    private String materiaNombre;
    private Integer gradoId;
    private String gradoNombre;
    private String titulo;
    private String descripcion;
    private Integer orden;
}
