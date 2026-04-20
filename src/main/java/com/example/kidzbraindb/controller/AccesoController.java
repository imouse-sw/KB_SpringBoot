package com.example.kidzbraindb.controller;

import com.example.kidzbraindb.dto.AccesoDto;
import com.example.kidzbraindb.dto.JuegoDto;
import com.example.kidzbraindb.model.Acceso;
import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.AccesoService;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/accesos")
@RestController
@AllArgsConstructor
public class AccesoController {

    private final AccesoService accesoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<AccesoDto>> getAll() {
        List<Acceso> accesos = accesoService.getAll();
        List<AccesoDto> dtos = accesos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AccesoDto> getById(@PathVariable Integer id) {
        Acceso acceso = accesoService.getById(id);
        if (acceso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(acceso));
    }

    @GetMapping("/accion/{accion}")
    public ResponseEntity<List<AccesoDto>> getByAccion(@PathVariable String accion) {
        return ResponseEntity.ok(
                accesoService.getByAccionRealizada(accion)
                        .stream()
                        .map(this::convertirAEntidadDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AccesoDto>> getByUsuarioId(@PathVariable Integer idUsuario) {
        List<Acceso> accesos = accesoService.getByUsuarioId(idUsuario);
        List<AccesoDto> dtos = accesos
                .stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{idUsuario}/recientes")
    public ResponseEntity<List<AccesoDto>> getByUsuarioRecientes(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(
                accesoService.getByUsuarioIdOrderByFechaDesc(idUsuario).stream()
                        .map(this::convertirAEntidadDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/usuario/{usuarioId}/accion/{accion}")
    public ResponseEntity<List<AccesoDto>> getByUsuarioAndAccion(@PathVariable Integer usuarioId, @PathVariable String accion) {
        return ResponseEntity.ok(
                accesoService.getByUsuarioIdAndAccionRealizada(usuarioId, accion).stream()
                        .map(this::convertirAEntidadDTO)
                        .collect(Collectors.toList())
        );
    }

    private AccesoDto convertirAEntidadDTO(Acceso acceso) {
        return AccesoDto.builder()
                .accesoId(acceso.getId())
                .usuarioId(acceso.getUsuario().getId())
                .accionRealizada(acceso.getAccionRealizada())
                .fechaAcceso(acceso.getFecha())
                .build();
    }

    @PostMapping
    public ResponseEntity<AccesoDto> save(@RequestBody AccesoDto dto) {
        Usuario usuario = usuarioService.getById(dto.getUsuarioId());
        if(usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Acceso acceso = Acceso.builder()
                .usuario(usuario)
                .accionRealizada(dto.getAccionRealizada())
                .build();

        Acceso guardado = accesoService.save(acceso);
        return ResponseEntity.ok(convertirAEntidadDTO(guardado));
    }

    private Acceso convertirAEntidad(AccesoDto dto) {
        Usuario usuario = usuarioService.getById(dto.getUsuarioId());

        return Acceso.builder()
                .id(dto.getAccesoId())
                .usuario(usuario)
                .accionRealizada(dto.getAccionRealizada())
                .fecha(dto.getFechaAcceso())
                .build();
    }
}
