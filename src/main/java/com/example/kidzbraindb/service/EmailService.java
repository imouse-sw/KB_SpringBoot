package com.example.kidzbraindb.service;

public interface EmailService {
    void enviarEmail(String destinatario, String asunto, String mensaje);
}
