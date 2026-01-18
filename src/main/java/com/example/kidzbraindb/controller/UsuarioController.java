package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.UsuarioDto;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/usuarios")
@RestController
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private List<UsuarioDto> usuarioDtos;

    // Puedes borrar este método loadList() si ya no usas la lista en memoria
    public void loadList() {
        usuarioDtos = new ArrayList<>();
        // ...
    }

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
                        .fotoUrl(u.getFotoUrl()) // <--- ¡ESTO FALTABA!
                        .build()
        );
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
        Usuario u = Usuario
                .builder()
                .id(usuarioDto.getUsuarioId())
                .nombre(usuarioDto.getNombre())
                .correo(usuarioDto.getCorreo())
                .password(usuarioDto.getPassword())
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