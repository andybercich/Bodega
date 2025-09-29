package org.example.Controllers;

import org.example.Entities.Compra;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Repositories.CompraRepository;
import org.example.Services.CompraService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra")
public class CompraController extends BaseController<Compra, Long, CompraRepository, CompraService>{
    public CompraController(CompraService service) {
        super(service);
    }

    @GetMapping("/paginados")
    public ResponseEntity<?> getComprasPaginados(
            @RequestParam int page,
            @RequestParam int size) throws Exception{
        try {
            Page<Compra> compras = service.getComprasPaginados(page, size);
            return ResponseEntity.ok(compras);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener compras paginadas: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable("id") Long compraId,
            @RequestParam("estado") EstadoCompra estadoCompra) {
        try {
            Compra compra = service.actualizarEstado(compraId, estadoCompra);
            return ResponseEntity.ok(compra);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar estado de compra: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cod-seguimiento")
    public ResponseEntity<?> actualizarCodSeguimiento(
            @PathVariable("id") Long compraId,
            @RequestParam("cod-seguimiento") String codSeguimiento) {
        try {
            Compra compra = service.actualizarCodSeguimiento(compraId,codSeguimiento);
            return ResponseEntity.ok(compra);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar estado de compra: " + e.getMessage());
        }
    }


}
