package com.example.kidzbraindb.service;

import com.example.kidzbraindb.model.Acceso;

import java.util.List;

public interface AccesoService {
    List<Acceso> getAll();
    Acceso getById(Integer id);
    List<Acceso> getByUsuarioId(Integer usuarioId);
    List<Acceso> getByAccionRealizada(String accion);
    List<Acceso> getByUsuarioIdAndAccionRealizada(Integer usuarioId, String accion);
    List<Acceso> getByUsuarioIdOrderByFechaDesc(Integer usuarioId);
    Acceso save(Acceso acceso);
}
