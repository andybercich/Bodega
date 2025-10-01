package org.example.Controllers;

import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Imagen;
import org.example.Repositories.ImagenRepository;
import org.example.Services.CloudinaryService;
import org.example.Services.ImagenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/imagen")
public class ImagenController extends BaseController<Imagen,Long, ImagenRepository, ImagenService> {

    private final CloudinaryService cloudinaryService;

    public ImagenController(ImagenService service, CloudinaryService cloudinaryService) {
        super(service);
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/producto/{productoId}")
    public ResponseEntity<?> agregarImagenProducto(
            @PathVariable Long productoId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("alt") String alt
    ) {
        try {
            Map uploadResult = cloudinaryService.upload(file);
            String url = uploadResult.get("secure_url").toString(); //Extraemos url segura

            ImagenDTO dto = new ImagenDTO(alt, url, true);

            Imagen imagen = service.agregarImagenAProducto(productoId, dto);

            return ResponseEntity.ok(imagen);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());
        }
    }

    @PostMapping("/articulo/{articuloId}")
    public ResponseEntity<?> agregarImagenArticulo(
            @PathVariable Long articuloId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("alt") String alt
    ) {
        try {
            Map uploadResult = cloudinaryService.upload(file);
            String url = uploadResult.get("secure_url").toString();

            ImagenDTO dto = new ImagenDTO(alt,url,true);
            Imagen imagen = service.agregarImagenArticulo(articuloId,dto);

            return  ResponseEntity.ok(imagen);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());
        }
    }

    @PutMapping("/imagen/{imagenId}")
    public ResponseEntity<?> editarImagen(
            @PathVariable Long imagenId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("alt") String alt
    ) {
        try {
            Imagen imagen = service.findById(imagenId);

            if (file != null && !file.isEmpty()) {
                Map result = cloudinaryService.upload(file);
                imagen.setUrl(result.get("secure_url").toString()); //cambiamos url de la imagen
            }
            imagen.setAlt(alt);

            service.save(imagen);

            return ResponseEntity.ok(imagen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al editar la imagen: " + e.getMessage());
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

    @GetMapping("/articulo/{idArticulo}")
    public ResponseEntity<?> getImagenByArticulo(@PathVariable Long idArticulo) {
        try {
            Imagen imagen = service.obtenerImagenPorArticulo(idArticulo);
            if (imagen != null) {
                return ResponseEntity.ok(imagen);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener imagenes de un producto: " + e.getMessage());
        }
    }

}
