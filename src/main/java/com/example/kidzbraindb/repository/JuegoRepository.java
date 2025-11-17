package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Leccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    List<Juego> findByLeccion_Id(Integer idLeccion);
}
