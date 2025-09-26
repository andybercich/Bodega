package org.example.Controllers;

import org.example.Entities.Descuento;
import org.example.Repositories.DescuentoRepository;
import org.example.Services.DescuentoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descuento")
public class DescuentoController extends BaseController<Descuento,Long, DescuentoRepository, DescuentoService>{

    public DescuentoController(DescuentoService service) {
        super(service);
    }

    @GetMapping("/paginados")
    public ResponseEntity<?> getDescuentosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        try {
            Page<Descuento> descuentos = service.getDescuentosPaginados(page, size);
            return ResponseEntity.ok(descuentos);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener descuentos paginados: " + e.getMessage());
        }
    }

}
