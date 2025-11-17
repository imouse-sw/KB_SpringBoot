package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Grado;

import java.util.List;

public interface GradoService {
    List<Grado> getAll();
    Grado getById(Integer id);
    Grado save(Grado grado);
    void delete(Integer id);
    Grado update(Integer id, Grado grado);
}

