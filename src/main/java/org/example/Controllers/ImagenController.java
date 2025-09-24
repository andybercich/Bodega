package org.example.Controllers;

import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Imagen;
import org.example.Repositories.ImagenRepository;
import org.example.Services.ImagenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagen")
public class ImagenController extends BaseController<Imagen,Long, ImagenRepository, ImagenService> {

    public ImagenController(ImagenService service) {
        super(service);
    }

    @PostMapping("/producto/{productoId}")
    public ResponseEntity<?> agregarImagen(@PathVariable Long productoId, @RequestBody ImagenDTO dto) {
        try{
            Imagen imagen = service.agregarImagenAProducto(productoId, dto);
            return ResponseEntity.ok(imagen);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());

        }
    }


    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> getImagenesPorProducto(@PathVariable Long productoId) {
        try{
            List<Imagen> imagenes = service.obtenerImagenesPorProducto(productoId);
            return ResponseEntity.ok(imagenes);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener imagenes de un producto: " + e.getMessage());

        }
    }


}
