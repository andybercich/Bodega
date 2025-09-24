package org.example.Entities.Dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Entities.Direccion;
import org.example.Entities.Enum.Rol;
import org.example.Entities.Producto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UsuarioDTO {

    private long id;

    private int dni;

    private String mail;

    private Set<ProductoDTO> favoritos;

    private String nombre;

    private List<Direccion> direccions = new ArrayList<>();

}
