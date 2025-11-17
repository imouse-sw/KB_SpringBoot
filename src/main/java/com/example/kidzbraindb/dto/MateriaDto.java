package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MateriaDto {
    private Integer materiaId;
    private String nombre;
}
