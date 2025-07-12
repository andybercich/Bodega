package org.example.Services;

import org.example.Entities.Usuario;
import org.example.Repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> {
}
