package org.example.Services;

import org.example.Repositories.UsuarioRepository;
import org.example.Repositories.UsuariosNuevosRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CleanNewUsers {

    private final UsuariosNuevosRepository usuarioRepository;

    public CleanNewUsers(UsuariosNuevosRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void eliminarUsuariosViejos() {
        LocalDate limite = LocalDate.now().minusDays(1);
        usuarioRepository.deleteByFechaRegistroBefore(limite);
        System.out.println("Usuarios eliminados creados antes de: " + limite);
    }
}
