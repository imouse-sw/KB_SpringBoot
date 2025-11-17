package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.repository.JuegoRepository;
import com.example.kidzbraindb.service.JuegoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class JuegoServiceImpl implements JuegoService {
    private final JuegoRepository juegoRepository;

    @Override
    public List<Juego> getAll() {
        return juegoRepository.findAll();
    }

    @Override
    public Juego getById(Integer id) {
        return juegoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Juego> getByLeccionId(Integer idLeccion) {
        return juegoRepository.findByLeccion_Id(idLeccion);
    }

    @Override
    public Juego save(Juego juego) {
        return juegoRepository.save(juego);
    }

    @Override
    public void delete(Integer id) {
        juegoRepository.deleteById(id);
    }

    @Override
    public Juego update(Integer id, Juego juego) {
        Juego aux = juegoRepository.findById(id).orElse(null);

        if(aux == null) {
            return null;
        }

        aux.setNombre_juego(juego.getNombre_juego());
        aux.setTipo(juego.getTipo());
        aux.setDescripcion(juego.getDescripcion());

        return juegoRepository.save(juego);
    }
}
