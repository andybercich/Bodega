package org.example.Controllers;

import org.example.Entities.Categoria;
import org.example.Repositories.CategoriaRepository;
import org.example.Services.CategoriaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends BaseController<Categoria,Long, CategoriaRepository, CategoriaService>{

    public CategoriaController(CategoriaService service) {
        super(service);
    }

    @GetMapping("/paginados")
    public ResponseEntity<?> getCategoriasPaginados(
            @RequestParam int page,
            @RequestParam int size) throws Exception{
        try {
            Page<Categoria> categorias = service.getCategoriasPaginados(page, size);
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener categorias paginadas: " + e.getMessage());
        }
    }

}
