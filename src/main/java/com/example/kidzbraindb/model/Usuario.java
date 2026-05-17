package com.example.kidzbraindb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.List;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_Usuarios")
public class Usuario {
    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String nombre;

    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "Debe tener un formato de correo válido.")
    @Column(name = "correo", nullable = false)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Column(name = "contraseña", nullable = false)
    private String password;

    @Column(name = "edad_hijo", nullable = false)
    private Integer edad;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private Instant fecha_registro;

    @Column(name = "codigo_recuperacion", length = 6)
    private String codigoRecuperacion;

    @Column(name = "expiracion_codigo")
    private Instant expiracionCodigo;

    @Column(name = "foto_url")
    private String fotoUrl;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Acceso> accesos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progreso> progresos;
}
