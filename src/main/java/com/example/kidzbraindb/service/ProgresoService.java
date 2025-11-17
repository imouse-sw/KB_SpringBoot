package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Progreso;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface ProgresoService {
    List<Progreso> getAll();
    Progreso getById(Integer id);
    List<Progreso> getByUsuario(Integer idUsuario);
    Integer sumByMateria(Integer idUsuario, Integer idMateria);
    Progreso save(Progreso progreso);
}
