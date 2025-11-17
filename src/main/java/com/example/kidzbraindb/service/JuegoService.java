package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Leccion;

import java.util.List;

public interface JuegoService {
    List<Juego> getAll();
    Juego getById(Integer id);
    List<Juego> getByLeccionId(Integer idLeccion);
    Juego save(Juego juego);
    void delete(Integer id);
    Juego update(Integer id, Juego juego);
}
