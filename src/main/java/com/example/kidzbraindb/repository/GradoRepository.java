package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Integer> {
}
