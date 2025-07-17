package org.example.Controllers;

import org.example.Entities.Articulo;
import org.example.Repositories.ArticuloRepository;
import org.example.Services.ArticuloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articulos")
public class ArticuloController extends BaseController<Articulo,Long, ArticuloRepository, ArticuloService>{

    public ArticuloController(ArticuloService service) {
        super(service);
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Articulo articulo) {
        try {
            Articulo articuloGuardado = service.guardarArticuloConFotos(articulo);
            return ResponseEntity.ok(articuloGuardado);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el articulo: " + e.getMessage());
        }
    }

}
