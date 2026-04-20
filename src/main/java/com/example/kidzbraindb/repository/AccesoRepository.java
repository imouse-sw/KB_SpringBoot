package com.example.kidzbraindb.repository;

import com.example.kidzbraindb.model.Acceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccesoRepository extends JpaRepository<Acceso, Integer>{
    List<Acceso> findByUsuarioId(Integer usuarioId);
    List<Acceso> findByAccionRealizada(String accion);
    List<Acceso> findByUsuarioIdAndAccionRealizada(Integer usuarioId, String accion);
    List<Acceso> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
}
