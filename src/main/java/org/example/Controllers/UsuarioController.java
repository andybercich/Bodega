package org.example.Controllers;

import jakarta.validation.Valid;
import org.example.Entities.Producto;
import org.example.Entities.Usuario;
import org.example.Repositories.UsuarioRepository;
import org.example.Services.DTO.ValidacionDTO;
import org.example.Services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseController<Usuario, Long, UsuarioRepository, UsuarioService>{
    public UsuarioController(UsuarioService service) {
        super(service);
    }


    @PostMapping("/registrarUsuario")
    public ResponseEntity<?> registrarNuevoUsuario(@RequestBody @Valid Usuario usuario) {
        try {

            Usuario newUser = service.registrarUsuario(usuario);

        return ResponseEntity.ok("d");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar al usuario: " + e.getMessage());
        }
    }

    @PostMapping("/validarMail")
    public ResponseEntity<?> validarMail(@RequestBody ValidacionDTO validacionDTO) {
        try {

            Usuario newUser = service.validarCodigoYRegistrar(validacionDTO);

            return ResponseEntity.ok("d");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar al usuario: " + e.getMessage());
        }
    }

    @PutMapping("favorite/{idProduct}/{idUser}")
    public ResponseEntity<?> agregarFavorito (@PathVariable Long idProduct, @PathVariable Long idUser){
        try {

            return ResponseEntity.ok(service.agregarFavorito(idProduct, idUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar favorito: " + e.getMessage());
        }
    }

    @PutMapping("deleteFavorite/{idProduct}/{idUser}")
    public ResponseEntity<?> eliminarFavorito (@PathVariable Long idProduct, @PathVariable Long idUser){
        try {

            return ResponseEntity.ok(service.eliminarFavorito(idProduct, idUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar favorito: " + e.getMessage());
        }
    }

    @GetMapping("favorites/{idUser}")
    public ResponseEntity<?> obtenerFavoritos (@PathVariable Long idUser){
        try {
            return ResponseEntity.ok(service.obtenerFavoritos(idUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar favorito: " + e.getMessage());
        }
    }

}
