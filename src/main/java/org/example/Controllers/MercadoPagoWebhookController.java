package org.example.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.Services.CompraService;
import org.example.Services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CompraService compraService;


    //Mercado Pago envía un JSON con información del pago
    //recibe los datos del webhook en un mapa.
    //Cambia el estado de la compra a Preparando
    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> body) {
        System.out.println("Webhook recibido: " + body);

        try {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            Long paymentId = Long.valueOf(data.get("id").toString());

            Map<String, Object> pago = mercadoPagoService.obtenerPagoPorId(paymentId);
            System.out.println("Pago consultado: " + pago);

            String externalReference = (String) pago.get("external_reference");
            if (externalReference != null && "approved".equals(pago.get("status"))) {
                Long idCompra = Long.valueOf(externalReference);

                boolean confirmado = compraService.confirmarPago(idCompra);
                if (confirmado) {
                    System.out.println("Pedido " + idCompra + " confirmado y actualizado a PREPARANDO");
                }
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error procesando webhook");
        }
    }


}
