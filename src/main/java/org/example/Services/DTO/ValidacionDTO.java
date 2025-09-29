package org.example.Services.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidacionDTO {
    @NotNull(message = "El mail no puede ser nulo")
    private String mail;

    @NotNull(message = "El código de verificación no puede ser nulo")
    @Size( max = 7, message = "El código debe tener 7 caracteres")
    private String codVerificacion;
}