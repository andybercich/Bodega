package org.example.Controllers;

import org.example.Entities.Direccion;
import org.example.Repositories.DireccionRepository;
import org.example.Services.DireccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direccion")
public class DireccionController extends BaseController<Direccion, Long,DireccionRepository, DireccionService>{
    public DireccionController(DireccionService service) {
        super(service);
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(
            @RequestBody Direccion direccion) {
        try {
            Direccion nueva = service.save(direccion);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar dirección: " + e.getMessage());
        }
    }

    @PutMapping("/update/{direccionId}")
    public ResponseEntity<?> actualizarDireccion(
            @PathVariable Long direccionId,
            @RequestBody Direccion direccion) {
        try {
            Direccion actualizada = service.update(direccionId, direccion);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar dirección: " + e.getMessage());
        }
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<?> obtenerDireccionesPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Direccion> direcciones = service.obtenerDireccionesPorUsuario(usuarioId);
            return ResponseEntity.ok(direcciones);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener direcciones: " + e.getMessage());
        }
    }



}
