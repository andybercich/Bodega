package org.example.Entities.Dto;

import lombok.Data;

@Data
public class ContactFormDTO {

    private String nombre;
    private String apellido;
    private String asunto;
    private String email;
    private String mensaje;

}