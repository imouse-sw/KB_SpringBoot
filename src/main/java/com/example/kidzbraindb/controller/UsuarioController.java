package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.LoginDto;
import com.example.kidzbraindb.dto.RestablecerPasswordDto;
import com.example.kidzbraindb.dto.SolicitudRecuperacionDto;
import com.example.kidzbraindb.dto.UsuarioDto;
import com.example.kidzbraindb.model.Acceso;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.AccesoService;
import com.example.kidzbraindb.service.EmailService;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/usuarios")
@RestController
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AccesoService accesoService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private List<UsuarioDto> usuarioDtos;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> list(@RequestParam (name = "nombre", defaultValue = "", required = false) String user) {
        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(user != null && !user.isEmpty()) {
            return ResponseEntity.ok(
                    usuarios.stream()
                            .filter(u -> u.getNombre().equals(user))
                            .map(u ->
                                    UsuarioDto.builder()
                                            .usuarioId(u.getId())
                                            .nombre(u.getNombre())
                                            .correo(u.getCorreo())
                                            .password(u.getPassword())
                                            .edadHijo(u.getEdad())
                                            .fechaRegistro(u.getFecha_registro())
                                            .fotoUrl(u.getFotoUrl()) // <-- AGREGADO
                                            .build())
                            .collect(Collectors.toList())
            );
        }
        return ResponseEntity.ok(
                usuarios.stream()
                        .map(u ->
                                UsuarioDto.builder()
                                        .usuarioId(u.getId())
                                        .nombre(u.getNombre())
                                        .correo(u.getCorreo())
                                        .password(u.getPassword())
                                        .edadHijo(u.getEdad())
                                        .fechaRegistro(u.getFecha_registro())
                                        .fotoUrl(u.getFotoUrl())
                                        .build())
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody LoginDto loginDto) {
        // busca al usuario por el correo que manda android
        Usuario u = usuarioService.getByCorreo(loginDto.getCorreo());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // chingalo
        }

        // si el usuario existe, compara la contraseña enviada con el del logindto
        if (passwordEncoder.matches(loginDto.getPassword(), u.getPassword())) {
            // devolvemos los datos para que android arme su SharedPreferences
            accesoService.save(Acceso.builder()
                    .usuario(u)
                    .accionRealizada("LOGIN_EXITOSO")
                    .build());

            return ResponseEntity.ok(
                    UsuarioDto.builder()
                            .usuarioId(u.getId())
                            .nombre(u.getNombre())
                            .correo(u.getCorreo())
                            .edadHijo(u.getEdad())
                            //no se envia la contraseña por seguridad
                            .fechaRegistro(u.getFecha_registro())
                            .fotoUrl(u.getFotoUrl())
                            .build()
            );
        }
        else {
            accesoService.save(Acceso.builder()
                    .usuario(u)
                    .accionRealizada("LOGIN_FALLIDO")
                    .build());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Código 401
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Integer id) {
        Usuario u = usuarioService.getById( id );

        if(u == null )
        {
            return ResponseEntity.notFound( ).build( );
        }
        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .fotoUrl(u.getFotoUrl())
                        .build()
        );
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody SolicitudRecuperacionDto dto) {
        Usuario usuario = usuarioService.getByCorreo(dto.getCorreo());

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // genera un código aleatorio de 6 dígitos
        int numeroAleatorio = (int) (Math.random() * 900000) + 100000;
        String codigo = String.valueOf(numeroAleatorio);

        // le da una vigencia de 15 minutos
        Instant expiracion = Instant.now().plus(15, java.time.temporal.ChronoUnit.MINUTES);

        // guarda el código y la fecha
        usuario.setCodigoRecuperacion(codigo);
        usuario.setExpiracionCodigo(expiracion);
        usuarioService.save(usuario); // actualiza en bd

        String asunto = "KidzBrain - Código de recuperación de contraseña";
        String mensaje = "Hola, " + usuario.getNombre() + ".\n\n"
                + "Recibimos una solicitud para restablecer tu contraseña en KidzBrain.\n"
                + "Tu código de seguridad de 6 dígitos es: " + codigo + "\n\n"
                + "Este código expirará en 15 minutos.\n"
                + "Si no solicitaste este cambio, puedes ignorar este correo.\n\n"
                + "Atentamente,\nEl equipo de iMouse.";

        emailService.enviarEmail(usuario.getCorreo(), asunto, mensaje);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> restablecerPassword(@RequestBody RestablecerPasswordDto dto) {
        Usuario usuario = usuarioService.getByCorreo(dto.getCorreo());

        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }

        // el código no coincide
        if (usuario.getCodigoRecuperacion() == null || !usuario.getCodigoRecuperacion().equals(dto.getCodigo())) {
            return ResponseEntity.status(400).body("El código de seguridad es incorrecto.");
        }

        // el código ya expiró conchesumare :(
        if (usuario.getExpiracionCodigo().isBefore(java.time.Instant.now())) {
            return ResponseEntity.status(400).body("El código ha expirado, solicita uno nuevo.");
        }

        // encripta la nueva contraseña
        String passwordEncriptada = passwordEncoder.encode(dto.getNuevaPassword());
        usuario.setPassword(passwordEncriptada);

        // quitamos los campos de recuperación
        usuario.setCodigoRecuperacion(null);
        usuario.setExpiracionCodigo(null);

        usuarioService.save(usuario);

        return ResponseEntity.ok("¡Contraseña actualizada con éxito!");
    }

    @GetMapping("/mail/{correo}")
    public ResponseEntity<UsuarioDto> getByCorreo(@PathVariable String correo) {
        Usuario u = usuarioService.getByCorreo(correo);

        if (u == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .fotoUrl(u.getFotoUrl()) // <--- ¡CRUCIAL PARA EL LOGIN!
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> save(@RequestBody UsuarioDto usuarioDto) {
        String passwordHasheada = passwordEncoder.encode(usuarioDto.getPassword());

        Usuario u = Usuario
                .builder()
                .id(usuarioDto.getUsuarioId())
                .nombre(usuarioDto.getNombre())
                .correo(usuarioDto.getCorreo())
                .password(passwordHasheada)
                .edad(usuarioDto.getEdadHijo())
                .fecha_registro(usuarioDto.getFechaRegistro())
                .build();

        Usuario guardado = usuarioService.save(u);

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(guardado.getId())
                        .nombre(guardado.getNombre())
                        .correo(guardado.getCorreo())
                        .password(guardado.getPassword())
                        .edadHijo(guardado.getEdad())
                        .fechaRegistro(guardado.getFecha_registro())
                        .build()
        );
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<UsuarioDto> delete(@PathVariable Integer id)
    {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        Usuario u = usuarioService.update(id, Usuario
                .builder()
                .id(usuarioDto.getUsuarioId())
                .nombre(usuarioDto.getNombre())
                .correo(usuarioDto.getCorreo())
                .password(usuarioDto.getPassword())
                .edad(usuarioDto.getEdadHijo())
                .fecha_registro(usuarioDto.getFechaRegistro())
                .build());

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .fotoUrl(u.getFotoUrl()) // También útil devolverlo al actualizar
                        .build()
        );
    }

    @PostMapping(
            value = "/id/{id}/foto",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<String> subirFotoPerfil(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile foto
    ) {
        System.out.println("--> RECIBIENDO PETICIÓN DE SUBIDA PARA USUARIO ID: " + id);

        try {
            if (foto.isEmpty()) {
                System.out.println("Error: Archivo vacío");
                return ResponseEntity.badRequest().body("Archivo vacío");
            }

            Usuario usuario = usuarioService.getById(id);
            if (usuario == null) {
                System.out.println("Error: Usuario no encontrado en BD");
                return ResponseEntity.notFound().build();
            }

            String contentType = foto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                System.out.println("Error: No es imagen, es " + contentType);
                return ResponseEntity.badRequest().body("Archivo no es una imagen");
            }

            String projectDir = System.getProperty("user.dir");
            String uploadDir = projectDir + File.separator + "uploads" + File.separator + "usuarios" + File.separator;

            System.out.println("Intentando guardar en: " + uploadDir);

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean creados = directory.mkdirs();
                System.out.println("¿Directorios creados?: " + creados);
            }

            String extension = "jpg";
            if (contentType != null) {
                if (contentType.contains("png")) {
                    extension = "png";
                } else if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                    extension = "jpg";
                }
            }

            String nombreArchivo = "usuario_" + id + "." + extension;
            Path rutaArchivo = Paths.get(uploadDir + nombreArchivo);

            try (InputStream inputStream = foto.getInputStream()) {
                Files.copy(inputStream, rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("¡ARCHIVO GUARDADO EXITOSAMENTE!");
            }

            String urlFoto = "/uploads/usuarios/" + nombreArchivo;
            usuario.setFotoUrl(urlFoto);
            usuarioService.save(usuario);

            return ResponseEntity.ok(urlFoto);

        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO AL SUBIR:");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al subir imagen: " + e.getMessage());
        }
    }
}