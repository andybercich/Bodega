package org.example.Controllers;

import org.example.Entities.Direccion;
import org.example.Repositories.DireccionRepository;
import org.example.Services.DireccionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direccion")
public class DireccionController extends BaseController<Direccion, Long,DireccionRepository, DireccionService>{
    public DireccionController(DireccionService service) {
        super(service);
    }

}
