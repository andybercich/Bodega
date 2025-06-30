package org.example.Controllers;

import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.example.Services.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController extends BaseController<Producto,Long, ProductoRepository, ProductoService>{

    public ProductoController(ProductoService service) {
        super(service);
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Producto producto) {
        try {
            Producto productoGuardado = service.guardarProductoConFotos(producto);
            return ResponseEntity.ok(productoGuardado);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el producto: " + e.getMessage());
        }
    }

}
