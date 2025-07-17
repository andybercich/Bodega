package org.example.Controllers;

import org.example.Entities.Categoria;
import org.example.Repositories.CategoriaRepository;
import org.example.Services.CategoriaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends BaseController<Categoria,Long, CategoriaRepository, CategoriaService>{

    public CategoriaController(CategoriaService service) {
        super(service);
    }

}
