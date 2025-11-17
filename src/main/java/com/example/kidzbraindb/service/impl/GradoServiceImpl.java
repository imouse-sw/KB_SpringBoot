package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.repository.GradoRepository;
import com.example.kidzbraindb.service.GradoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GradoServiceImpl implements GradoService {
    private final GradoRepository gradoRepository;

    @Override
    public List<Grado> getAll() {
        return gradoRepository.findAll();
    }

    @Override
    public Grado getById(Integer id) {
        return gradoRepository.findById(id).orElse(null);
    }

    @Override
    public Grado save(Grado grado) {
        return gradoRepository.save(grado);
    }

    @Override
    public void delete(Integer id) {
        gradoRepository.deleteById(id);
    }

    @Override
    public Grado update(Integer id, Grado grado) {
        Grado aux = gradoRepository.findById(id).orElse(null);

        if(aux==null) {
            return null;
        }

        aux.setNombre(grado.getNombre());
        return gradoRepository.save(grado);
    }
}
