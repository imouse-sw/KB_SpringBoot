package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.repository.UsuarioRepository;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario getByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        if(usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if(usuario!=null){
            usuarioRepository.deleteById(id);
        }
    }

    @Override
    public Usuario update(Integer id, Usuario usuario) {
        Usuario aux = usuarioRepository.findById(id).orElse(null);

        if(aux==null) {
            return null;
        }

        Optional<Usuario> repetido = usuarioRepository.findByCorreo(usuario.getCorreo());
        if(repetido.isPresent() && !repetido.get().getId().equals(id)) {
            throw new RuntimeException("Este correo ya está en uso por otra cuenta.");
        }

        aux.setNombre(usuario.getNombre());
        aux.setNombre(usuario.getCorreo());
        aux.setEdad(usuario.getEdad());

        return usuarioRepository.save(aux);
    }
}
