package org.example.Controllers;

import org.example.Entities.CodigoDescuento;
import org.example.Repositories.CodigoDescuentoRepository;
import org.example.Services.CodigoDescuentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codDescuento")
public class CodigoDescuentoController extends BaseController<CodigoDescuento, Long, CodigoDescuentoRepository,
        CodigoDescuentoService>{


    public CodigoDescuentoController(CodigoDescuentoService service) {
        super(service);
    }
}
