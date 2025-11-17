package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Materia;

import java.util.List;

public interface MateriaService {
    List<Materia> getAll();
    Materia getById(Integer id);
    Materia save(Materia materia);
    void delete(Integer id);
    Materia update(Integer id, Materia materia);
}
