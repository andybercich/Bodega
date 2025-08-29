package org.example.Services;

import org.example.Repositories.UsuariosNuevosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodigoService {

    @Autowired
    private UsuariosNuevosRepository usuarioRepository;

    private final Random random = new Random();

    public String generarCodigoUnico() {
        String codigo;
        do {
            codigo = String.format("%04d", random.nextInt(10000));
        }
        while (usuarioRepository.existsByCodigoVerificacion(codigo) );
        return codigo;
    }
}
