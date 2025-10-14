package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Compra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // Método fromEntity
    public static CompraDTO fromEntity(Compra compra) {
        if (compra == null) return null;

        CompraDTO dto = new CompraDTO();
        dto.setId(compra.getId());
        dto.setCodigoSeguimiento(compra.getCodigoSeguimiento());
        dto.setEstadoCompra(compra.getEstadoCompra() != null ? compra.getEstadoCompra().name() : null);
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setTotal(compra.getTotal().doubleValue());

        if (compra.getDetalles() != null) {
            dto.setDetalles(
                    compra.getDetalles().stream()
                            .map(DetalleCompraDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
