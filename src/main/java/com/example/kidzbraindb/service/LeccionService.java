package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Leccion;

import java.util.List;

public interface LeccionService {
    List<Leccion> getAll();
    Leccion getById(Integer id);
    List<Leccion> getByMateriaAndGrado(Integer idMateria, Integer idGrado);
    Leccion save(Leccion leccion);
    void delete(Integer id);
    Leccion update(Integer id, Leccion leccion);
}
