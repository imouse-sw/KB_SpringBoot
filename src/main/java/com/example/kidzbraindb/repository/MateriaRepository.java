package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Materia;
import com.example.kidzbraindb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {
}
