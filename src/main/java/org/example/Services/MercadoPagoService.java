package org.example.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Entities.Dto.ItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MercadoPagoService {

    @Autowired
    private final DescuentoService descuentoService;

    //token de prueba para crear preferencias
    @Value("${mercadopago.test-access-token}")
    private String accessToken;

    //Es la URL base de tu frontend. Se usa para armar las rutas a donde el usuario será redirigido después del pago
    @Value("${mercadopago.url-base}")
    private String urlBase;

    //sirve para hacer llamadas HTTP a la API de Mercado Pago.
    private final RestTemplate restTemplate = new RestTemplate();
    //sirve para convertir mapas o objetos a JSON antes de enviarlos en la petición
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MercadoPagoService(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    //Recibe una lista de productos (items) y el email del comprador.
    public String crearPreferencia(List<ItemRequest> items, String email) {
        try {

            //Si no hay productos, no hacemos nada y retornamos null.
            if (items == null || items.isEmpty()) return null;

            //es un mapa que representará el JSON que se enviará a Mercado Pago.
            Map<String, Object> payload = new HashMap<>();

            // Construir lista de items
            List<Map<String, Object>> itemsList = new ArrayList<>();

            //guarda cuantas unidades de cada producto hay en total
            Map<String, Integer> cantidadPorCodigo = new HashMap<>();

            //guarda una copia del producto original para tener el title
            Map<String, ItemRequest> itemPorCodigo = new HashMap<>();

            //si ya hay cantidad, suma la nueva cantidad; si no, pone la primera.
            for (ItemRequest item : items) {
                cantidadPorCodigo.put(item.getCodigo(),
                        cantidadPorCodigo.getOrDefault(item.getCodigo(), 0) + item.getQuantity());
                itemPorCodigo.putIfAbsent(item.getCodigo(), item); //guarda solo el primer item que aparece con ese código
            }

            for (String codigo : cantidadPorCodigo.keySet()) {
                ItemRequest item = itemPorCodigo.get(codigo);
                int cantidad = cantidadPorCodigo.get(codigo);
                BigDecimal precioFinal = descuentoService.calcularPrecioFinal(codigo);

                Map<String, Object> map = new HashMap<>();
                map.put("title", item.getTitle() + (cantidad > 1 ? " x" + cantidad : ""));
                map.put("quantity", cantidad);
                map.put("unit_price", precioFinal);
                map.put("picture_url", item.getPicture_url());
                map.put("currency_id", "ARS");
                itemsList.add(map);
            }

            //Luego agregamos la lista de productos y el email del comprador al payload.
            payload.put("items", itemsList);
            payload.put("payer", Map.of("email", email));

            //Configuramos rutas de redirección
            Map<String, String> backUrls = new HashMap<>();
            backUrls.put("success", urlBase + "/successful-payment");
            backUrls.put("failure", urlBase + "/failed-payment");
            backUrls.put("pending", urlBase + "/pending-payment");

            payload.put("back_urls", backUrls); ////URLs a las que Mercado Pago redirige al usuario después del pago.
            payload.put("auto_return", "approved"); //si el pago es exitoso, redirecciona automáticamente al usuario.

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); //indicamos que enviamos JSON
            headers.setBearerAuth(accessToken); //pasamos el token de acceso de Mercado Pago en el header

            //Convertimos el payload a JSON
            //HttpEntity combina cuerpo + headers para enviarlo en la petición.
            HttpEntity<String> request = new HttpEntity<>(
                    objectMapper.writeValueAsString(payload),
                    headers
            );

            //Llamamos a la API de mp
            ResponseEntity<Map> response = restTemplate.exchange( //La respuesta viene como un Map JSON.
                    "https://api.mercadopago.com/checkout/preferences",
                    HttpMethod.POST,
                    request,
                    Map.class //Cada clave del JSON se convierte en la clave del Map y cada valor en el valor del Map.
            );

            //response.getBody() devuelve el JSON que Mercado Pago te respondió, convertido automáticamente a un Map.
            Map<String, Object> responseBody = response.getBody();

            //init_point es la URL donde el usuario hará el pago.
            //La retornamos al frontend para que pueda redirigir al usuario.
            return responseBody != null ? responseBody.get("init_point").toString() : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

        /*Recibe el ID de un pago que ya fue generado en Mercado Pago.
        Hace una llamada GET a la API de Mercado Pago para obtener información detallada sobre ese pago.
        Devuelve un Map con los datos del pago (status, monto, fecha, etc.).*/
    public Map<String, Object> obtenerPagoPorId(Long id) {
        try {

            //endpoint para consultar un pago
            String url = "https://api.mercadopago.com/v1/payments/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken); //token de acceso para autenticar la petición.
            headers.setContentType(MediaType.APPLICATION_JSON); //JSON, aunque para GET no es estrictamente necesario.

            //Solo necesitamos los headers para la autenticación
            HttpEntity<Void> request = new HttpEntity<>(headers);


            //La respuesta es un JSON que RestTemplate convierte automáticamente a un Map.
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            //Devuelve todos los datos del pago.
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "error", "message", "No se pudo consultar el pago");
        }
    }

}
