package org.example;

import org.example.Entities.Enum.Rol;
import org.example.Entities.Usuario;
import org.example.Repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminMail = "admin@tuapp.com";

            if (usuarioRepository.findByMail(adminMail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setMail(adminMail);
                admin.setRol(Rol.ADMIN);
                admin.setPassword(passwordEncoder.encode("admin123"));
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado con éxito");
            } else {
                System.out.println("Usuario ADMIN ya existe");
            }
        };
    }
}

