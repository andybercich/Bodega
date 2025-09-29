package org.example.Entities.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = "El mail es obligatorio")
    @Size(max = 50, message = "El mail debe tener como máximo 50 caracteres")
    private String mail;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 100, message = "La contraseña debe tener como máximo 100 caracteres")
    private String password;
}
