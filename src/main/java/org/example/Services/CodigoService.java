package org.example.Services;

import org.example.Repositories.UsuariosNuevosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class CodigoService {

    @Autowired
    private UsuariosNuevosRepository usuarioRepository;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int MAX_INTENTOS = 20;

    public String generarCodigoUnico() {
        int intentos = 0;
        String codigo;

        do {
            if (intentos++ > MAX_INTENTOS) {
                throw new IllegalStateException("No se pudo generar un código único después de varios intentos");
            }
//Generador de codigos
            codigo = String.format("%06d", secureRandom.nextInt(1_000_000));
        } while (usuarioRepository.existsByCodigoVerificacion(codigo));

        return codigo;
    }
}