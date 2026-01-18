package com.example.kidzbraindb.service.impl;

import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.repository.UsuarioRepository;
import com.example.kidzbraindb.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
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
        Optional<Usuario> existente = usuarioRepository.findByCorreo(usuario.getCorreo());

        // Si encontramos a alguien con ese correo...
        if(existente.isPresent()) {
            // Verificamos: ¿Es un usuario diferente?
            // Si el usuario que llega es nuevo (id null) O tiene un ID distinto al de la BD...
            // ENTONCES sí es un error (robo de correo).
            if (usuario.getId() == null || !usuario.getId().equals(existente.get().getId())) {
                throw new RuntimeException("El correo ya está registrado.");
            }
            // Si el ID es el mismo, no entra al if y permite guardar (es una actualización).
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
        aux.setCorreo(usuario.getCorreo());
        aux.setEdad(usuario.getEdad());

        return usuarioRepository.save(aux);
    }
}
