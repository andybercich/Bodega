package org.example.Entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Dto.DireccionDTO;
import org.example.Entities.Dto.ProductoDTO;
import org.example.Entities.Enum.Rol;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String nombre;
    private int dni;
    private String mail;
    private Rol rol;
    private List<DireccionDTO> direcciones;
    private LocalDate fechaRegistro;
    private Set<ProductoDTO> favoritos = new HashSet<>();
    private String token;

}
