package com.example.kidzbraindb.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder

public class BorrarCuentaDto {
    private String correo;
    private String password;
}
