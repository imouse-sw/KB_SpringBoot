package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Progreso;
import java.util.List;

public interface ProgresoService {
    List<Progreso> getAll();
    Progreso getById(Integer id);
    List<Progreso> getByUsuario(Integer idUsuario);
    Progreso save(Progreso progreso);
    Integer getPuntuacionPorMateria(Integer idUsuario, Integer idMateria);

}