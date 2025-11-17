package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.LeccionDto;
import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.model.Materia;
import com.example.kidzbraindb.service.GradoService;
import com.example.kidzbraindb.service.LeccionService;
import com.example.kidzbraindb.service.MateriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/lecciones")
@RestController
@AllArgsConstructor
public class LeccionController {

    private final LeccionService leccionService;
    private final MateriaService materiaService;
    private final GradoService gradoService;

    @GetMapping
    public ResponseEntity<List<LeccionDto>> getAll() {
        List<Leccion> lecciones = leccionService.getAll();
        List<LeccionDto> dtos = lecciones.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeccionDto> getById(@PathVariable Integer id) {
        Leccion leccion = leccionService.getById(id);
        if (leccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(leccion));
    }

    @GetMapping("/materia/{idMateria}/grado/{idGrado}")
    public ResponseEntity<List<LeccionDto>> getByMateriaAndGrado(
            @PathVariable Integer idMateria, @PathVariable Integer idGrado) {

        List<Leccion> lecciones = leccionService.getByMateriaAndGrado(idMateria, idGrado);
        List<LeccionDto> dtos = lecciones.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<LeccionDto> save(@RequestBody LeccionDto dto) {
        Leccion leccionParaGuardar = convertirAEntidad(dto);
        Leccion leccionGuardada = leccionService.save(leccionParaGuardar);
        return ResponseEntity.ok(convertirAEntidadDTO(leccionGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeccionDto> update(@PathVariable Integer id, @RequestBody LeccionDto dto) {
        Leccion leccionParaActualizar = convertirAEntidad(dto);
        Leccion leccionActualizada = leccionService.update(id, leccionParaActualizar);
        return ResponseEntity.ok(convertirAEntidadDTO(leccionActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        leccionService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private LeccionDto convertirAEntidadDTO(Leccion leccion) {
        return LeccionDto.builder()
                .leccionId(leccion.getId())
                .titulo(leccion.getTitulo())
                .descripcion(leccion.getDescripcion())
                .orden(leccion.getOrden())
                .materiaId(leccion.getMateria().getId())
                .materiaNombre(leccion.getMateria().getNombre())
                .gradoId(leccion.getGrado().getId())
                .gradoNombre(leccion.getGrado().getNombre())
                .build();
    }

    private Leccion convertirAEntidad(LeccionDto dto) {
        Materia materia = materiaService.getById(dto.getMateriaId());
        Grado grado = gradoService.getById(dto.getGradoId());

        return Leccion.builder()
                .id(dto.getLeccionId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .orden(dto.getOrden())
                .materia(materia)
                .grado(grado)
                .build();
    }
}