package org.example.Controllers;

import jakarta.validation.Valid;
import org.example.Entities.Categoria;
import org.example.Entities.Dto.*;
import org.example.Entities.Producto;
import org.example.Entities.Usuario;
import org.example.Entities.UsuariosNuevos;
import org.example.Repositories.UsuarioRepository;
import org.example.Services.DTO.ValidacionDTO;
import org.example.Services.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseController<Usuario, Long, UsuarioRepository, UsuarioService>{
    public UsuarioController(UsuarioService service) {
        super(service);
    }


    @PostMapping("/registrarUsuario")
    public ResponseEntity<?> registrarNuevoUsuario(@RequestBody @Valid UsuariosNuevos usuario) {
        try {

            if (service.mailExistente(usuario.getMail())){
                return ResponseEntity.status(471).body("Este mail ya esta registrado");
            }
            UsuariosNuevos newUser = service.registrarUsuario(usuario);

             return ResponseEntity.ok("Se ha enviado el codigo de verificacion al mail: "+usuario.getMail());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar al usuario: " + e.getMessage());
        }
    }

    @PostMapping("/validarMail")
    public ResponseEntity<?> validarMail(@RequestBody @Valid  ValidacionDTO validacionDTO) {


            Usuario newUser = service.validarCodigoYRegistrar(validacionDTO);

            return ResponseEntity.ok(newUser);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {


        Usuario usuario = service.login(loginDTO);

        return ResponseEntity.ok(usuario);

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
    public ResponseEntity<?> obtenerFavoritos (@PathVariable Long idUser) {
        try {
            return ResponseEntity.ok(service.obtenerFavoritos(idUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar favorito: " + e.getMessage());

        }
    }

    @GetMapping("/paginados")
    public ResponseEntity<?> getUsuariosPaginados(
            @RequestParam int page,
            @RequestParam int size) throws Exception{
        try {
            Page<Usuario> usuarios = service.getUsuariosPaginados(page, size);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener categorias paginadas: " + e.getMessage());

        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> crearAdmin(@RequestBody @Valid CreateAdminDTO dto) {
        try {
            Usuario admin = service.crearUserAdmin(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(admin);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear user Admin: " + e.getMessage());
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUserByAdmin(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserDTO dto) {
        try{
            Usuario updated = service.updateUserByAdmin(id, dto);
            return ResponseEntity.ok(updated);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar user desde Admin: " + e.getMessage());
        }
    }

    @PutMapping("/dataUser/{idUser}")
    public ResponseEntity<?> updateDataUser(@RequestBody @Valid DataUser dataUser, @PathVariable Long idUser){
        try {
            UsuarioDTO updateUser = service.updateDataUser(idUser, dataUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(updateUser);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al modificar data User: " + e.getMessage());
        }
    }

}
