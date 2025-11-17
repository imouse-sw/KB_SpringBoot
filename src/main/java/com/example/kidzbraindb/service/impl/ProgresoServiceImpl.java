package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Progreso;
import com.example.kidzbraindb.repository.ProgresoRepository;
import com.example.kidzbraindb.service.ProgresoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class ProgresoServiceImpl implements ProgresoService {
    private final ProgresoRepository progresoRepository;

    @Override
    public List<Progreso> getAll() {
        return progresoRepository.findAll();
    }

    @Override
    public Progreso getById(Integer id) {
        return progresoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Progreso> getByUsuario(Integer idUsuario) {
        return progresoRepository.findByUsuario_Id(idUsuario);
    }

    @Override
    public Integer sumByMateria(Integer idUsuario, Integer idMateria) {
        Integer suma = progresoRepository.sumPuntuacionByUsuarioAndMateria(idUsuario, idMateria);
        return (suma == null) ? 0 : suma;
    }

    @Override
    public Progreso save(Progreso progreso) {
        progreso.setFecha(Instant.now());

        return progresoRepository.save(progreso);
    }
}
