package org.example.Controllers;

import org.example.Entities.Compra;
import org.example.Repositories.CompraRepository;
import org.example.Services.CompraService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compra")
public class CompraController extends BaseController<Compra, Long, CompraRepository, CompraService>{
    public CompraController(CompraService service) {
        super(service);
    }
}
