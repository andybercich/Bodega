package org.example.Services;

import jakarta.transaction.Transactional;
import org.example.Entities.Direccion;
import org.example.Entities.Usuario;
import org.example.Repositories.DireccionRepository;
import org.example.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionService extends BaseService<Direccion, Long, DireccionRepository>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Direccion save(Direccion direccion, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        direccion.getUsuarios().add(usuario);

        usuario.getDirecciones().add(direccion);

        return repository.save(direccion);
    }


    @Transactional
    public Direccion update(Long direccionId, Direccion nuevaDireccion, Long usuarioId) {
        Direccion direccionExistente = repository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + direccionId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        direccionExistente.setPais(nuevaDireccion.getPais());
        direccionExistente.setProvincia(nuevaDireccion.getProvincia());
        direccionExistente.setLocalidad(nuevaDireccion.getLocalidad());
        direccionExistente.setCalle(nuevaDireccion.getCalle());
        direccionExistente.setNumero(nuevaDireccion.getNumero());
        direccionExistente.setCodigoPostal(nuevaDireccion.getCodigoPostal());

        if (!direccionExistente.getUsuarios().contains(usuario)) {
            direccionExistente.getUsuarios().add(usuario);
            usuario.getDirecciones().add(direccionExistente);
        }

        return repository.save(direccionExistente);
    }

    public List<Direccion> obtenerDireccionesPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}
