package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AccesoDto {
    private Integer accesoId;
    private Integer usuarioId;
    private String accionRealizada;
    private Instant fechaAcceso;
}
