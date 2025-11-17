package com.example.kidzbraindb.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.kidzbraindb.model.Usuario;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface UsuarioService {
    List<Usuario> getAll();
    Usuario getById(Integer id);
    Usuario getByCorreo(String correo);
    Usuario save(Usuario usuario);
    void delete(Integer id);
    Usuario update(Integer id, Usuario usuario);
}
