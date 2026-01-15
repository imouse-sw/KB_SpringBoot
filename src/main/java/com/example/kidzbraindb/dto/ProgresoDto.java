package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProgresoDto {
    private Integer progresoId;
    private Integer usuarioId;

    // --- CAMBIO PRINCIPAL ---
    // Antes apuntábamos al juego, ahora apuntamos directo a la lección
    private Integer leccionId;

    private Integer completado;
    private Integer puntuacion;

    // Eliminamos 'intentos' para coincidir con tu nueva tabla simplificada
    // private Integer intentos;

    private Instant fecha;
}