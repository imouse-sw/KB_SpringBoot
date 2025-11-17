package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.MateriaDto;
import com.example.kidzbraindb.model.Materia;
import com.example.kidzbraindb.service.MateriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/materias")
@RestController
@AllArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping
    public ResponseEntity<List<MateriaDto>> getAll() {
        List<Materia> materias = materiaService.getAll();
        List<MateriaDto> dtos = materias.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MateriaDto> getById(@PathVariable Integer id) {
        Materia materia = materiaService.getById(id);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(materia));
    }

    @PostMapping
    public ResponseEntity<MateriaDto> save(@RequestBody MateriaDto dto) {
        Materia materiaParaGuardar = convertirAEntidad(dto);
        Materia materiaGuardada = materiaService.save(materiaParaGuardar);
        return ResponseEntity.ok(convertirAEntidadDTO(materiaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaDto> update(@PathVariable Integer id, @RequestBody MateriaDto dto) {
        Materia materiaParaActualizar = convertirAEntidad(dto);
        Materia materiaActualizada = materiaService.update(id, materiaParaActualizar);
        return ResponseEntity.ok(convertirAEntidadDTO(materiaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        materiaService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private MateriaDto convertirAEntidadDTO(Materia materia) {
        return MateriaDto.builder()
                .materiaId(materia.getId())
                .nombre(materia.getNombre())
                .build();
    }

    private Materia convertirAEntidad(MateriaDto dto) {
        return Materia.builder()
                .id(dto.getMateriaId())
                .nombre(dto.getNombre())
                .build();
    }
}