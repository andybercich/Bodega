package org.example.Entities.Dto;

import lombok.Data;

@Data
public class DireccionDTO {

    private Long id;

    private String pais;

    private String provincia;

    private String localidad;

    private String calle;

    private int numero;

    private String codigoPostal;
}
