package org.example.Entities.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.Entities.Usuario;

@Data
public class EnvioCodigoDescuento {

    @Size(min = 4, max = 15, message = "El código de descuento debe tener entre 3 y 20 caracteres")
    private String codigoDescuento;

    private Long idUsuario;


}
