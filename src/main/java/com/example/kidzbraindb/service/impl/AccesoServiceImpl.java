package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Acceso;
import com.example.kidzbraindb.repository.AccesoRepository;
import com.example.kidzbraindb.service.AccesoService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class AccesoServiceImpl implements AccesoService {
    private final AccesoRepository accesoRepository;

    @Override
    public List<Acceso> getAll() {
        return accesoRepository.findAll();
    }

    @Override
    public Acceso getById(Integer id) {
        return accesoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Acceso> getByUsuarioId(Integer usuarioId) {
        return accesoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Acceso> getByAccionRealizada(String accion) {
        return accesoRepository.findByAccionRealizada(accion);
    }

    @Override
    public List<Acceso> getByUsuarioIdAndAccionRealizada(Integer usuarioId, String accion) {
        return accesoRepository.findByUsuarioIdAndAccionRealizada(usuarioId, accion);
    }

    @Override
    public List<Acceso> getByUsuarioIdOrderByFechaDesc(Integer usuarioId) {
        return accesoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @Override
    public Acceso save(Acceso acceso) {
        return accesoRepository.save(acceso);
    }
}
