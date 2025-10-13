package org.example.Services;

import jakarta.transaction.Transactional;
import org.example.Entities.Direccion;
import org.example.Entities.Usuario;
import org.example.Repositories.DireccionRepository;
import org.example.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DireccionService extends BaseService<Direccion, Long, DireccionRepository>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Direccion save(Direccion direccion) {
        Usuario user = usuarioRepository.findByMail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        direccion.getUsuarios().add(user);

        user.getDirecciones().add(direccion);

        return repository.save(direccion);
    }


    @Transactional
    public Direccion update(Long direccionId, Direccion nuevaDireccion) {
        Direccion direccionExistente = repository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + direccionId));

        Usuario user = usuarioRepository.findByMail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));


        direccionExistente.setPais(nuevaDireccion.getPais());
        direccionExistente.setProvincia(nuevaDireccion.getProvincia());
        direccionExistente.setLocalidad(nuevaDireccion.getLocalidad());
        direccionExistente.setCalle(nuevaDireccion.getCalle());
        direccionExistente.setNumero(nuevaDireccion.getNumero());
        direccionExistente.setCodigoPostal(nuevaDireccion.getCodigoPostal());

        if (!direccionExistente.getUsuarios().contains(user)) {
            direccionExistente.getUsuarios().add(user);
            user.getDirecciones().add(direccionExistente);
        }

        return repository.save(direccionExistente);
    }

    public List<Direccion> obtenerDireccionesPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}
