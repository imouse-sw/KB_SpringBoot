package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.model.Leccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeccionRepository extends JpaRepository<Leccion, Integer> {
    List<Leccion> findByMateria_IdAndGrado_Id(Integer idMateria, Integer idGrado);
}
