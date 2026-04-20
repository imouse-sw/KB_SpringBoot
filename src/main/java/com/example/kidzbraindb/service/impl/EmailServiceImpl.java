package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void enviarEmail(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage correo = new SimpleMailMessage();

        correo.setFrom("${KIDZBRAIN_EMAIL}");
        correo.setTo(destinatario);
        correo.setSubject(asunto);
        correo.setText(mensaje);

        mailSender.send(correo);
    }
}