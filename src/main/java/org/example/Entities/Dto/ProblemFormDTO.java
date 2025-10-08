package org.example.Entities.Dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class
ProblemFormDTO {

    @Size(min = 3, max = 250, message = "nombre debe tener entre 3 y 100 caracteres")
    private String message;

    private Long idCompra;

    private Long idUser;

}
