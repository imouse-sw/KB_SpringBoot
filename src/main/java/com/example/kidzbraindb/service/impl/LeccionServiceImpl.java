package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.repository.LeccionRepository;
import com.example.kidzbraindb.service.LeccionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class LeccionServiceImpl implements LeccionService {
    private final LeccionRepository leccionRepository;

    @Override
    public List<Leccion> getAll() {
        return leccionRepository.findAll();
    }

    @Override
    public Leccion getById(Integer id) {
        return leccionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Leccion> getByMateriaAndGrado(Integer idMateria, Integer idGrado) {
        return leccionRepository.findByMateria_IdAndGrado_Id(idMateria, idGrado);
    }

    @Override
    public Leccion save(Leccion leccion) {
        return leccionRepository.save(leccion);
    }

    @Override
    public void delete(Integer id) {
        leccionRepository.deleteById(id);
    }

    @Override
    public Leccion update(Integer id, Leccion leccion) {
        Leccion aux = leccionRepository.findById(id).orElse(null);

        if(aux==null) {
            return null;
        }

        aux.setTitulo(leccion.getTitulo());
        aux.setOrden(leccion.getOrden());
        aux.setDescripcion(leccion.getDescripcion());

        aux.setMateria(leccion.getMateria());
        aux.setGrado(leccion.getGrado());

        return leccionRepository.save(aux);
    }
}
