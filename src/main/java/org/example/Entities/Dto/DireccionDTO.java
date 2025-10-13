package org.example.Entities.Dto;

import lombok.Data;
import org.example.Entities.Direccion;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DireccionDTO {

    private Long id;

    private String pais;

    private String provincia;

    private String localidad;

    private String calle;

    private int numero;

    private String codigoPostal;

    public static DireccionDTO fromEntity(Direccion direccion) {
        if (direccion == null) {
            return null;
        }

        DireccionDTO dto = new DireccionDTO();
        dto.setId(direccion.getId());
        dto.setPais(direccion.getPais());
        dto.setProvincia(direccion.getProvincia());
        dto.setLocalidad(direccion.getLocalidad());
        dto.setCalle(direccion.getCalle());
        dto.setNumero(direccion.getNumero());
        dto.setCodigoPostal(direccion.getCodigoPostal());
        return dto;
    }


    public static List<DireccionDTO> fromEntities(List<Direccion> direcciones) {
        if (direcciones == null) {
            return List.of();
        }
        return direcciones.stream()
                .map(DireccionDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
