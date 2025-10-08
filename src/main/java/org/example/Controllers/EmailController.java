package org.example.Controllers;

import org.example.Entities.Compra;
import org.example.Entities.Dto.ContactFormDTO;
import org.example.Entities.Dto.ProblemFormDTO;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Entities.Usuario;
import org.example.Repositories.CompraRepository;
import org.example.Repositories.UsuarioRepository;
import org.example.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/contacto")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<String> sendContactEmail(@RequestBody ContactFormDTO form) {
        try {
            String body = form.getMensaje();
            String subject = form.getNombre() + " " + form.getApellido() + " - " + form.getAsunto();

            emailService.sendEmailAsync(subject, body, form.getEmail());

            return ResponseEntity.ok("Mensaje enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al mandar el mail: " + e.getMessage());
        }
    }

    @PostMapping("/problem")
    public ResponseEntity<?> sendPedidoProblema (@RequestBody ProblemFormDTO form){
        try {

            if (!compraRepository.existsById(form.getIdCompra()) || !usuarioRepository.existsById(form.getIdUser())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario o pedido no existen");
            }


            Compra compraDTO = compraRepository.findById(form.getIdCompra())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Compra no encontrada"));

            Usuario usuarioDTO = usuarioRepository.findById(form.getIdUser())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

            emailService.sendMailProblem(usuarioDTO, form.getMessage(), compraDTO);
            return ResponseEntity.ok("Mensaje enviado correctamente");



        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al mandar el mail con problema: " + e.getMessage());
        }
    }

    @PostMapping("/pedido/{idPedido}")
    public ResponseEntity<String> sendEmailChangeState(
            @PathVariable Long idPedido,
            @RequestParam("idUser") Long idUser,
                @RequestParam("newState") EstadoCompra newState) {
        try {
            emailService.sendEmailChangeState(idPedido,idUser,newState);
            return ResponseEntity.ok("Mensaje enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al mandar el mail: " + e.getMessage());
        }
    }

}


