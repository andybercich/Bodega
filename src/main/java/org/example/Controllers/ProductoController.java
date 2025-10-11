package org.example.Controllers;

import org.example.Entities.Dto.ProductoDTO;
import org.example.Entities.Dto.ProductoPageDTO;
import org.example.Entities.Enum.EstadoProducto;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.example.Services.DescuentoService;
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

    public ProductoController(ProductoService service, DescuentoService descuentoService) {
        super(service);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll() {
        try {
            List<ProductoDTO> productosDTO = service.getAllProductos();
            return ResponseEntity.ok(productosDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Long id) {
        try {
            ProductoDTO productoDTO = service.getProductoById(id);
            return ResponseEntity.ok(productoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/quitar-descuento")
    public ResponseEntity<?> quitarDescuento(@PathVariable Long id) {
        try {
            Producto productoActualizado = service.quitarDescuento(id);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al quitar un descuento: " + e.getMessage());
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

    @GetMapping("/padres")
    public ResponseEntity<?> getProductosPadre(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(service.getProductosPadrePaginados(page, size));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener " +
                            "productos padres paginadas: " + e.getMessage());
        }
    }

    @GetMapping("/hijos/{padreId}")
    public ResponseEntity<List<ProductoDTO>> getHijosByPadre(@PathVariable Long padreId) {
        try {
            List<ProductoDTO> hijos = service.getHijosByPadre(padreId);
            return ResponseEntity.ok(hijos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)EstadoProducto estado
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
                    size,
                    estado
            );

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/searchFather")
    public ResponseEntity<ProductoPageDTO> getFilteredFather(
            @RequestParam(required = false) boolean nuevo,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Boolean conPadre,
            @RequestParam(required = false) List<Long> categorias,
            @RequestParam(required = false, defaultValue = "true") Boolean destacado,
            @RequestParam(required = false) Boolean conDescuento,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)EstadoProducto estado
    ) {
        try {
            ProductoPageDTO resultado = service.buscarProductosPadre(
                    nuevo,
                    stockMin,
                    stockMax,
                    fechaDesde,
                    fechaHasta,
                    conPadre,
                    categorias,
                    destacado,
                    conDescuento,
                    keyword,
                    page,
                    size,
                    estado
            );

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/conectedProducts/{idProduct}")
    public ResponseEntity<List<ProductoDTO>> connectedProducts(@PathVariable Long idProduct){
        try {
            return ResponseEntity.ok(service.obtenerRelacionados(idProduct));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}