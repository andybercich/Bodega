package org.example.Entities.Dto;

import lombok.Data;

@Data
public class CreateAdminDTO {

    private Long id;
    private boolean estado;
    private String nombre;
    private String mail;
    private String password;

}
