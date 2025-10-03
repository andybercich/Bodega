package org.example.Entities.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DataUser {
    @Size(min = 3, max = 50, message = "nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Min(value = 1000000, message = "DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "DNI debe tener como máximo 8 dígitos")
    private Integer dni;
}
