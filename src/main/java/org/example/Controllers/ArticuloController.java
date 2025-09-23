package org.example.Controllers;

import org.example.Entities.Articulo;
import org.example.Repositories.ArticuloRepository;
import org.example.Services.ArticuloService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articulos")
public class ArticuloController extends BaseController<Articulo,Long, ArticuloRepository, ArticuloService>{

    public ArticuloController(ArticuloService service) {
        super(service);
    }


    @GetMapping("/paginados")
    public ResponseEntity<?> getArticulosPaginados(
            @RequestParam int page,
            @RequestParam int size) throws Exception{

        try {
            Page<Articulo> articulos = service.getArticulosPaginados(page, size);
            return ResponseEntity.ok(articulos);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener articulos paginados: " + e.getMessage());
        }
    }

}
