package org.example.Controllers;

import org.example.Entities.Dto.ProductoPageDTO;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.example.Services.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController extends BaseController<Producto,Long, ProductoRepository, ProductoService> {

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
    @GetMapping("/destacados")
    public ResponseEntity<ProductoPageDTO> getDestacados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(service.getProductosDestacados(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);


        }
    }
}
