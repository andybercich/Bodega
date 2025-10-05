package org.example.Controllers;

import org.example.Entities.Dto.ContactFormDTO;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacto")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailController {

    @Autowired
    private EmailService emailService;

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
