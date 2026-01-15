package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.ProgresoDto;
import com.example.kidzbraindb.model.Leccion; // Cambio: Importamos Leccion
import com.example.kidzbraindb.model.Progreso;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.LeccionService; // Cambio: Service de Leccion
import com.example.kidzbraindb.service.ProgresoService;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/progreso")
@RestController
@AllArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;
    private final UsuarioService usuarioService;
    private final LeccionService leccionService; // Cambio: Inyectamos LeccionService

    @GetMapping
    public ResponseEntity<List<ProgresoDto>> getAll() {
        List<Progreso> progresos = progresoService.getAll();
        List<ProgresoDto> dtos = progresos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProgresoDto> getById(@PathVariable Integer id) {
        Progreso progreso = progresoService.getById(id);
        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(progreso));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ProgresoDto>> getByUsuario(@PathVariable Integer idUsuario) {
        List<Progreso> progresos = progresoService.getByUsuario(idUsuario);
        List<ProgresoDto> dtos = progresos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ProgresoDto> save(@RequestBody ProgresoDto dto) {
        Progreso progresoParaGuardar = convertirAEntidad(dto);
        Progreso progresoGuardado = progresoService.save(progresoParaGuardar);
        return ResponseEntity.ok(convertirAEntidadDTO(progresoGuardado));
    }

    // --- MÉTODOS DE CONVERSIÓN ACTUALIZADOS ---

    private ProgresoDto convertirAEntidadDTO(Progreso progreso) {
        return ProgresoDto.builder()
                .progresoId(progreso.getId())
                .usuarioId(progreso.getUsuario().getId())
                .leccionId(progreso.getLeccion().getId()) // Cambio: leccionId
                .completado(progreso.getCompletado())
                .puntuacion(progreso.getPuntuacion())
                // .intentos(progreso.getIntentos()) // Eliminado (opcional según tu nueva tabla)
                .fecha(progreso.getFecha())
                .build();
    }

    private Progreso convertirAEntidad(ProgresoDto dto) {
        Usuario usuario = usuarioService.getById(dto.getUsuarioId());

        // Cambio: Buscamos la Lección, no el Juego
        Leccion leccion = leccionService.getById(dto.getLeccionId());

        return Progreso.builder()
                .id(dto.getProgresoId())
                .usuario(usuario)
                .leccion(leccion) // Cambio: Seteamos la lección
                .completado(dto.getCompletado())
                .puntuacion(dto.getPuntuacion())
                // .intentos(dto.getIntentos()) // Eliminado
                .fecha(dto.getFecha())
                .build();
    }
}