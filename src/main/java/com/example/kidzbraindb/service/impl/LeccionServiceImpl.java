package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.repository.LeccionRepository;
import com.example.kidzbraindb.repository.ProgresoRepository;
import com.example.kidzbraindb.service.LeccionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class LeccionServiceImpl implements LeccionService {
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;


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

    @Override
    public Integer getSiguienteOrdenDesbloqueado(Integer idUsuario, Integer idMateria, Integer idGrado) {
        // 1. Preguntamos al repositorio cuál es el orden MÁXIMO que ya completó
        // (Asegúrate de haber puesto el @Query en ProgresoRepository como vimos antes)
        Integer maxOrden = progresoRepository.findMaxOrdenCompletado(idUsuario, idMateria, idGrado);

        // 2. Decidimos qué devolver
        if (maxOrden == null) {
            // Si nunca ha jugado, la primera lección (Orden 1) es la que está abierta
            return 1;
        } else {
            // Si ya acabó la 1, le toca la 2. Si acabó la 5, le toca la 6.
            return maxOrden + 1;
        }
    }

}
