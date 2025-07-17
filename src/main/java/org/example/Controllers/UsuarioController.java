package org.example.Controllers;

import org.example.Entities.Usuario;
import org.example.Repositories.UsuarioRepository;
import org.example.Services.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseController<Usuario, Long, UsuarioRepository, UsuarioService>{
    public UsuarioController(UsuarioService service) {
        super(service);
    }
}
