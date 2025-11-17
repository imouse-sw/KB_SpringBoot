package com.example.kidzbraindb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_Progreso")
public class Progreso {
    @Id
    @Column(name = "id_progreso")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_juego")
    private Juego juego;

    @Column(name = "completado", nullable = false)
    private Integer completado;

    @Column(name = "puntuacion_obtenida")
    private Integer puntuacion;

    @Column(name = "intentos_realizados", nullable = false)
    private Integer intentos;

    @Column(name = "fecha")
    private Instant fecha;
}
