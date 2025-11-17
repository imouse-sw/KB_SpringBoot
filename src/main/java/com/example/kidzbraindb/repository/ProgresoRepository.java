package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.model.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoRepository extends JpaRepository<Progreso, Integer> {
   List<Progreso> findByUsuario_Id(Integer id);

   @Query("SELECT SUM(p.puntuacion) " +
           "FROM Progreso p " +
           "JOIN p.juego j " +
           "JOIN j.leccion l " +
           "JOIN l.materia m " +
           "WHERE p.usuario.id = :idUsuario AND m.id = :idMateria")
    Integer sumPuntuacionByUsuarioAndMateria(@Param("idUsuario") Integer idUsuario, @Param("idMateria") Integer idMateria);
}
