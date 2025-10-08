package org.example.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AplicarCodigo {
    private boolean valido;

    private double porcentajeDescuento;

    private double tope;

    private Long idCodigo;
}
