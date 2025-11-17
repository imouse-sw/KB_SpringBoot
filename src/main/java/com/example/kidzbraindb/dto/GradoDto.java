package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class GradoDto {
    private Integer gradoId;
    private String nombre;
    private String rangoEdad;
}
