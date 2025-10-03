package org.example.Controllers;

import org.example.Entities.Dto.PagoRequest;
import org.example.Services.MercadoPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mercadopago")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/crear-preferencia")
    public ResponseEntity<?> crearPreferencia(@RequestBody PagoRequest request) {

       //Se llama a tu service y se le pasan los items y el email que vienen del frontend.
        //Devuelve initPoint → la URL donde el usuario hará el pago.
        String initPoint = mercadoPagoService.crearPreferencia(request.getItems(), request.getEmail());

        if (initPoint == null) {
            //Si algo falla y no se pudo crear la preferencia, devolvemos un 400 Bad Request con un JSON de error.
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo generar la preferencia"));
        }

        return ResponseEntity.ok(Map.of("init_point", initPoint));
    }


}
