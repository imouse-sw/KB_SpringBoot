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
@Table(name = "tbl_progreso")
public class Progreso {
    @Id
    @Column(name = "id_progreso")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // --- CAMBIO CLAVE: Ahora nos conectamos directo a la Lección ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_leccion")
    private Leccion leccion;

    @Column(name = "completado")
    private Integer completado;

    @Column(name = "puntuacion_obtenida")
    private Integer puntuacion;

    @Column(name = "fecha")
    private Instant fecha;
}