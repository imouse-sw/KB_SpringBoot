package com.example.kidzbraindb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {

    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "Debe tener un formato de correo válido.")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
}
