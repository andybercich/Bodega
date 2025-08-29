package org.example.Controllers;

import org.example.Entities.UsuariosNuevos;
import org.example.Repositories.UsuariosNuevosRepository;
import org.example.Services.UsuarioNuevoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarioNuevo")
public class UsuarioNuevoController extends BaseController<UsuariosNuevos, Long, UsuariosNuevosRepository,UsuarioNuevoService>{
    public UsuarioNuevoController(UsuarioNuevoService service) {
        super(service);
    }

}
