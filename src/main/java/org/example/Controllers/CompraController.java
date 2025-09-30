package org.example.Controllers;

import org.example.Entities.Compra;
import org.example.Entities.Dto.CompraPageDTO;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Repositories.CompraRepository;
import org.example.Services.CompraService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/user/{idUser}")
    public ResponseEntity<?> getComprasByIdUser(@PathVariable Long idUser){
        try {

            return ResponseEntity.ok(service.getByIdUser(idUser));

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener compras de usuario: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CompraPageDTO> getFiltered(
            @RequestParam(required = false) String codigoSeguimiento,
            @RequestParam(required = false) String nombreUsuario,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) List<EstadoCompra> estados,
            @RequestParam(defaultValue = "false") boolean nuevo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            CompraPageDTO resultado = service.buscarCompras(
                    codigoSeguimiento,
                    nombreUsuario,
                    fechaDesde,
                    fechaHasta,
                    estados,
                    nuevo,
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
