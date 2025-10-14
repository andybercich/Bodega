package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.DetalleCompra;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {    private Long id;
    private int cantidad;
    private double precioUnitario;
    private ProductoDTO producto;

    public static DetalleCompraDTO fromEntity(DetalleCompra detalle) {
        if (detalle == null) return null;

        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setId(detalle.getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario().doubleValue());
        if (detalle.getProducto() != null) {
            dto.setProducto(ProductoDTO.fromEntity(detalle.getProducto()));
        }

        return dto;
    }
}
