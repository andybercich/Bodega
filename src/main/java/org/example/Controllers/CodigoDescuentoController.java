package org.example.Controllers;

import org.example.Entities.AplicarCodigo;
import org.example.Entities.CodigoDescuento;
import org.example.Entities.Dto.EnvioCodigoDescuento;
import org.example.Entities.Usuario;
import org.example.Repositories.CodigoDescuentoRepository;
import org.example.Repositories.UsuarioRepository;
import org.example.Services.CodigoDescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codDescuento")
public class CodigoDescuentoController extends BaseController<CodigoDescuento, Long, CodigoDescuentoRepository,
        CodigoDescuentoService>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CodigoDescuentoController(CodigoDescuentoService service) {
        super(service);
    }


    @PostMapping("/aplicarCodigo")
    public ResponseEntity<?> validarCodigo(@RequestBody EnvioCodigoDescuento envioCodigoDescuento) {

        try {

            Usuario usuario = usuarioRepository.findById(envioCodigoDescuento.getIdUsuario())
                    .orElse(null);

            if (usuario == null) {
                return ResponseEntity
                        .status(450)
                        .body("Este usuario no existe");
            }

            AplicarCodigo aplicarCodigo = service.validarCodigo(envioCodigoDescuento.getCodigoDescuento(), usuario);

            if (aplicarCodigo.getPorcentajeDescuento() == 0.0 || !aplicarCodigo.isValido() ){
                return ResponseEntity.status(450).body("Este codigo descuento no puede ser aplicado");
            }

            return ResponseEntity.ok(aplicarCodigo);


        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
