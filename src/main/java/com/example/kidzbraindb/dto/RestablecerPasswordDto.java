package com.example.kidzbraindb.dto;

import lombok.Data;

@Data
public class RestablecerPasswordDto {
    private String correo;
    private String codigo;
    private String nuevaPassword;
}