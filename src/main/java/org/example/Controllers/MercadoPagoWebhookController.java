package org.example.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.Services.MercadoPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor //crea automáticamente un constructor con todos los atributos final para inyección de dependencias.
public class MercadoPagoWebhookController {

    //Este controller está pensado para recibir notificaciones automáticas de Mercado Pago, llamadas webhooks,
    // cada vez que cambia el estado de un pago.

    //Solo necesitas exponer tu endpoint /webhook públicamente para que Mercado Pago pueda enviar las notificaciones.

    private final MercadoPagoService mercadoPagoService;


    //Mercado Pago envía un JSON con información del pago
    //recibe los datos del webhook en un mapa.
    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> body) {
        System.out.println("Webhook recibido: " + body);

        try {
            Map<String, Object> data = (Map<String, Object>) body.get("data"); //obtenermos la data
            Long paymentId = Long.valueOf(data.get("id").toString());//obtenemos el id del pago

            //Llamamos a obtenerPagoPorId para tener todos los detalles del pago (monto, status, etc.).
            Map<String, Object> pago = mercadoPagoService.obtenerPagoPorId(paymentId);

            System.out.println("Pago consultado: " + pago);

            //Si el estado es "approved", podemos crear la orden en nuestra base de datos o actualizar el estado del pedido.
            if ("approved".equals(pago.get("status"))) {
                System.out.println("Pago aprobado, crear orden");
            }

            //Siempre hay que devolver 200 OK si procesamos el webhook correctamente, para que Mercado Pago no lo reintente.
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
