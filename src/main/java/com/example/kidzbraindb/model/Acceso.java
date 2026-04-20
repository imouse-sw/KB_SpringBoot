package com.example.kidzbraindb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_Accesos")
public class Acceso {
    @Id
    @Column(name = "id_acceso")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "accion_realizada", nullable = false)
    private String accionRealizada;

    @CreationTimestamp
    @Column(name = "fecha", updatable = false)
    private Instant fecha;
}
