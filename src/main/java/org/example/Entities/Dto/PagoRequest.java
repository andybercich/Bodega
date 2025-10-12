package org.example.Entities.Dto;

import lombok.Data;
import java.util.List;

@Data
public class PagoRequest {
    private List<ItemRequest> items;
    private String email;
    private Long idCodigo;
    private Long idPedido;
}
