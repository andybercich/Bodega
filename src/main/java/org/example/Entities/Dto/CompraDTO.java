package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraDTO {
    private Long id;
    private String codigoSeguimiento;
    private String estadoCompra;
    private LocalDateTime fechaCompra;
    private double total;
    private List<DetalleCompraDTO> detalles;
}
