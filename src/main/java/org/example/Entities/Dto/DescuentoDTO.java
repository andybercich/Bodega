package org.example.Entities.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Entities.Descuento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DescuentoDTO {

    private double valor;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    public static DescuentoDTO fromEntity(Descuento descuento){
        return new DescuentoDTO(descuento.getValor(), descuento.getFechaInicio(), descuento.getFechaFin());
    }

    public static List<DescuentoDTO> fromEntitys(List<Descuento> descuentos){
        List<DescuentoDTO> descuentoDTOS = new ArrayList<>();
        for (Descuento descuento : descuentos) {
            descuentoDTOS.add(fromEntity(descuento));
        }
        return descuentoDTOS;
    }

}
