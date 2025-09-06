package org.example.Controllers;

import org.example.Entities.Dto.ProductoPageDTO;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.example.Services.ProductoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/search")
    public ResponseEntity<ProductoPageDTO> getFiltered(
            @RequestParam(required = false) boolean nuevo,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Boolean conPadre,
            @RequestParam(required = false) List<Long> categorias,
            @RequestParam(required = false, defaultValue = "true") Boolean destacado,
            @RequestParam(required = false) Boolean conDescuento,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            ProductoPageDTO resultado = service.buscarProductos(
                    nuevo,
                    stockMin,
                    stockMax,
                    precioMin,
                    precioMax,
                    fechaDesde,
                    fechaHasta,
                    conPadre,
                    categorias,
                    destacado,
                    conDescuento,
                    keyword,
                    page,
                    size
            );

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
