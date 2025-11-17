package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.JuegoDto;
import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.service.JuegoService;
import com.example.kidzbraindb.service.LeccionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/juegos")
@RestController
@AllArgsConstructor
public class JuegoController {

    private final JuegoService juegoService;
    private final LeccionService leccionService;

    @GetMapping
    public ResponseEntity<List<JuegoDto>> getAll() {
        List<Juego> juegos = juegoService.getAll();
        List<JuegoDto> dtos = juegos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JuegoDto> getById(@PathVariable Integer id) {
        Juego juego = juegoService.getById(id);
        if (juego == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(juego));
    }

    @GetMapping("/leccion/{idLeccion}")
    public ResponseEntity<List<JuegoDto>> getByLeccionId(@PathVariable Integer idLeccion) {
        List<Juego> juegos = juegoService.getByLeccionId(idLeccion);
        List<JuegoDto> dtos = juegos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<JuegoDto> save(@RequestBody JuegoDto dto) {
        Juego juegoParaGuardar = convertirAEntidad(dto);
        Juego juegoGuardado = juegoService.save(juegoParaGuardar);
        return ResponseEntity.ok(convertirAEntidadDTO(juegoGuardado));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JuegoDto> update(@PathVariable Integer id, @RequestBody JuegoDto dto) {
        Juego juegoParaActualizar = convertirAEntidad(dto);
        Juego juegoActualizado = juegoService.update(id, juegoParaActualizar);
        return ResponseEntity.ok(convertirAEntidadDTO(juegoActualizado));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        juegoService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private JuegoDto convertirAEntidadDTO(Juego juego) {
        return JuegoDto.builder()
                .juegoId(juego.getId())
                .nombreJuego(juego.getNombre_juego())
                .tipo(juego.getTipo())
                .descripcion(juego.getDescripcion())
                .leccionId(juego.getLeccion().getId())
                .nombreLeccion(juego.getLeccion().getTitulo())
                .build();
    }

    private Juego convertirAEntidad(JuegoDto dto) {
        Leccion leccion = leccionService.getById(dto.getLeccionId());

        return Juego.builder()
                .id(dto.getJuegoId())
                .nombre_juego(dto.getNombreJuego())
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .leccion(leccion)
                .build();
    }
}