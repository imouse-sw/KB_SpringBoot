package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Materia;
import com.example.kidzbraindb.repository.MateriaRepository;
import com.example.kidzbraindb.service.MateriaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaRepository materiaRepository;

    @Override
    public List<Materia> getAll() {
        return materiaRepository.findAll();
    }

    @Override
    public Materia getById(Integer id) {
        return materiaRepository.findById(id).orElse(null);
    }

    @Override
    public Materia save(Materia materia) {
        return materiaRepository.save(materia);
    }

    @Override
    public void delete(Integer id) {
        materiaRepository.deleteById(id);
    }

    @Override
    public Materia update(Integer id, Materia materia) {
        Materia aux = materiaRepository.findById(id).orElse(null);

        if(aux==null) {
            return null;
        }

        aux.setNombre(materia.getNombre());
        return materiaRepository.save(materia);
    }
}
