package org.example.Controllers;

import org.example.Entities.DetalleCompra;
import org.example.Repositories.DetalleCompraRepository;
import org.example.Services.DetalleCompraService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/detalle")
public class DetalleController extends BaseController<DetalleCompra, Long, DetalleCompraRepository, DetalleCompraService>{
    public DetalleController(DetalleCompraService service) {
        super(service);
    }
}
