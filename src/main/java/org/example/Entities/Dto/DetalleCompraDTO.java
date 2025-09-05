package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {
    private Long id;
    private int cantidad;
    private double precioUnitario;
    private ProductoDTO producto;
}
